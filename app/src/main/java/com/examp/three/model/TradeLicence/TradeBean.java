package com.examp.three.model.TradeLicence;

public class TradeBean {
   String mCode;
   String mDescriptionT;
   String mDescriptionE;
   String mTradeRate;

    public TradeBean(String mDescriptionT, String mDescriptionE, String mTradeRate,String mCode) {
        this.mDescriptionT = mDescriptionT;
        this.mDescriptionE = mDescriptionE;
        this.mTradeRate = mTradeRate;
        this.mCode = mCode;
    }

    public String getmDescriptionT() {
        return mDescriptionT;
    }

    public void setmDescriptionT(String mDescriptionT) {
        this.mDescriptionT = mDescriptionT;
    }

    public String getmDescriptionE() {
        return mDescriptionE;
    }

    public void setmDescriptionE(String mDescriptionE) {
        this.mDescriptionE = mDescriptionE;
    }

    public String getmTradeRate() {
        return mTradeRate;
    }

    public void setmTradeRate(String mTradeRate) {
        this.mTradeRate = mTradeRate;
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }
}
