package com.capgemini.test.code.notification;

import org.springframework.stereotype.Component;

import com.capgemini.test.code.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SmsNotifier implements Notifier {

    @Override
    public boolean supports(String rol) {
        return "superadmin".equalsIgnoreCase(rol);
    }

    @Override
    public void notify(User user) {
        log.info("Enviando SMS a {} con mensaje: usuario guardado", user.getPhone());
    }
    
}
