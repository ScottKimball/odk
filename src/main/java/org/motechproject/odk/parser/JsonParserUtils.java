package org.motechproject.odk.parser;

import java.util.List;

public class JsonParserUtils {

    public static String formatStringList(Object value) {
        String s = (String) value;
        return s.replace(' ', '\n');
    }

    public static String formatStringArray(List<String> value) {
        if (value.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(value.get(0));
        for (int i = 1; i < value.size(); i++) {
            builder.append("\n");
            builder.append(value.get(i));
        }
        return builder.toString();
    }

    public static String formatDoubleArray(List<Double> value) {
        if (value.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(value.get(0).toString());
        for (int i = 1; i < value.size(); i++) {
            builder.append("\n");
            builder.append(value.get(i));
        }
        return builder.toString();
    }


}
