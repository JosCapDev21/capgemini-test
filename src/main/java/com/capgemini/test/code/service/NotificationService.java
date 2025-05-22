package com.capgemini.test.code.service;

import com.capgemini.test.code.entity.Role;
import com.capgemini.test.code.entity.User;

public interface NotificationService {
    
    void notify(String contact, Role role);
    
    void notifyUser(User user);

    void sendEmail(String email, String message);

    void sendSms(String phone, String message);

}