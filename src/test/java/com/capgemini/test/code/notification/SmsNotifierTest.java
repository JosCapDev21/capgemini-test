package com.capgemini.test.code.notification;

import org.junit.jupiter.api.Test;

import com.capgemini.test.code.entity.Role;
import com.capgemini.test.code.entity.User;

import static org.junit.jupiter.api.Assertions.*;

public class SmsNotifierTest {
    private final SmsNotifier notifier = new SmsNotifier();

    @Test
    void shouldSupportSuperAdminRole() {
        assertTrue(notifier.supports("superadmin"));
        assertTrue(notifier.supports("SUPERADIN"));
        assertFalse(notifier.supports("admin"));
    }

    @Test
    void shouldNotifyBySms() {
        User user = new User();
        user.setPhone("600000000");
        user.setRol(Role.SUPERADMIN);

        assertDoesNotThrow(() -> notifier.notify(user));
    }
}
