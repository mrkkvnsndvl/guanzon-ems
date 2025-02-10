package com.project.guanzonems.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.guanzonems.dao.DepartmentDAO;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class DepartmentValidator {

    private static final String DEPARTMENT_NAME_REGEX = "^[a-zA-Z\\s]+$";
    private static final int MAX_DEPARTMENT_NAME_LENGTH = 50;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    public ObjectNode validateDepartment(
            String departmentName,
            Integer departmentId
    ) {
        ObjectNode validationErrors = objectMapper.createObjectNode();
        validateDepartment(departmentName, departmentId, validationErrors);
        return validationErrors;
    }

    public void validateDepartment(String departmentName, Integer departmentId, ObjectNode validationErrors) {
        if (departmentName == null || departmentName.trim().isEmpty()) {
            validationErrors.put("departmentName", "Department name is required.");
        } else {
            if (!Pattern.matches(DEPARTMENT_NAME_REGEX, departmentName)) {
                validationErrors.put("departmentName", "Department name should only contain letters and spaces.");
            }

            if (departmentName.length() > MAX_DEPARTMENT_NAME_LENGTH) {
                validationErrors.put("departmentName", "Department name should not exceed " + MAX_DEPARTMENT_NAME_LENGTH + " characters.");
            }

            try {
                boolean isDepartmentExists = departmentDAO.isDepartmentExists(departmentName);

                if (isDepartmentExists && (departmentId == null || !departmentDAO.isDepartmentBelongsToId(departmentName, departmentId))) {
                    validationErrors.put("departmentName", "Department name already exists.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                validationErrors.put("departmentName", "Failed to check department uniqueness.");
            }
        }
    }
}
