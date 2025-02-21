package com.project.guanzonems.model;

import java.time.LocalDate;

public class Employee {

    private int lnId;
    private String lsFullName;
    private int lnAge;
    private String lsEmail;
    private String lsPhoneNumber;
    private String lsPosition;
    private String lsDepartment;
    private LocalDate ldDateOfJoining;
    private double ldSalary;

    public Employee() {
    }

    public Employee(int lnId, String lsFullName, int lnAge, String lsEmail, String lsPhoneNumber, String lsPosition, String lsDepartment, LocalDate ldDateOfJoining, double ldSalary) {
        this.lnId = lnId;
        this.lsFullName = lsFullName;
        this.lnAge = lnAge;
        this.lsEmail = lsEmail;
        this.lsPhoneNumber = lsPhoneNumber;
        this.lsPosition = lsPosition;
        this.lsDepartment = lsDepartment;
        this.ldDateOfJoining = ldDateOfJoining;
        this.ldSalary = ldSalary;
    }

    public int getLnId() {
        return lnId;
    }

    public void setLnId(int lnId) {
        this.lnId = lnId;
    }

    public String getLsFullName() {
        return lsFullName;
    }

    public void setLsFullName(String lsFullName) {
        this.lsFullName = lsFullName;
    }

    public int getLnAge() {
        return lnAge;
    }

    public void setLnAge(int lnAge) {
        this.lnAge = lnAge;
    }

    public String getLsEmail() {
        return lsEmail;
    }

    public void setLsEmail(String lsEmail) {
        this.lsEmail = lsEmail;
    }

    public String getLsPhoneNumber() {
        return lsPhoneNumber;
    }

    public void setLsPhoneNumber(String lsPhoneNumber) {
        this.lsPhoneNumber = lsPhoneNumber;
    }

    public String getLsPosition() {
        return lsPosition;
    }

    public void setLsPosition(String lsPosition) {
        this.lsPosition = lsPosition;
    }

    public String getLsDepartment() {
        return lsDepartment;
    }

    public void setLsDepartment(String lsDepartment) {
        this.lsDepartment = lsDepartment;
    }

    public LocalDate getLdDateOfJoining() {
        return ldDateOfJoining;
    }

    public void setLdDateOfJoining(LocalDate ldDateOfJoining) {
        this.ldDateOfJoining = ldDateOfJoining;
    }

    public double getLdSalary() {
        return ldSalary;
    }

    public void setLdSalary(double ldSalary) {
        this.ldSalary = ldSalary;
    }

    @Override
    public String toString() {
        return "Employee{"
                + "lnId=" + lnId
                + ", lsFullName='" + lsFullName + '\''
                + ", lnAge=" + lnAge
                + ", lsEmail='" + lsEmail + '\''
                + ", lsPhoneNumber='" + lsPhoneNumber + '\''
                + ", lsPosition='" + lsPosition + '\''
                + ", lsDepartment='" + lsDepartment + '\''
                + ", ldDateOfJoining=" + ldDateOfJoining
                + ", ldSalary=" + ldSalary
                + '}';
    }
}