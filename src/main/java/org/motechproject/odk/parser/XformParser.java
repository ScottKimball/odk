package org.motechproject.odk.parser;

import org.motechproject.odk.domain.FormDefinition;

import javax.xml.xpath.XPathExpressionException;

public interface XformParser {

    FormDefinition parse(String xForm, String configurationName) throws XPathExpressionException;
}
