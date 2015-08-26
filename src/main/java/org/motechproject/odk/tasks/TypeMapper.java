package org.motechproject.odk.tasks;

import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.constant.TasksDataTypes;

import java.util.HashMap;
import java.util.Map;

public class TypeMapper {

    private static final Map<String, String> TYPE_MAP = new HashMap<String,String>(){{
        put(FieldTypeConstants.STRING, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.DATE_TIME,TasksDataTypes.DATE);
        put(FieldTypeConstants.BOOLEAN, TasksDataTypes.BOOLEAN);
        put(FieldTypeConstants.INT, TasksDataTypes.INTEGER);
        put(FieldTypeConstants.DECIMAL, TasksDataTypes.DOUBLE);
        put(FieldTypeConstants.DATE, TasksDataTypes.DATE);
        put(FieldTypeConstants.TIME,TasksDataTypes.TIME);
        put(FieldTypeConstants.SELECT, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.SELECT_1, TasksDataTypes.LIST);
        put(FieldTypeConstants.GEOPOINT, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.GEOTRACE, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.GEOSHAPE, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.DOUBLE_ARRAY, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.STRING_ARRAY, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.REPEAT_GROUP, TasksDataTypes.UNICODE);
    }};

    public static String getType(String type) {
        return TYPE_MAP.get(type);
    }
}
