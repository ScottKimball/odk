package org.motechproject.odk.service;

import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.Verification;

public interface VerificationService {

    Verification verifyKobo(Configuration configuration);
    Verification verifyOna(Configuration configuration);
    Verification verifyOdk(Configuration configuration);


}
