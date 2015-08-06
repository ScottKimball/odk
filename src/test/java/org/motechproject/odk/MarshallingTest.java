package org.motechproject.odk;

import org.apache.commons.io.FileUtils;

import org.junit.Test;

import org.motechproject.odk.domain.FormField;
import org.motechproject.odk.parser.XformParser;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import javax.xml.xpath.XPathFactory;

import java.io.ByteArrayInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarshallingTest {


    @Test
    public void testXPath() throws Exception {

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        HashMap<String, String> prefMap = new HashMap<String, String>() {{
            put("xForms", "http://www.w3.org/2002/xforms");
            put("ev", "http://www.w3.org/2001/xml-events");
            put("h", "http://www.w3.org/1999/xhtml");
            put("jr", "http://openrosa.org/javarosa");
            put("orx", "http://openrosa.org/xforms");
            put("xsd", "http://www.w3.org/2001/XMLSchema");
        }};
        SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
        namespaces.setBindings(prefMap);
        xPath.setNamespaceContext(namespaces);
        File f = new File("odk/src/test/resources/widgets.xml");
        String xml = FileUtils.readFileToString(f);
        InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
        NodeList nodeList = (NodeList) xPath.compile("/h:html/h:head/xForms:model/xForms:bind").evaluate(inputSource, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
            System.out.println();

        }
    }


}
