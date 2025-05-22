package com.capgemini.test.code.notification;

import com.capgemini.test.code.entity.User;

public interface Notifier {
    
    boolean supports(String rol);

    void notify(User user);
    
}
