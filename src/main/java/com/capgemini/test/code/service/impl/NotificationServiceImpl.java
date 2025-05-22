package com.capgemini.test.code.service.impl;

import com.capgemini.test.code.entity.Role;
import com.capgemini.test.code.entity.User;
import com.capgemini.test.code.notification.Notifier;
import com.capgemini.test.code.service.NotificationService;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final List<Notifier> notifiers;
    
    @Override
    public void notify(String contact, Role role) {
        if (role == Role.ADMIN) {
            System.out.println("Enviando email a " + contact + ": usuario guardado");
        } else {
            System.out.println("Enviando SMS a " + contact + ": usuario guardado");
        }
    }

    @Override
    public void notifyUser(User user) {
        /*String rol = user.getRol().toString().toLowerCase();

        if ("admin".equals(rol)) {
            sendEmail(user.getEmail(), "usuario guardado");
        } else if ("superadmin".equals(rol)) {
            sendSms(user.getPhone(), "usuario guardado");
        } else {
            log.warn("Rol desconocido: no se notificará a {}", user.getEmail());
        }*/

        notifiers.stream().filter(
            n -> n.supports(user.getRol().name())).findFirst()
            .ifPresentOrElse(notifier -> notifier.notify(user), 
            () -> log.warn("No hay estrategia de notificación para el rol {}", user.getRol()));

    }

    @Override
    public void sendEmail(String email, String message) {
        log.info("Envienado email a {} con mensaje: {}", email, message);

    }

    @Override
    public void sendSms(String phone, String message) {
        log.info("Enviando SMS a {} con mensaje: {}", phone, message);

    }
}
