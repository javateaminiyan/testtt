package com.examp.three.model.TradeLicence;

public class TL_Incomplete_Pojo {

    String name, mobileNo, emailID,entryDate,district,panchayat;

    int sno;


    public TL_Incomplete_Pojo(String name, String mobileNo, String emailID, String entryDate, String district, String panchayat, int sno) {
        this.name = name;
        this.mobileNo = mobileNo;
        this.emailID = emailID;
        this.entryDate = entryDate;
        this.district = district;
        this.panchayat = panchayat;
        this.sno = sno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getDistrict() {
        return district;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
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
}
