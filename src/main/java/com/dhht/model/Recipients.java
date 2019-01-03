package com.dhht.model;


import lombok.Data;

@Data
public class Recipients {
    private String id;

    private String recipientsName;

    private String recipientsAddress;

    private String recipientsTelphone;

    private String loginTelphone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRecipientsName() {
        return recipientsName;
    }

    public void setRecipientsName(String recipientsName) {
        this.recipientsName = recipientsName == null ? null : recipientsName.trim();
    }

    public String getRecipientsAddress() {
        return recipientsAddress;
    }

    public void setRecipientsAddress(String recipientsAddress) {
        this.recipientsAddress = recipientsAddress == null ? null : recipientsAddress.trim();
    }

    public String getRecipientsTelphone() {
        return recipientsTelphone;
    }

    public void setRecipientsTelphone(String recipientsTelphone) {
        this.recipientsTelphone = recipientsTelphone == null ? null : recipientsTelphone.trim();
    }

    public String getLoginTelphone() {
        return loginTelphone;
    }

    public void setLoginTelphone(String loginTelphone) {
        this.loginTelphone = loginTelphone == null ? null : loginTelphone.trim();
    }
}