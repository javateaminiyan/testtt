package com.examp.three.model.TradeLicence;

public class ViewReceiptHistory_Pojo {

    String status,referenceNo,applicationDate,finYear,
            licenseValidity,licenseNo,applicantName,establishmentName,
            district,panchayat,userid,tradersCode;





    public ViewReceiptHistory_Pojo(String status, String referenceNo, String applicationDate, String finYear, String licenseValidity, String licenseNo, String applicantName, String establishmentName, String district, String panchayat, String userid, String tradersCode) {
        this.status = status;
        this.referenceNo = referenceNo;
        this.applicationDate = applicationDate;
        this.finYear = finYear;
        this.licenseValidity = licenseValidity;
        this.licenseNo = licenseNo;
        this.applicantName = applicantName;
        this.establishmentName = establishmentName;
        this.district = district;
        this.panchayat = panchayat;
        this.userid = userid;
        this.tradersCode = tradersCode;
    }

    public String getTradersCode() {
        return tradersCode;
    }

    public void setTradersCode(String tradersCode) {
        this.tradersCode = tradersCode;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getFinYear() {
        return finYear;
    }

    public void setFinYear(String finYear) {
        this.finYear = finYear;
    }

    public String getLicenseValidity() {
        return licenseValidity;
    }

    public void setLicenseValidity(String licenseValidity) {
        this.licenseValidity = licenseValidity;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public void setEstablishmentName(String establishmentName) {
        this.establishmentName = establishmentName;
    }


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
}
