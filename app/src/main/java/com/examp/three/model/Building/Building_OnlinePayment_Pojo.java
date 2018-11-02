package com.examp.three.model.Building;

public class Building_OnlinePayment_Pojo {
    String ApplicationNo,ApplicationDate,ApplicantName,ApplicationName,OBPADate,LicenceFees,DevelopmentCharge,VacantLandTax,TotalBL,Userid;

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public Building_OnlinePayment_Pojo(String applicationNo, String applicationDate, String applicantName, String applicationName, String OBPADate, String licenceFees, String developmentCharge, String vacantLandTax, String totalBL, String userid) {

        ApplicationNo = applicationNo;
        ApplicationDate = applicationDate;
        ApplicantName = applicantName;
        ApplicationName = applicationName;
        this.OBPADate = OBPADate;
        LicenceFees = licenceFees;
        DevelopmentCharge = developmentCharge;
        VacantLandTax = vacantLandTax;
        TotalBL = totalBL;
        Userid = userid;
    }

    public String getApplicationNo() {
        return ApplicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        ApplicationNo = applicationNo;
    }

    public String getApplicationDate() {
        return ApplicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        ApplicationDate = applicationDate;
    }

    public String getApplicantName() {
        return ApplicantName;
    }

    public void setApplicantName(String applicantName) {
        ApplicantName = applicantName;
    }

    public String getApplicationName() {
        return ApplicationName;
    }

    public void setApplicationName(String applicationName) {
        ApplicationName = applicationName;
    }

    public String getOBPADate() {
        return OBPADate;
    }

    public void setOBPADate(String OBPADate) {
        this.OBPADate = OBPADate;
    }

    public String getLicenceFees() {
        return LicenceFees;
    }

    public void setLicenceFees(String licenceFees) {
        LicenceFees = licenceFees;
    }

    public String getDevelopmentCharge() {
        return DevelopmentCharge;
    }

    public void setDevelopmentCharge(String developmentCharge) {
        DevelopmentCharge = developmentCharge;
    }

    public String getVacantLandTax() {
        return VacantLandTax;
    }

    public void setVacantLandTax(String vacantLandTax) {
        VacantLandTax = vacantLandTax;
    }

    public String getTotalBL() {
        return TotalBL;
    }

    public void setTotalBL(String totalBL) {
        TotalBL = totalBL;
    }
}
