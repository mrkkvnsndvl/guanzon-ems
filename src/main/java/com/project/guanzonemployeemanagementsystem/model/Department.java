package com.project.guanzonemployeemanagementsystem.model;

public class Department {

    private int lnId;
    private String lsDepartmentName;

    // Default Constructor
    public Department() {
    }

    // Parameterized Constructor
    public Department(int lnId, String lsDepartmentName) {
        this.lnId = lnId;
        this.lsDepartmentName = lsDepartmentName;
    }

    // Getters and Setters
    public int getLnId() {
        return lnId;
    }

    public void setLnId(int lnId) {
        this.lnId = lnId;
    }

    public String getLsDepartmentName() {
        return lsDepartmentName;
    }

    public void setLsDepartmentName(String lsDepartmentName) {
        this.lsDepartmentName = lsDepartmentName;
    }

    // toString Method (Optional, for debugging or logging)
    @Override
    public String toString() {
        return "Department{"
                + "lnId=" + lnId
                + ", lsDepartmentName='" + lsDepartmentName + '\''
                + '}';
    }
}
