package com.examp.three.model.TradeLicence;

public class TL_ViewToComp_Pojo {


    private String querytype,sMobileNo,sEmail,sApplicantSurName,sApplicantFirstName,sApGender,sApFGSurName,ApFGFirstName,ApDoorNo,
            ApStreet,ApCity,ApDistrict,ApPIN,Aadhar,sDistrict,sPanchayat,sWardNo,sStreetName,sDoorNo,LicenceYear,
            LicenceTypeId,  TradeDescription ,EstablishmentName,motorInstalled,
            RentalAgrStatus,RentPaid,NOCStatus,ApplicantPhoto,AddressProofCopy,AgreementCopy,NOCCopy,
            MachineSpecifications,MachineInstallationDig,gst;

    private int Sno,motorTotalQty,HorsePowerTotal,TradeRate;
    public TL_ViewToComp_Pojo(String querytype,int Sno,String sMobileNo, String sEmail, String sApplicantSurName,
                              String sApplicantFirstName, String sApGender,
                              String sApFGSurName, String apFGFirstName,
                              String apDoorNo, String apStreet, String apCity,
                              String apDistrict, String apPIN, String aadhar,String gst,
                              String sDistrict,String sPanchayat,String sWardNo,String sStreetName,String sDoorNo,
                              String LicenceYear,String LicenceTypeId, String TradeDescription,int TradeRate,String EstablishmentName,
                              String motorInstalled,int motorTotalQty,int HorsePowerTotal,
                              String RentalAgrStatus,String RentPaid,String NOCStatus,
                              String ApplicantPhoto,String AddressProofCopy,
                              String AgreementCopy,String NOCCopy,
                              String MachineSpecifications,String MachineInstallationDig

                              ) {
        this.querytype = querytype;
        this.Sno = Sno;
        this.sMobileNo = sMobileNo;
        this.sEmail = sEmail;
        this.sApplicantSurName = sApplicantSurName;
        this.sApplicantFirstName = sApplicantFirstName;
        this.sApGender = sApGender;
        this.sApFGSurName = sApFGSurName;
        this.ApFGFirstName = apFGFirstName;
        this.ApDoorNo = apDoorNo;
        this.ApStreet = apStreet;
        this.ApCity = apCity;
        this. ApDistrict = apDistrict;
        this.ApPIN = apPIN;
        this.Aadhar = aadhar;
        this.gst = gst;

        this.sDistrict = sDistrict;
        this.sPanchayat = sPanchayat;
        this.sWardNo = sWardNo;
        this.sStreetName = sStreetName;
        this.sDoorNo = sDoorNo;
        this.LicenceYear = LicenceYear;

        this.LicenceTypeId = LicenceTypeId;
        this.TradeDescription = TradeDescription;
        this.TradeRate =TradeRate;
        this.EstablishmentName = EstablishmentName;
        this.motorInstalled = motorInstalled;
        this.motorTotalQty = motorTotalQty;
        this.HorsePowerTotal = HorsePowerTotal;
        this.RentalAgrStatus = RentalAgrStatus;
        this.RentPaid = RentPaid;
        this.NOCStatus = NOCStatus;

        this.ApplicantPhoto = ApplicantPhoto;
        this.AddressProofCopy = AddressProofCopy;
        this.AgreementCopy = AgreementCopy;
        this.NOCCopy = NOCCopy;
        this.MachineSpecifications = MachineSpecifications;
        this.MachineInstallationDig = MachineInstallationDig;

    }

    public String getsMobileNo() {
        return sMobileNo;
    }

    public int getSno() {
        return Sno;
    }

    public void setSno(int sno) {
        Sno = sno;
    }

    public String getQuerytype() {
        return querytype;
    }

    public void setQuerytype(String querytype) {
        this.querytype = querytype;
    }

    public int getTradeRate() {
        return TradeRate;
    }

    public void setTradeRate(int tradeRate) {
        TradeRate = tradeRate;
    }

    public String getGst() {
        return gst;

    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public void setsMobileNo(String sMobileNo) {
        this.sMobileNo = sMobileNo;
    }

    public String getsDistrict() {
        return sDistrict;
    }

    public void setsDistrict(String sDistrict) {
        this.sDistrict = sDistrict;
    }

    public String getsPanchayat() {
        return sPanchayat;
    }

    public void setsPanchayat(String sPanchayat) {
        this.sPanchayat = sPanchayat;
    }

    public String getsWardNo() {
        return sWardNo;
    }

    public void setsWardNo(String sWardNo) {
        this.sWardNo = sWardNo;
    }

    public String getsStreetName() {
        return sStreetName;
    }

    public void setsStreetName(String sStreetName) {
        this.sStreetName = sStreetName;
    }

    public String getsDoorNo() {
        return sDoorNo;
    }

    public void setsDoorNo(String sDoorNo) {
        this.sDoorNo = sDoorNo;
    }

    public String getLicenceYear() {
        return LicenceYear;
    }

    public void setLicenceYear(String licenceYear) {
        LicenceYear = licenceYear;
    }

    public String getsEmail() {

        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsApplicantSurName() {
        return sApplicantSurName;
    }

    public void setsApplicantSurName(String sApplicantSurName) {
        this.sApplicantSurName = sApplicantSurName;
    }

    public String getsApplicantFirstName() {
        return sApplicantFirstName;
    }

    public void setsApplicantFirstName(String sApplicantFirstName) {
        this.sApplicantFirstName = sApplicantFirstName;
    }

    public String getsApGender() {
        return sApGender;
    }

    public void setsApGender(String sApGender) {
        this.sApGender = sApGender;
    }

    public String getsApFGSurName() {
        return sApFGSurName;
    }

    public void setsApFGSurName(String sApFGSurName) {
        this.sApFGSurName = sApFGSurName;
    }

    public String getApFGFirstName() {
        return ApFGFirstName;
    }

    public void setApFGFirstName(String apFGFirstName) {
        ApFGFirstName = apFGFirstName;
    }

    public String getApDoorNo() {
        return ApDoorNo;
    }

    public void setApDoorNo(String apDoorNo) {
        ApDoorNo = apDoorNo;
    }

    public String getApStreet() {
        return ApStreet;
    }

    public void setApStreet(String apStreet) {
        ApStreet = apStreet;
    }

    public String getApCity() {
        return ApCity;
    }

    public void setApCity(String apCity) {
        ApCity = apCity;
    }

    public String getApDistrict() {
        return ApDistrict;
    }

    public void setApDistrict(String apDistrict) {
        ApDistrict = apDistrict;
    }

    public String getApPIN() {
        return ApPIN;
    }

    public void setApPIN(String apPIN) {
        ApPIN = apPIN;
    }

    public String getAadhar() {
        return Aadhar;
    }

    public void setAadhar(String aadhar) {
        Aadhar = aadhar;
    }

    public String getLicenceTypeId() {
        return LicenceTypeId;
    }

    public void setLicenceTypeId(String licenceTypeId) {
        LicenceTypeId = licenceTypeId;
    }

    public String getTradeDescription() {
        return TradeDescription;
    }

    public void setTradeDescription(String tradeDescription) {
        TradeDescription = tradeDescription;
    }

    public String getEstablishmentName() {
        return EstablishmentName;
    }

    public void setEstablishmentName(String establishmentName) {
        EstablishmentName = establishmentName;
    }

    public String getMotorInstalled() {
        return motorInstalled;
    }

    public void setMotorInstalled(String motorInstalled) {
        this.motorInstalled = motorInstalled;
    }

    public int getMotorTotalQty() {
        return motorTotalQty;
    }

    public void setMotorTotalQty(int motorTotalQty) {
        this.motorTotalQty = motorTotalQty;
    }

    public int getHorsePowerTotal() {
        return HorsePowerTotal;
    }

    public void setHorsePowerTotal(int horsePowerTotal) {
        HorsePowerTotal = horsePowerTotal;
    }

    public String getRentalAgrStatus() {
        return RentalAgrStatus;
    }

    public void setRentalAgrStatus(String rentalAgrStatus) {
        RentalAgrStatus = rentalAgrStatus;
    }

    public String getRentPaid() {
        return RentPaid;
    }

    public void setRentPaid(String rentPaid) {
        RentPaid = rentPaid;
    }

    public String getNOCStatus() {
        return NOCStatus;
    }

    public void setNOCStatus(String NOCStatus) {
        this.NOCStatus = NOCStatus;
    }

    public String getApplicantPhoto() {
        return ApplicantPhoto;
    }

    public void setApplicantPhoto(String applicantPhoto) {
        ApplicantPhoto = applicantPhoto;
    }

    public String getAddressProofCopy() {
        return AddressProofCopy;
    }

    public void setAddressProofCopy(String addressProofCopy) {
        AddressProofCopy = addressProofCopy;
    }

    public String getAgreementCopy() {
        return AgreementCopy;
    }

    public void setAgreementCopy(String agreementCopy) {
        AgreementCopy = agreementCopy;
    }

    public String getNOCCopy() {
        return NOCCopy;
    }

    public void setNOCCopy(String NOCCopy) {
        this.NOCCopy = NOCCopy;
    }

    public String getMachineSpecifications() {
        return MachineSpecifications;
    }

    public void setMachineSpecifications(String machineSpecifications) {
        MachineSpecifications = machineSpecifications;
    }

    public String getMachineInstallationDig() {
        return MachineInstallationDig;
    }

    public void setMachineInstallationDig(String machineInstallationDig) {
        MachineInstallationDig = machineInstallationDig;
    }
}
