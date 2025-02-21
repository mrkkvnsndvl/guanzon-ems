package com.project.guanzonems.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class DailyAttendance {

    private int lnId;
    private String lsFullName;
    private String lsDepartment;
    private String lsStatus;
    private LocalTime ltTimeIn;
    private LocalTime ltTimeOut;
    private LocalDate ldDate;

    public DailyAttendance(int lnId, String lsFullName, String lsDepartment, String lsStatus, LocalTime ltTimeIn, LocalTime ltTimeOut, LocalDate ldDate) {
        this.lnId = lnId;
        this.lsFullName = lsFullName;
        this.lsDepartment = lsDepartment;
        this.lsStatus = lsStatus;
        this.ltTimeIn = ltTimeIn;
        this.ltTimeOut = ltTimeOut;
        this.ldDate = ldDate != null ? ldDate : LocalDate.now();
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

    public String getLsStatus() {
        return lsStatus;
    }

    public void setLsStatus(String lsStatus) {
        this.lsStatus = lsStatus;
    }

    public LocalTime getLtTimeIn() {
        return ltTimeIn;
    }

    public void setLtTimeIn(LocalTime ltTimeIn) {
        this.ltTimeIn = ltTimeIn;
    }

    public LocalTime getLtTimeOut() {
        return ltTimeOut;
    }

    public void setLtTimeOut(LocalTime ltTimeOut) {
        this.ltTimeOut = ltTimeOut;
    }

    public LocalDate getLdDate() {
        return ldDate;
    }

    public void setLdDate(LocalDate ldDate) {
        this.ldDate = ldDate;
    }
}
