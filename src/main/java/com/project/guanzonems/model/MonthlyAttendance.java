package com.project.guanzonems.model;

public class MonthlyAttendance {

    private int lnId;
    private String lsFullName; 
    private String lsDepartment;
    private int lnPresentDays; 
    private int lnLateDays;
    private int lnAbsentDays;
    private int lnOnLeaveDays;
    private double ldAttendancePercentage;

    public MonthlyAttendance(int lnId, String lsFullName, String lsDepartment, int lnPresentDays, int lnLateDays,
            int lnAbsentDays, int lnOnLeaveDays, double ldAttendancePercentage) {
        this.lnId = lnId;
        this.lsFullName = lsFullName;
        this.lsDepartment = lsDepartment;
        this.lnPresentDays = lnPresentDays;
        this.lnLateDays = lnLateDays;
        this.lnAbsentDays = lnAbsentDays;
        this.lnOnLeaveDays = lnOnLeaveDays;
        this.ldAttendancePercentage = ldAttendancePercentage;
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

    public String getLsDepartment() {
        return lsDepartment;
    }

    public void setLsDepartment(String lsDepartment) {
        this.lsDepartment = lsDepartment;
    }

    public int getLnPresentDays() {
        return lnPresentDays;
    }

    public void setLnPresentDays(int lnPresentDays) {
        this.lnPresentDays = lnPresentDays;
    }

    public int getLnLateDays() {
        return lnLateDays;
    }

    public void setLnLateDays(int lnLateDays) {
        this.lnLateDays = lnLateDays;
    }

    public int getLnAbsentDays() {
        return lnAbsentDays;
    }

    public void setLnAbsentDays(int lnAbsentDays) {
        this.lnAbsentDays = lnAbsentDays;
    }

    public int getLnOnLeaveDays() {
        return lnOnLeaveDays;
    }

    public void setLnOnLeaveDays(int lnOnLeaveDays) {
        this.lnOnLeaveDays = lnOnLeaveDays;
    }

    public double getLdAttendancePercentage() {
        return ldAttendancePercentage;
    }

    public void setLdAttendancePercentage(double ldAttendancePercentage) {
        this.ldAttendancePercentage = ldAttendancePercentage;
    }
}
