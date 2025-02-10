package com.project.guanzonems.model;

public class Department {

    private int lnId;
    private String lsDepartment;

    public Department() {
    }

    public Department(int lnId, String lsDepartment) {
        this.lnId = lnId;
        this.lsDepartment = lsDepartment;
    }

    public int getLnId() {
        return lnId;
    }

    public void setLnId(int lnId) {
        this.lnId = lnId;
    }

    public String getLsDepartment() {
        return lsDepartment;
    }

    public void setLsDepartment(String lsDepartment) {
        this.lsDepartment = lsDepartment;
    }

    @Override
    public String toString() {
        return "Department{"
                + "lnId=" + lnId
                + ", lsDepartment='" + lsDepartment + '\''
                + '}';
    }
}
