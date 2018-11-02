package com.examp.three.model.SharedBean;

public class TaxBalancePayment {

    public String  FinancialYear,ReceiptDate,PaymentMode  ;

    public String  TaxNo,ReceiptNo ,TaxPaid ,TaxType;

    public String getFinancialYear() {
        return FinancialYear;
    }

    public TaxBalancePayment(String financialYear, String receiptDate, String paymentMode, String taxNo, String receiptNo, String taxPaid, String taxType) {
        FinancialYear = financialYear;
        ReceiptDate = receiptDate;
        PaymentMode = paymentMode;
        TaxNo = taxNo;
        ReceiptNo = receiptNo;
        TaxPaid = taxPaid;
        TaxType = taxType;
    }

    public void setFinancialYear(String financialYear) {
        FinancialYear = financialYear;
    }

    public String getReceiptDate() {
        return ReceiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        ReceiptDate = receiptDate;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getTaxNo() {
        return TaxNo;
    }

    public void setTaxNo(String taxNo) {
        TaxNo = taxNo;
    }

    public String getReceiptNo() {
        return ReceiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        ReceiptNo = receiptNo;
    }

    public String getTaxPaid() {
        return TaxPaid;
    }

    public void setTaxPaid(String taxPaid) {
        TaxPaid = taxPaid;
    }

    public String getTaxType() {
        return TaxType;
    }

    public void setTaxType(String taxType) {
        TaxType = taxType;
    }
}
