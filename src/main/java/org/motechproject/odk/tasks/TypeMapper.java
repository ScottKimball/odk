package org.motechproject.odk.tasks;

import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.constant.TasksDataTypes;

/**
 * Utility class for mapping from external types to tasks types.
 */
public final class TypeMapper {

    private TypeMapper() { }

    /**
     * Maps from an XML form type to a tasks type.
     * @param type The type described on the XML form
     * @return the tasks type.
     */
    public static String getType(String type) {

        switch (type) {
            case FieldTypeConstants.INT:
                return TasksDataTypes.INTEGER;

            case FieldTypeConstants.DATE_TIME:
                return TasksDataTypes.UNICODE;

            case FieldTypeConstants.BOOLEAN:
                return TasksDataTypes.UNICODE;

            case FieldTypeConstants.DECIMAL:
                return TasksDataTypes.DOUBLE;

            case FieldTypeConstants.DATE:
                return TasksDataTypes.UNICODE;

            case FieldTypeConstants.TIME:
                return TasksDataTypes.UNICODE;

            case FieldTypeConstants.SELECT_1:
                return TasksDataTypes.LIST;

            default:
                return TasksDataTypes.UNICODE;
        }
    }
}
