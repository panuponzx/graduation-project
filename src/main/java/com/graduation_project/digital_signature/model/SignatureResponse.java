package com.graduation_project.digital_signature.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignatureResponse {

    private String file;
    private String status;
    private String signedBy;
    private String signDate;
    private String certificateNumber;
    private String expires;

    public void setFile(String file) {
        this.file = file;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
}
