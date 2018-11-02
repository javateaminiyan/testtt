package com.examp.three.model.Building;

public class SurveyorBean {
    String mSurveyorId;
    String mSurveyorName;

    public SurveyorBean(String mSurveyorId, String mSurveyorName) {
        this.mSurveyorId = mSurveyorId;
        this.mSurveyorName = mSurveyorName;
    }

    public String getmSurveyorId() {
        return mSurveyorId;
    }

    public void setmSurveyorId(String mSurveyorId) {
        this.mSurveyorId = mSurveyorId;
    }

    public String getmSurveyorName() {
        return mSurveyorName;
    }

    public void setmSurveyorName(String mSurveyorName) {
        this.mSurveyorName = mSurveyorName;
    }
}
