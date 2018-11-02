package com.examp.three.model.BuildingPlan;

public class BP_RequestStatus_Pojo {

    private String applicationNo,date, name, fatherName,mobileNo,emailId,plot_area_sqft,buildingType,status;

    public BP_RequestStatus_Pojo(String applicationNo, String date, String name, String fatherName, String mobileNo, String emailId, String plot_area_sqft, String buildingType, String status) {
        this.applicationNo = applicationNo;
        this.date = date;
        this.name = name;
        this.fatherName = fatherName;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
        this.plot_area_sqft = plot_area_sqft;
        this.buildingType = buildingType;
        this.status = status;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPlot_area_sqft() {
        return plot_area_sqft;
    }

    public void setPlot_area_sqft(String plot_area_sqft) {
        this.plot_area_sqft = plot_area_sqft;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
