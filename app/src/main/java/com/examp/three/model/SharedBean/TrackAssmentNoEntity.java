package com.examp.three.model.SharedBean;

public class TrackAssmentNoEntity {
    private String reqNo;
    private String reqDate;
    private String district;
    private String panchayat;
    private String name;
    private String blNo;
    private String blockNo;
    private String wardNo;
    private String streetName;
    private String status;
    private String mobileNo;
    private String emailId;
    private String tax_type;

    private String prof_TradeName;
    private String prof_OrganizationCode;
    private String prof_OrganizationName;
    private String prof_DesignationName;

    public String getProf_TradeName() {
        return prof_TradeName;
    }

    public void setProf_TradeName(String prof_TradeName) {
        this.prof_TradeName = prof_TradeName;
    }

    public String getProf_OrganizationCode() {
        return prof_OrganizationCode;
    }

    public void setProf_OrganizationCode(String prof_OrganizationCode) {
        this.prof_OrganizationCode = prof_OrganizationCode;
    }

    public String getProf_OrganizationName() {
        return prof_OrganizationName;
    }

    public void setProf_OrganizationName(String prof_OrganizationName) {
        this.prof_OrganizationName = prof_OrganizationName;
    }

    public String getProf_DesignationName() {
        return prof_DesignationName;
    }

    public void setProf_DesignationName(String prof_DesignationName) {
        this.prof_DesignationName = prof_DesignationName;
    }

    public TrackAssmentNoEntity(String reqNo, String reqDate, String district, String panchayat, String name, String blNo, String blockNo, String wardNo, String streetName, String status, String mobileNo, String emailId, String tax_type, String prof_TradeName, String prof_OrganizationCode, String prof_OrganizationName, String prof_DesignationName) {
        this.reqNo = reqNo;
        this.reqDate = reqDate;
        this.district = district;
        this.panchayat = panchayat;
        this.name = name;
        this.blNo = blNo;
        this.blockNo = blockNo;
        this.wardNo = wardNo;
        this.streetName = streetName;
        this.status = status;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
        this.tax_type = tax_type;
        this.prof_TradeName = prof_TradeName;
        this.prof_OrganizationCode = prof_OrganizationCode;
        this.prof_OrganizationName = prof_OrganizationName;
        this.prof_DesignationName = prof_DesignationName;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public TrackAssmentNoEntity(String reqNo, String reqDate, String district, String panchayat, String name, String blNo, String blockNo, String wardNo, String streetName, String status, String mobileNo, String emailId, String tax_type) {
        this.reqNo = reqNo;
        this.reqDate = reqDate;
        this.district = district;
        this.panchayat = panchayat;
        this.name = name;
        this.blNo = blNo;
        this.blockNo = blockNo;
        this.wardNo = wardNo;
        this.streetName = streetName;
        this.status = status;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
        this.tax_type = tax_type;
    }

    public String getTax_type() {
        return tax_type;
    }

    public void setTax_type(String tax_type) {
        this.tax_type = tax_type;
    }
}
