package org.motechproject.odk.parser.impl;

import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.parser.XformParser;
import org.motechproject.odk.parser.XformParserException;
import org.motechproject.odk.tasks.FieldTypeConstants;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XformParserODK implements XformParser {

    protected static XPath XPATH;
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

    protected static final String TITLE_PATH = "/h:html/h:head/h:title";
    protected static final String BIND_ELEMENTS = "/h:html/h:head/xForms:model/xForms:bind";
    protected static final String ROOT_PATH = "/";
    protected static final String NODE_SET = "nodeset";
    protected static final String TYPE = "type";
    protected static final String STRING = "string";
    protected static final String FORM_ELEMENTS_PARENT_PATH = "/h:html/h:head/xForms:model/xForms:instance/*[@id]";



    public  FormDefinition parse (String xForm, String configurationName) throws XformParserException {
        try {
            InputSource inputSource = new InputSource(new ByteArrayInputStream(xForm.getBytes()));
            Node root = getRoot(inputSource);
            return parseXForm(configurationName, root);
        } catch (XPathExpressionException e) {
            throw new XformParserException("Error parsing xForm", e);
        }

    }

    protected FormDefinition parseXForm( String configurationName, Node root) throws XPathExpressionException{
        Map<String,FormElement> formElementMap = new HashMap<>();
        String title = XPATH.compile(TITLE_PATH).evaluate(root);
        FormDefinition formDefinition = new FormDefinition(configurationName);
        formDefinition.setTitle(title);

        Node node =(Node) XPATH.compile(FORM_ELEMENTS_PARENT_PATH).evaluate(root, XPathConstants.NODE);
        String uri = node.getNodeName();
        NodeList formElementsList = node.getChildNodes();

        recursivelyAddFormElements(formElementMap, formElementsList,"/" + uri);
        NodeList binds = (NodeList) XPATH.compile(BIND_ELEMENTS).evaluate(root, XPathConstants.NODESET);
        addBindInformationToFormFields(formElementMap, binds);
        formDefinition.setFormElements(createFormElementListFromMap(formElementMap));
        return formDefinition;
    }

    protected Node getRoot(InputSource inputSource) throws XPathExpressionException {
        return (Node) XPATH.evaluate(ROOT_PATH, inputSource, XPathConstants.NODE);
    }


    private void addBindInformationToFormFields(Map<String, FormElement> formElementMap, NodeList binds ) {
        for (int i = 0; i < binds.getLength(); i++) {
            Node bind = binds.item(i);
            NamedNodeMap attributes = bind.getAttributes();
            String fieldUri = attributes.getNamedItem(NODE_SET).getNodeValue();
            FormElement formElement = formElementMap.get(fieldUri);
            Node typeNode = attributes.getNamedItem(TYPE);

            if (formElement != null) {
                String type;
                if (typeNode == null) {
                    type = STRING;
                } else {
                    type = typeNode.getNodeValue();
                }
                formElement.setType(type);
            }
        }
    }

    private void recursivelyAddFormElements(Map<String, FormElement> formElementMap, NodeList nodeList, String uri) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node element = nodeList.item(i);
            if (hasChildElements(element)) {

                if (element.hasAttributes()) { // repeat group
                    FormElement formElement = new FormElement( uri + "/" + element.getNodeName());
                    formElement.setLabel(formElement.getName());
                    formElementMap.put(formElement.getName(), formElement);
                    formElement.setType(FieldTypeConstants.REPEAT_GROUP);
                    formElement.setChildren(new ArrayList<FormElement>());
                    recursivelyAddGroup(formElementMap, element.getChildNodes(), uri + "/" + element.getNodeName(), formElement.getChildren(), formElement);
                } else {
                    recursivelyAddFormElements(formElementMap, element.getChildNodes(), uri + "/" + element.getNodeName());

                }
            } else if (element.getNodeType() == Node.ELEMENT_NODE) {
                FormElement formElement = new FormElement( uri + "/" + element.getNodeName());
                formElement.setLabel(formElement.getName());
                formElementMap.put(formElement.getName(), formElement);
            }
        }
    }


    private void recursivelyAddGroup (Map<String, FormElement> formElementMap, NodeList nodeList, String uri, List<FormElement> group, FormElement parent) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node element = nodeList.item(i);

            if (element.hasChildNodes()) {

                if (element.hasAttributes()) {
                    FormElement formElement = new FormElement( uri + "/" + element.getNodeName());
                    formElementMap.put(formElement.getName(), formElement);
                    formElement.setType(FieldTypeConstants.REPEAT_GROUP);
                    formElement.setChildren(new ArrayList<FormElement>());
                    group.add(formElement);
                    recursivelyAddGroup(formElementMap, element.getChildNodes(), uri + "/" + element.getNodeName(), formElement.getChildren(), formElement);

                } else {
                    recursivelyAddFormElements(formElementMap, element.getChildNodes(), uri + "/" + element.getNodeName());
                }

            } else if (element.getNodeType() == Node.ELEMENT_NODE){
                FormElement formElement = new FormElement( uri + "/" + element.getNodeName());
                group.add(formElement);
                formElementMap.put(formElement.getName(), formElement);
            }
        }

        for (FormElement child : group) {
            child.setParent(parent);
        }
    }

    protected boolean hasChildElements (Node element) {
        NodeList children = element.getChildNodes();
        for (int i = 0;i < children.getLength();i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE)
                return true;
        }
        return false;
    }

    private List<FormElement> createFormElementListFromMap(Map<String, FormElement> formElementMap) {
        List<FormElement> formElements = new ArrayList<>();
        for (FormElement formElement : formElementMap.values()) {
            if (!(formElement.hasParent() && formElement.getParent().getType().equals(FieldTypeConstants.REPEAT_GROUP))) {
                formElements.add(formElement);
            }
        }
        return formElements;
    }
}
