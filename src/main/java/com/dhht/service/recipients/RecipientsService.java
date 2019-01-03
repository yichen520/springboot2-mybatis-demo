package com.dhht.service.recipients;

import com.dhht.model.Recipients;
import com.dhht.model.User;

import java.util.List;

public interface RecipientsService {

    int insertRecipients(Recipients recipients,User user);

    int updateRecipients(Recipients recipients,User user);

    int deleteRecipients(String id);

    List<Recipients> recipientsList(User user);
}
