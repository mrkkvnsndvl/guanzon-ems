package com.project.guanzonems.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.guanzonems.dao.EmployeeDAO;
import java.sql.SQLException;

import java.util.regex.Pattern;

public class EmployeeValidator {

    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PHONE_NUMBER_REGEX = "^[0-9]{10,15}$";
    private static final String FULL_NAME_REGEX = "^[a-zA-Z\\s]+$";
    private static final String POSITION_REGEX = "^[a-zA-Z\\s]+$";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public ObjectNode validateEmployee(
            String fullName,
            String age,
            String email,
            String phoneNumber,
            String position,
            String department,
            String dateOfJoining,
            String salary,
            Integer employeeId
    ) {
        ObjectNode validationErrors = objectMapper.createObjectNode();
        validateFullName(fullName, validationErrors);
        validateAge(age, validationErrors);
        validateEmail(email, validationErrors, employeeId);
        validatePhoneNumber(phoneNumber, validationErrors);
        validatePosition(position, validationErrors);
        validateDepartment(department, validationErrors);
        validateDateOfJoining(dateOfJoining, validationErrors);
        validateSalary(salary, validationErrors);
        return validationErrors;
    }

    private void validateFullName(String fullName, ObjectNode validationErrors) {
        if (fullName == null || fullName.trim().isEmpty()) {
            validationErrors.put("lsFullNameValidatorText", "Full name is required.");
        } else if (!Pattern.matches(FULL_NAME_REGEX, fullName)) {
            validationErrors.put("lsFullNameValidatorText", "Full name should only contain letters and spaces.");
        }
    }

    private void validateAge(String age, ObjectNode validationErrors) {
        if (age == null || age.trim().isEmpty()) {
            validationErrors.put("lnAgeValidatorText", "Age is required.");
        } else {
            try {
                int ageValue = Integer.parseInt(age);
                if (ageValue < 18 || ageValue > 60) {
                    validationErrors.put("lnAgeValidatorText", "Age must be between 18 and 60.");
                }
            } catch (NumberFormatException e) {
                validationErrors.put("lnAgeValidatorText", "Age must be a valid number.");
            }
        }
    }

    private void validateEmail(String email, ObjectNode validationErrors, Integer employeeId) {
        if (email == null || email.trim().isEmpty()) {
            validationErrors.put("lsEmailValidatorText", "Email is required.");
        } else if (!Pattern.matches(EMAIL_REGEX, email)) {
            validationErrors.put("lsEmailValidatorText", "Invalid email format.");
        } else {
            try {
                boolean isEmailExists = employeeDAO.isEmailExists(email);

                if (isEmailExists && (employeeId == null || !employeeDAO.isEmailBelongsToEmployee(email, employeeId))) {
                    validationErrors.put("lsEmailValidatorText", "Email already in use.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                validationErrors.put("lsEmailValidatorText", "Failed to check email uniqueness.");
            }
        }
    }

    private void validatePhoneNumber(String phoneNumber, ObjectNode validationErrors) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            validationErrors.put("lsPhoneNumberValidatorText", "Phone number is required.");
        } else if (!Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber)) {
            validationErrors.put("lsPhoneNumberValidatorText", "Phone number must be 10-15 digits.");
        }
    }

    private void validatePosition(String position, ObjectNode validationErrors) {
        if (position == null || position.trim().isEmpty()) {
            validationErrors.put("lsPositionValidatorText", "Position is required.");
        } else if (!Pattern.matches(POSITION_REGEX, position)) {
            validationErrors.put("lsPositionValidatorText", "Position should only contain letters and spaces.");
        }
    }

    private void validateDepartment(String department, ObjectNode validationErrors) {
        if (department == null || department.trim().isEmpty()) {
            validationErrors.put("lsDepartmentValidatorText", "Department is required.");
        }
    }

    private void validateDateOfJoining(String dateOfJoining, ObjectNode validationErrors) {
        if (dateOfJoining == null || dateOfJoining.trim().isEmpty()) {
            validationErrors.put("ldDateOfJoiningValidatorText", "Date of joining is required.");
        }
    }

    private void validateSalary(String salary, ObjectNode validationErrors) {
        if (salary == null || salary.trim().isEmpty()) {
            validationErrors.put("ldSalaryValidatorText", "Salary is required.");
        } else {
            try {
                double salaryValue = Double.parseDouble(salary);
                if (salaryValue <= 0) {
                    validationErrors.put("ldSalaryValidatorText", "Salary must be greater than zero.");
                }
            } catch (NumberFormatException e) {
                validationErrors.put("ldSalaryValidatorText", "Salary must be a valid number.");
            }
        }
    }
}
