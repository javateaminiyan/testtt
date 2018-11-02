package com.examp.three.model.Grievances_admin;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GrievanceData {

    @SerializedName("Sno")
    @Expose
    private Integer sno;
    @SerializedName("ComplaintNo")
    @Expose
    private Integer complaintNo;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("ComplainerName")
    @Expose
    private String complainerName;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("EmailId")
    @Expose
    private String emailId;
    @SerializedName("WardNo")
    @Expose
    private String wardNo;
    @SerializedName("StreetName")
    @Expose
    private String streetName;
    @SerializedName("Priority")
    @Expose
    private String priority;
    @SerializedName("ComplaintType")
    @Expose
    private String complaintType;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("Status")
    @Expose
    private String status;

    public Integer getSno() {
        return sno;
    }

    public void setSno(Integer sno) {
        this.sno = sno;
    }

    public Integer getComplaintNo() {
        return complaintNo;
    }

    public void setComplaintNo(Integer complaintNo) {
        this.complaintNo = complaintNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComplainerName() {
        return complainerName;
    }

    public void setComplainerName(String complainerName) {
        this.complainerName = complainerName;
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

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}