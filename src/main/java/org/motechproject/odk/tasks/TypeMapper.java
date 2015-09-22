package org.motechproject.odk.tasks;

import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.constant.TasksDataTypes;

import java.util.HashMap;
import java.util.Map;

public class TypeMapper {

    public static String getType(String type) {

        switch (type) {
            case FieldTypeConstants.INT:
                return  TasksDataTypes.INTEGER;

            case FieldTypeConstants.DATE_TIME:
                return TasksDataTypes.UNICODE; // TODO

            case FieldTypeConstants.BOOLEAN:
                return TasksDataTypes.UNICODE; // TODO

            case FieldTypeConstants.DECIMAL:
                return TasksDataTypes.DOUBLE;

            case FieldTypeConstants.DATE:
                return TasksDataTypes.UNICODE; // TODO

            case FieldTypeConstants.TIME:
                return TasksDataTypes.UNICODE; //TODO

            case FieldTypeConstants.SELECT_1:
                return TasksDataTypes.LIST;

            default:
                return TasksDataTypes.UNICODE;
        }
    }
}
