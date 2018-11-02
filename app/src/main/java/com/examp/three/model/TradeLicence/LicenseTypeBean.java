package com.examp.three.model.TradeLicence;

public class LicenseTypeBean {
    int mLicenceTypeId;
    String mLicenceTypeNameEnglish;

    public LicenseTypeBean(int mLicenceTypeId, String mLicenceTypeNameEnglish) {
        this.mLicenceTypeId = mLicenceTypeId;
        this.mLicenceTypeNameEnglish = mLicenceTypeNameEnglish;
    }

    public int getmLicenceTypeId() {
        return mLicenceTypeId;
    }

    public void setmLicenceTypeId(int mLicenceTypeId) {
        this.mLicenceTypeId = mLicenceTypeId;
    }

    public String getmLicenceTypeNameEnglish() {
        return mLicenceTypeNameEnglish;
    }

    public void setmLicenceTypeNameEnglish(String mLicenceTypeNameEnglish) {
        this.mLicenceTypeNameEnglish = mLicenceTypeNameEnglish;
    }
}
