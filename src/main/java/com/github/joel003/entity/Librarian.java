package com.github.joel003.entity;

public class Librarian extends Staff{

    private String officeId;

    public void setOfficeId(String officeNo) {
        this.officeId = officeNo;
    }
    public String getOfficeId() {
        return officeId;
    }
}
