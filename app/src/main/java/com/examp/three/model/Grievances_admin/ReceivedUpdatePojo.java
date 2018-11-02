package com.examp.three.model.Grievances_admin;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReceivedUpdatePojo{

    @SerializedName("ComplaintNo")
    @Expose
    private Integer complaintNo;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("UpdatedId")
    @Expose
    private String updatedId;
    @SerializedName("UpdatedBy")
    @Expose
    private String updatedBy;
    @SerializedName("Attender")
    @Expose
    private String attender;

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

    public String getUpdatedId() {
        return updatedId;
    }

    public void setUpdatedId(String updatedId) {
        this.updatedId = updatedId;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getAttender() {
        return attender;
    }

    public void setAttender(String attender) {
        this.attender = attender;
    }

}