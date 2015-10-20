package org.motechproject.odk.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.odk.domain.FormDefinition;

import java.util.List;

public interface FormDefinitionDataService extends MotechDataService<FormDefinition> {

    @Lookup
    FormDefinition byTitle(@LookupField(name = "title") String title);

    @Lookup
    List<FormDefinition> byConfigurationName(@LookupField(name = "configurationName") String configurationName);

    @Lookup
    FormDefinition byConfigurationNameAndTitle(@LookupField(name = "configurationName") String configurationName, @LookupField(name = "title") String title);

}
