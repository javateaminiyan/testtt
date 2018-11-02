package com.examp.three.model.propertytax;

public class PaymenyHistoryBean {
    private String mUserId,mDistrict,mPanchayat,mTaxNo,mReceiptNo,mCollectionDate,mAmount,mMobileNo,mEmailId,mOrderNo,
            mAssessmentName,taxType;

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmDistrict() {
        return mDistrict;
    }

    public void setmDistrict(String mDistrict) {
        this.mDistrict = mDistrict;
    }

    public String getmPanchayat() {
        return mPanchayat;
    }

    public void setmPanchayat(String mPanchayat) {
        this.mPanchayat = mPanchayat;
    }

    public String getmTaxNo() {
        return mTaxNo;
    }

    public void setmTaxNo(String mTaxNo) {
        this.mTaxNo = mTaxNo;
    }

    public String getmReceiptNo() {
        return mReceiptNo;
    }

    public void setmReceiptNo(String mReceiptNo) {
        this.mReceiptNo = mReceiptNo;
    }

    public String getmCollectionDate() {
        return mCollectionDate;
    }

    public void setmCollectionDate(String mCollectionDate) {
        this.mCollectionDate = mCollectionDate;
    }

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    public String getmMobileNo() {
        return mMobileNo;
    }

    public void setmMobileNo(String mMobileNo) {
        this.mMobileNo = mMobileNo;
    }

    public String getmEmailId() {
        return mEmailId;
    }

    public void setmEmailId(String mEmailId) {
        this.mEmailId = mEmailId;
    }

    public String getmOrderNo() {
        return mOrderNo;
    }

    public void setmOrderNo(String mOrderNo) {
        this.mOrderNo = mOrderNo;
    }

    public String getmAssessmentName() {
        return mAssessmentName;
    }

    public void setmAssessmentName(String mAssessmentName) {
        this.mAssessmentName = mAssessmentName;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public PaymenyHistoryBean(String userId, String district, String panchayat, String taxNo, String receiptNo,
                              String collectionDate, String amount, String mobileNo, String emailId, String orderNo,
                              String assessmentName, String taxType) {
        this.mUserId=userId;
        this.mDistrict=district;
        this.mPanchayat=panchayat;
        this.mTaxNo=taxNo;
        this.mReceiptNo=receiptNo;
        this.mCollectionDate=collectionDate;
        this.mAmount=amount;
        this.mMobileNo=mobileNo;
        this.mEmailId=emailId;
        this.mOrderNo=orderNo;
        this.mAssessmentName=assessmentName;
        this.taxType=taxType;


    }
}
