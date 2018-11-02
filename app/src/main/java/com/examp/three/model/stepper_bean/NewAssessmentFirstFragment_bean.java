package com.examp.three.model.stepper_bean;

public class NewAssessmentFirstFragment_bean {
    String district;
    String panchayat;
    String name;
    String mobileno;
    String emailid;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPanchayat() {
        return panchayat;
    }

    public void setPanchayat(String panchayat) {
        this.panchayat = panchayat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public NewAssessmentFirstFragment_bean(String district, String panchayat, String name, String mobileno, String emailid) {
        this.district = district;
        this.panchayat = panchayat;
        this.name = name;
        this.mobileno = mobileno;
        this.emailid = emailid;
    }
}
