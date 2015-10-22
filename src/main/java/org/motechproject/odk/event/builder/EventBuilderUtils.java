package org.motechproject.odk.event.builder;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;

/**
 * Utility class for {@link org.motechproject.odk.event.builder.EventBuilder}
 */
public final class EventBuilderUtils {

    private EventBuilderUtils() { }

    public static String formatStringList(Object value) {
        String s = (String) value;
        return s.replace(' ', '\n');
    }

    public static String formatStringArray(List<String> value) {
        if (value.size() == 0 || value.get(0) == null) {
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
        if (value.size() == 0 || value.get(0) == null) {
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

    public static String formatAsJson(Object value) {
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (Exception e) {
            return null;
        }
    }


}
