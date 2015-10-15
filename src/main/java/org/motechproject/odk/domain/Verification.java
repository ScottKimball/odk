package org.motechproject.odk.domain;

public class Verification {

    boolean verified;

    public Verification(boolean verified) {
        this.verified = verified;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
