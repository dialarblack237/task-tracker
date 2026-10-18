package com.example.task_tracker.support;

import com.example.task_tracker.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

public final class Users {

    private Users() {}

    public static User withId(Long id) {
        User u = new User();
        ReflectionTestUtils.setField(u, "id", id);
        return u;
    }

    public static User withIdAndEmail(Long id, String email) {
        User u = withId(id);
        u.setEmail(email);
        return u;
    }
}