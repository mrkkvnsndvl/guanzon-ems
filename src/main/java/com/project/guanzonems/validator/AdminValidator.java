package com.project.guanzonems.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.regex.Pattern;

public class AdminValidator {

    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final int MIN_PASSWORD_LENGTH = 8;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ObjectNode validateAdmin(String email, String password) {
        ObjectNode validationErrors = objectMapper.createObjectNode();
        validateEmail(email, validationErrors);
        validatePassword(password, validationErrors);
        return validationErrors;
    }

    private void validateEmail(String email, ObjectNode validationErrors) {
        if (email == null || email.trim().isEmpty()) {
            validationErrors.put("email", "Email is required.");
        } else if (!Pattern.matches(EMAIL_REGEX, email)) {
            validationErrors.put("email", "Invalid email format.");
        }
    }

    private void validatePassword(String password, ObjectNode validationErrors) {
        if (password == null || password.trim().isEmpty()) {
            validationErrors.put("password", "Password is required.");
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            validationErrors.put("password", "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }
    }
}
