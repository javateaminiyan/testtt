package com.examp.three.model.Building;

public class BLCompletedRequestBean {


  private   String applicationNo, applicationDate,applicantName,obpaDate,licenceFee,delopmentCharge,
            vacantLandTax,totalbl;

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getObpaDate() {
        return obpaDate;
    }

    public void setObpaDate(String obpaDate) {
        this.obpaDate = obpaDate;
    }

    public String getLicenceFee() {
        return licenceFee;
    }

    public void setLicenceFee(String licenceFee) {
        this.licenceFee = licenceFee;
    }

    public String getDelopmentCharge() {
        return delopmentCharge;
    }

    public void setDelopmentCharge(String delopmentCharge) {
        this.delopmentCharge = delopmentCharge;
    }

    public String getVacantLandTax() {
        return vacantLandTax;
    }

    public void setVacantLandTax(String vacantLandTax) {
        this.vacantLandTax = vacantLandTax;
    }

    public String getTotalbl() {
        return totalbl;
    }

    public void setTotalbl(String totalbl) {
        this.totalbl = totalbl;
    }

    public BLCompletedRequestBean(String applicationNo, String applicationDate, String applicantName, String obpaDate, String licenceFee, String delopmentCharge, String vacantLandTax, String totalbl) {
        this.applicationNo = applicationNo;
        this.applicationDate = applicationDate;
        this.applicantName = applicantName;
        this.obpaDate = obpaDate;
        this.licenceFee = licenceFee;
        this.delopmentCharge = delopmentCharge;
        this.vacantLandTax = vacantLandTax;
        this.totalbl = totalbl;
    }
}
