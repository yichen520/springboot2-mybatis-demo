package com.dhht.service.recipients;

import com.dhht.model.Recipients;
import com.dhht.model.User;

import java.util.List;

public interface RecipientsService {

    int insertRecipients(Recipients recipients,String currentUserMobilePhone);

    int updateRecipients(Recipients recipients,String  currentUserMobilePhone);

    int deleteRecipients(String id);

    List<Recipients> recipientsList(String loginTelphone);
}
