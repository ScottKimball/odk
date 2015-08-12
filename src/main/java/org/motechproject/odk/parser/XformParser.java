package org.motechproject.odk.parser;

import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormField;
import org.motechproject.odk.tasks.FieldTypeConstants;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XformParser {

    private static XPath XPATH;
    private static final HashMap<String, String> NAMESPACE_MAP = new HashMap<String, String>() {{
        put("xForms", "http://www.w3.org/2002/xforms");
        put("ev", "http://www.w3.org/2001/xml-events");
        put("h", "http://www.w3.org/1999/xhtml");
        put("jr", "http://openrosa.org/javarosa");
        put("orx", "http://openrosa.org/xforms");
        put("xsd", "http://www.w3.org/2001/XMLSchema");
    }};

    static {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPATH = xPathFactory.newXPath();
        SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
        namespaces.setBindings(NAMESPACE_MAP);
        XPATH.setNamespaceContext(namespaces);
    }

    private static final String TITLE = "/h:html/h:head/h:title";
    private static final String BIND_ELEMENTS = "/h:html/h:head/xForms:model/xForms:bind";
    private static final String NODE_SET = "nodeset";
    private static final String TYPE = "type";
    private static final String STRING = "string";
    private static final String DATA = "data";
    private static final String DATA_PATH = "//xForms:" + DATA + "/*";


    public static FormDefinition parse (String xForm, String configurationName)  throws Exception{
        InputSource inputSource = new InputSource(new ByteArrayInputStream(xForm.getBytes()));
        Node root = (Node) XPATH.evaluate("/", inputSource, XPathConstants.NODE);
        String title = XPATH.compile(TITLE).evaluate(root);

        FormDefinition formDefinition = new FormDefinition(configurationName);
        formDefinition.setTitle(title);

        title = (Character.toLowerCase(title.charAt(0)) + title.substring(1));

        String uri;
        NodeList formElementsList;
        if (title.contains(" ")) {
            uri = DATA;
            formElementsList = (NodeList) XPATH.compile(DATA_PATH).evaluate(root, XPathConstants.NODESET);

        } else {
            String formElements = "//xForms:" + title + "/*";
            formElementsList = (NodeList) XPATH.compile(formElements).evaluate(root, XPathConstants.NODESET);

            if (formElementsList.getLength() == 0) {
                uri = DATA;
                formElementsList = (NodeList) XPATH.compile(DATA_PATH).evaluate(root, XPathConstants.NODESET);

            } else {
                uri = title;
            }
        }

        Map<String,FormField> formFieldMap = new HashMap<>();
        recursivelyAddFormFields(formFieldMap, formElementsList, "/" + uri);
        NodeList binds = (NodeList) XPATH.compile(BIND_ELEMENTS).evaluate(root, XPathConstants.NODESET);
        addBindInformationToFormFields(formFieldMap, binds);
        formDefinition.setFormFields(new ArrayList<>(formFieldMap.values()));
        return formDefinition;
    }

    private static void addBindInformationToFormFields(Map<String, FormField> formFieldMap, NodeList binds ) {
        for (int i = 0; i < binds.getLength(); i++) {
            Node bind = binds.item(i);
            NamedNodeMap attributes = bind.getAttributes();
            String fieldUri = attributes.getNamedItem(NODE_SET).getNodeValue();
            FormField formField = formFieldMap.get(fieldUri);
            Node typeNode = attributes.getNamedItem(TYPE);

            if (formField != null) {
                String type;
                if (typeNode == null) {
                    type = STRING;
                } else {
                    type = typeNode.getNodeValue();
                }
                formField.setType(type);
            }
        }
    }

    private static void recursivelyAddFormFields(Map<String, FormField> formFieldMap, NodeList nodeList, String uri) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node element = nodeList.item(i);
            if (hasChildElements(element)) {

                if (element.hasAttributes()) { // repeat group
                    FormField formField = new FormField( uri + "/" + element.getNodeName());
                    formFieldMap.put(formField.getName(), formField);
                    formField.setType(FieldTypeConstants.REPEAT_GROUP);
                    formField.setChildren(new ArrayList<FormField>());
                    recursivelyAddGroup(formFieldMap, element.getChildNodes(), uri + "/" + element.getNodeName(), formField.getChildren());
                } else {
                    recursivelyAddFormFields(formFieldMap, element.getChildNodes(), uri + "/" + element.getNodeName());

                }
            } else if (element.getNodeType() == Node.ELEMENT_NODE) {
                FormField formField = new FormField( uri + "/" + element.getNodeName());
                formFieldMap.put(formField.getName(),formField);
            }
        }
    }


    private static void recursivelyAddGroup (Map<String, FormField> formFieldMap, NodeList nodeList, String uri, List<FormField> group) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node element = nodeList.item(i);

            if (element.hasChildNodes()) {

                if (element.hasAttributes()) {
                    FormField formField = new FormField( uri + "/" + element.getNodeName());
                    formFieldMap.put(formField.getName(), formField);
                    formField.setType(FieldTypeConstants.REPEAT_GROUP);
                    formField.setChildren(new ArrayList<FormField>());
                    group.add(formField);
                    recursivelyAddGroup(formFieldMap, element.getChildNodes(), uri + "/" + element.getNodeName(), formField.getChildren());

                } else {
                    recursivelyAddFormFields(formFieldMap, element.getChildNodes(), uri + "/" + element.getNodeName());
                }

            } else {
                FormField formField = new FormField( uri + "/" + element.getNodeName());
                group.add(formField);
                formFieldMap.put(formField.getName(), formField);
            }
        }
    }

    private static boolean hasChildElements (Node element) {
        NodeList children = element.getChildNodes();
        for (int i = 0;i < children.getLength();i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE)
                return true;
        }
        return false;
    }
}
