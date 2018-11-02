package com.examp.three.model.SharedBean;

/**
 * Created by admin on 23-07-2015.
 */
public class GetDemandEntity {

    private String financialYear;
    private String term;
    private String balance;
    private String sNo;
    private String cessAmount;
    private String maintenanceCharge;
    private String waterCharges;
    private String penaltyAmount;
    private String swaAmount;
    private String propertyTax;
    private String swmSno;
    private String swmMonth;

    public String getSwmSno() {
        return swmSno;
    }

    @Override
    public String toString() {
        return "GetDemandEntity{" +
                "financialYear='" + financialYear + '\'' +
                ", term='" + term + '\'' +
                ", balance='" + balance + '\'' +
                ", sNo='" + sNo + '\'' +
                ", cessAmount='" + cessAmount + '\'' +
                ", maintenanceCharge='" + maintenanceCharge + '\'' +
                ", waterCharges='" + waterCharges + '\'' +
                ", penaltyAmount='" + penaltyAmount + '\'' +
                ", swaAmount='" + swaAmount + '\'' +
                ", propertyTax='" + propertyTax + '\'' +
                ", swmSno='" + swmSno + '\'' +
                ", swmMonth='" + swmMonth + '\'' +
                '}';
    }

    public void setSwmSno(String swmSno) {
        this.swmSno = swmSno;
    }

    public String getSwmMonth() {
        return swmMonth;
    }

    public void setSwmMonth(String swmMonth) {
        this.swmMonth = swmMonth;
    }

    public String getSwaAmount() {
        return swaAmount;
    }

    public void setSwaAmount(String swaAmount) {
        this.swaAmount = swaAmount;
    }

    public String getPropertyTax() {
        return propertyTax;
    }

    public void setPropertyTax(String propertyTax) {
        this.propertyTax = propertyTax;
    }

    public GetDemandEntity() {
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getCessAmount() {
        return cessAmount;
    }

    public void setCessAmount(String cessAmount) {
        this.cessAmount = cessAmount;
    }

    public String getMaintenanceCharge() {
        return maintenanceCharge;
    }

    public void setMaintenanceCharge(String maintenanceCharge) {
        this.maintenanceCharge = maintenanceCharge;
    }

    public String getWaterCharges() {
        return waterCharges;
    }

    public void setWaterCharges(String waterCharges) {
        this.waterCharges = waterCharges;
    }

    public String getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(String penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

}
