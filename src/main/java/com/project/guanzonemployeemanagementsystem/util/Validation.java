package com.project.guanzonemployeemanagementsystem.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Validation {

    private static final Gson gson = new Gson();

    // Method to create an error message JSON
    public static String createErrorMessage(String message) {
        JsonObject json = new JsonObject();
        json.addProperty("status", "error");
        json.addProperty("message", message);
        return gson.toJson(json);
    }

    // Method to create a success message JSON
    public static String createSuccessMessage(String message) {
        JsonObject json = new JsonObject();
        json.addProperty("status", "success");
        json.addProperty("message", message);
        return gson.toJson(json);
    }
}
