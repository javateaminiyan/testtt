package com.examp.three.model.TradeLicence;

public class TradeNames {

    String tradeCode,description,tradeRate;

    public TradeNames(String tradeCode, String description,String tradeRate) {
        this.tradeCode = tradeCode;
        this.description = description;
        this.tradeRate = tradeRate;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTradeRate() {
        return tradeRate;
    }

    public void setTradeRate(String tradeRate) {
        this.tradeRate = tradeRate;
    }
}

