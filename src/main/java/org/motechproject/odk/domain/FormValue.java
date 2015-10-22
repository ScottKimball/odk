package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Base class for a value in a form instance.
 */
@Entity
public class FormValue {

    @Field
    private String name;

    @Field
    private String label;

    @Field
    private String type;


    /*MDS doesn't seem to like collections of abstract classes so this is the workaround*/
    protected FormValue(String name, String label, String type) {
        this.name = name;
        this.label = label;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
