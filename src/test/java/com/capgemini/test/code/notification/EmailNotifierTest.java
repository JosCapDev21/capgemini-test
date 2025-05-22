package com.capgemini.test.code.notification;

import org.junit.jupiter.api.Test;

import com.capgemini.test.code.entity.Role;
import com.capgemini.test.code.entity.User;

import static org.junit.jupiter.api.Assertions.*;

public class EmailNotifierTest {
    
    private final EmailNotifier notifier = new EmailNotifier();

    @Test
    void shouldNotifyAdminRole() {
        assertTrue(notifier.supports("admin"));
        assertTrue(notifier.supports("ADMIN"));
        assertFalse(notifier.supports("superadmin"));
    }

    void shouldNotifyByEmail() {
        User user = new User();
        user.setEmail("admin@email.com");
        user.setRol(Role.ADMIN);

        assertDoesNotThrow(() -> notifier.notify(user));
    }
}
