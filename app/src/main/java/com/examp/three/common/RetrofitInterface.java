package com.examp.three.common;

import com.examp.three.model.Grievances_admin.DashboardPojo;
import com.examp.three.model.Grievances_admin.GrievanceData;
import com.examp.three.model.Grievances_admin.ReceivedUpdatePojo;
import com.examp.three.model.Grievances_admin.ReceivedUpdateResult;
import com.examp.three.model.Result;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Admin on 8/3/2018.
 */

public interface RetrofitInterface {
    @GET("EPSGetDistrictDetails")
    Call<Object>getDistricts(@Header("accessToken") String accessToken);
    @GET("EPSGetPanchayatDetails")
    Call<Object>getPanchayat(@Header("accessToken") String accessToken,
                             @Query("DistrictId") int DistrictId);
    @GET("EPSTaxMasterDetails")
    Call<Object>getMasterDetails(@Header("accessToken") String accessToken,
                                 @Query("Type") String Type,
                                 @Query("Input") String Input,
                                 @Query("District") String District,
                                 @Query("Panchayat") String Panchayat
    );
    @GET("EPSTrackingRequest")
    Call<Object>getTrackingDetails(@Header("accessToken") String accessToken,
                                   @Query("Type") String Type,
                                   @Query("MobileNo") String MobileNo,
                                   @Query("RequestNo") String RequestNo,
                                   @Query("RequestDate") String RequestDate,
                                   @Query("District") String District,
                                   @Query("Panchayat") String Panchayat
    );
    @GET("EPSApplyBirthRequest")
    Call<Object>saveBirthDetails(@Header("accessToken") String accessToken,
                                 @Query("District") String District,
                                 @Query("Panchayat") String Panchayat,
                                 @Query("MobileNo") String MobileNo,
                                 @Query("EmailId") String EmailId,
                                 @Query("DOB") String DOB,
                                 @Query("Gender") String Gender,
                                 @Query("ChildName") String ChildName,
                                 @Query("FatherName") String FatherName,
                                 @Query("MotherName") String MotherName,
                                 @Query("FatherAadhaarNo") String FatherAadhaarNo,
                                 @Query("MotherAadhaarNo") String MotherAadhaarNo,
                                 @Query("PerAddress") String PerAddress,
                                 @Query("PerPincode") String PerPincode,
                                 @Query("BirthAddress") String BirthAddress,
                                 @Query("BirthPincode") String BirthPincode,
                                 @Query("BirthPlace") String BirthPlace,
                                 @Query("HospitalCode") String HospitalCode,
                                 @Query("HospitalName") String HospitalName,
                                 @Query("DoorNo") String DoorNo,
                                 @Query("WardNo") String WardNo,
                                 @Query("StreetCode") String StreetCode,
                                 @Query("StreetName") String StreetName,
                                 @Query("StateName") String StateName,
                                 @Query("DistrictName") String DistrictName,
                                 @Query("TownVillage") String TownVillage,
                                 @Query("TownVillageName") String TownVillageName,
                                 @Query("Religion") String Religion,
                                 @Query("ReligionOthers") String ReligionOthers,
                                 @Query("FatherEducation") String FatherEducation,
                                 @Query("MotherEducation") String MotherEducation,
                                 @Query("FatherOccupation") String FatherOccupation,
                                 @Query("MotherOccupation") String MotherOccupation,
                                 @Query("MotherMarriageAge") String MotherMarriageAge,
                                 @Query("MotherChildBirthAge") String MotherChildBirthAge,
                                 @Query("ChildBornAlive") String ChildBornAlive,
                                 @Query("ChildWeight") String ChildWeight,
                                 @Query("MethodofDelivery") String MethodofDelivery,
                                 @Query("TypeAttentionDelivery") String TypeAttentionDelivery,
                                 @Query("PregnancyDuration") String PregnancyDuration,
                                 @Query("EntryType") String EntryType
    );
    @GET("EPSApplyDeathRequest")
    Call<Object>saveDeathDetails(@Header("accessToken") String accessToken,
                                 @Query("District") String District,
                                 @Query("Panchayat") String Panchayat,
                                 @Query("MobileNo") String MobileNo,
                                 @Query("EmailId") String EmailId,
                                 @Query("DOD") String DOD,
                                 @Query("Gender") String Gender,
                                 @Query("Age") String Age,
                                 @Query("AgeType") String AgeType,
                                 @Query("DeceasedName") String DeceasedName,
                                 @Query("HusbandWifeName") String HusbandWifeName,
                                 @Query("FatherName") String FatherName,
                                 @Query("MotherName") String MotherName,
                                 @Query("PerAddress") String PerAddress,
                                 @Query("PerPincode") String PerPincode,
                                 @Query("DeathAtAddress") String DeathAtAddress,
                                 @Query("DeathAtPincode") String DeathAtPincode,
                                 @Query("DeathPlace") String DeathPlace,
                                 @Query("HospitalCode") String HospitalCode,
                                 @Query("HospitalName") String HospitalName,
                                 @Query("DoorNo") String DoorNo,
                                 @Query("WardNo") String WardNo,
                                 @Query("StreetCode") String StreetCode,
                                 @Query("StreetName") String StreetName,
                                 @Query("OtherAddress") String OtherAddress,
                                 @Query("OtherPincode") String OtherPincode,
                                 @Query("StateName") String StateName,
                                 @Query("DistrictName") String DistrictName,
                                 @Query("TownVillage") String TownVillage,
                                 @Query("TownVillageName") String TownVillageName,
                                 @Query("Religion") String Religion,
                                 @Query("ReligionOthers") String ReligionOthers,
                                 @Query("OccupationDeceased") String OccupationDeceased,
                                 @Query("TypeMedicalAttention") String TypeMedicalAttention,
                                 @Query("MedicalCertified") String MedicalCertified,
                                 @Query("CauseDisease") String CauseDisease,
                                 @Query("DeathOccurPregnancy") String DeathOccurPregnancy,
                                 @Query("HabituallySmoke") String HabituallySmoke,
                                 @Query("HabituallySmokeYears") String HabituallySmokeYears,
                                 @Query("HabituallyTobacco") String HabituallyTobacco,
                                 @Query("HabituallyTobaccoYears") String HabituallyTobaccoYears,
                                 @Query("HabituallyChewArecaunt") String HabituallyChewArecaunt,
                                 @Query("HabituallyChewArecauntYears") String HabituallyChewArecauntYears,
                                 @Query("HabituallyDrinkAlcohol") String HabituallyDrinkAlcohol,
                                 @Query("HabituallyDrinkAlcoholYears") String HabituallyDrinkAlcoholYears,
                                 @Query("EntryType") String EntryType
    );
    @GET("EPSNonTaxRequest")
    Call<Object> saveNonTaxDetails(@Header("accessToken") String accessToken,
                                   @Query("District") String District,
                                   @Query("Panchayat") String Panchayat,
                                   @Query("Name") String Name,
                                   @Query("MobileNo") String MobileNo,
                                   @Query("EmailId") String EmailId,
                                   @Query("LeaseName") String LeaseName,
                                   @Query("DoorNo") String DoorNo,
                                   @Query("WardNo") String WardNo,
                                   @Query("StreetCode") String StreetCode,
                                   @Query("StreetName") String StreetName,
                                   @Query("EntryType") String EntryType
    );

    @GET("Trade_GetTradeApplicationStatusListByUser")
    Call<Object> getReferenceNo(
            @Header("accessToken") String accessToken,
            @Query("PublicUserId") String PublicUserId
    );

    @GET("Trade_GetTradeApplicationStatusDetailsByUser")
    Call<Object> getApplicationStatus(
            @Header("accessToken") String accessToken,
            @Query("ApplicationRefNo") String ApplicationRefNo
    );

    @GET("Request_Status")
    Call<Object> Request_Status(
            @Header("accessToken") String accessToken,
            @Query("UserId") String UserId,
            @Query("UserName") String UserName
    );

    @GET("/Etown/Building_Type")
    Call<Object>getBuildingType(@Header("accessToken")String accessToken,
                                @Query("Panchayat")String Panchayat,
                                @Query("District")String District);
    @GET("/Etown/Surveyor")
    Call<Object>getSurveyar(@Header("accessToken")String accessToken,
                            @Query("Panchayat")String Panchayat,
                            @Query("District")String District);

    @GET("/Etown/New_application_status")
    Call<Object> getBuildingLicence_IncompleteDetails(
            @Header("accessToken") String accessToken,
            @Query("UserId") String UserId,
            @Query("UserName") String UserName
    );

    @GET("/Etown/View_BuildingType")
    Call<Object> getMeasurementDetails(@Header("accessToken") String accessToken,
                                       @Query("Panchayat") String Panchayat,
                                       @Query("District") String District,
                                       @Query("BuildingType") String BuildingType
    );

    @GET("/Etown/BL_InsertOnlineApplication")
    Call<Object>insertBuildingPlan(@Header("accessToken")String accessToken,
                                   @Query("QueryType")String QueryType,
                                   @Query("ServiceCategory")String ServiceCategory,
                                   @Query("ServiceName")String ServiceName,
                                   @Query("ApplicationDate")String ApplicationDate,
                                   @Query("ApplicationName")String ApplicationName,
                                   @Query("FatherName")String FatherName,
                                   @Query("MobileNo")String MobileNo,
                                   @Query("CommunicationAddress")String CommunicationAddress,
                                   @Query("EmailID")String EmailID,
                                   @Query("SurveyNo")String SurveyNo,
                                   @Query("DocumentNo")String DocumentNo,
                                   @Query("DoorNo")String DoorNo,
                                   @Query("WardNo")String WardNo,
                                   @Query("StreetNo")String StreetNo,
                                   @Query("Town")String Town,
                                   @Query("City")String City,
                                   @Query("Pincode")String Pincode,
                                   @Query("PlatNo")String PlatNo,
                                   @Query("BlockNo")String BlockNo,
                                   @Query("BuildingType")String BuildingType,
                                   @Query("PlotAreaSqFt")String PlotAreaSqFt,
                                   @Query("PlotAreaSqMt")String PlotAreaSqMt,
                                   @Query("SurveyorName")String SurveyorName,
                                   @Query("AppStatus")String AppStatus,
                                   @Query("AppDescription")String AppDescription,
                                   @Query("UserId")String UserId,
                                   @Query("UserName")String UserName,
                                   @Query("Panchayat")String Panchayat,
                                   @Query("District")String District,
                                   @Query("CreatedBy")String CreatedBy
    );
    @GET("/Etown/BL_InsertOnlineApplication")
    Call<Object>updateBuildingPlan(@Header("accessToken")String accessToken,
                                   @Query("QueryType")String QueryType,
                                   @Query("ServiceCategory")String ServiceCategory,
                                   @Query("ServiceName")String ServiceName,
                                   @Query("ApplicationDate")String ApplicationDate,
                                   @Query("ApplicationName")String ApplicationName,
                                   @Query("FatherName")String FatherName,
                                   @Query("MobileNo")String MobileNo,
                                   @Query("CommunicationAddress")String CommunicationAddress,
                                   @Query("EmailID")String EmailID,
                                   @Query("SurveyNo")String SurveyNo,
                                   @Query("DocumentNo")String DocumentNo,
                                   @Query("DoorNo")String DoorNo,
                                   @Query("WardNo")String WardNo,
                                   @Query("StreetNo")String StreetNo,
                                   @Query("Town")String Town,
                                   @Query("City")String City,
                                   @Query("Pincode")String Pincode,
                                   @Query("PlatNo")String PlatNo,
                                   @Query("BlockNo")String BlockNo,
                                   @Query("BuildingType")String BuildingType,
                                   @Query("PlotAreaSqFt")String PlotAreaSqFt,
                                   @Query("PlotAreaSqMt")String PlotAreaSqMt,
                                   @Query("SurveyorName")String SurveyorName,
                                   @Query("AppStatus")String AppStatus,
                                   @Query("AppDescription")String AppDescription,
                                   @Query("UserId")String UserId,
                                   @Query("UserName")String UserName,
                                   @Query("Panchayat")String Panchayat,
                                   @Query("District")String District,
                                   @Query("CreatedBy")String CreatedBy,
                                   @Query("ApplicationNo")String ApplicationNo
    );


    @Multipart
    @Headers("accessToken:eyJhbGciOiJIUzI1NiJ9.UHJlbWF0aXg.VnYf5L2bruAL3IhIbhOCnqW3SADSM2qjWrZAV0yrB94")
    @POST("/Etown/InsertOnlineApplicationIMG_New")
    Call<ResponseBody> UploadBuildingPlanDoc(@Part MultipartBody.Part image, @Query("ApplicationID") String ApplicationID,
                                             @Query("Panchayat") String Panchayat, @Query("District") String District,
                                             @Query("imgStatus") String imgStatus, @Query("UploadType") String UploadType);

    @GET("EPSGetDistrictDetails")
    Call<Object> getDistrict(
            @Header("accessToken") String accessToken
    );

//    @GET("EPSGetPanchayatDetails")
//    Call<Object> getPanchayat(
//            @Header("accessToken") String accessToken,
//            @Query("DistrictId") int districtId
//    );

    @GET("EPSBirthDeathAbstract")
    Call<Object> getBirthDetailsDatas(
            @Header("accessToken") String accessToken,
            @Query("Type") String Type,
            @Query("Year") String Year,
            @Query("WardNo") String WardNo,
            @Query("District") String District,
            @Query("Panchayat") String Panchayat
    );

    @Headers("accessToken:eyJhbGciOiJIUzI1NiJ9.UHJlbWF0aXg.VnYf5L2bruAL3IhIbhOCnqW3SADSM2qjWrZAV0yrB94")
    @POST("testing_OTP_sms")
    Call<Object> sendOTP(
            @Query("mailormobile") String mailormobile
    );

    @GET("EPSBirthDeathSearch")
    Call<Object> getBirthCertificateSearchDetails(
            @Header("accessToken") String accessToken,
            @Query("Type") String Type,
            @Query("Date") String Date,
            @Query("Gender") String Gender,
            @Query("District") String District,
            @Query("Panchayat") String Panchayat
    );

    @GET("EPSTaxMasterDetails")
    Call<Object> getWard_Street(
            @Header("accessToken") String accessToken,
            @Query("Type") String Type,
            @Query("Input") String Input,
            @Query("District") String District,
            @Query("Panchayat") String Panchayat
    );

    @GET("EPSGrievancesRegister")
    Call<Object> submitGrievances(
            @Header("accessToken") String accessToken,
            @Query("District") String District,
            @Query("Panchayat") String Panchayat,
            @Query("CType") String CType,
            @Query("Description") String Description,
            @Query("Name") String Name,
            @Query("DoorNo") String DoorNo,
            @Query("WardNo") String WardNo,
            @Query("StreetName") String StreetName,
            @Query("City") String City,
            @Query("MobileNo") String MobileNo,
            @Query("EmailId") String EmailId,
            @Query("EntryType") String EntryType
    );

    //responsebody for string response using okhttp3
    @GET("SendSMS")
    Call<ResponseBody> sendSMS(

            @Query("SMSMobileNo") String SMSMobileNo,
            @Query("SMSMessage") String SMSMessage
    );


    @GET("/Etown/EPSGrievancesTracking")
    Call<Object> getGrievance(@Header("accessToken")String accessToken,
                              @Query("GrievancesNo")String GrievancesNo,
                              @Query("FromDate")String FromDate,
                              @Query("ToDate")String ToDate


    );
    @GET("/Etown/EPSSingleGrievance")
    Call<Object> getSingleGrievance(@Header("accessToken")String accessToken,
                                    @Query("GrievancesNo")String GrievancesNo,
                                    @Query("District")String District,
                                    @Query("Panchayat")String Panchayat
    );

    @GET("EPSPanchayatContact")
    Call<Object> getEODetails(
            @Header("accessToken") String accessToken,
            @Query("District") String District,
            @Query("Panchayat") String Panchayat
    );

    @GET("/Etown/ETPanchayatGrievanceDetails")
    Call<List<GrievanceData>> getGrievanceDatas(@Header("accessToken")String accessToken,
                                                @Query("Type")String Type,
                                                @Query("District")String District,
                                                @Query("Panchayat")String Panchayat,
                                                @Query("GrievanceNo")String GrievanceNo
    );
    @GET("/Etown/ETPanchayatGrievanceDetails")
    Call<List<ReceivedUpdatePojo>> getGrievanceReceivedDatas(@Header("accessToken")String accessToken,
                                                             @Query("Type")String Type,
                                                             @Query("District")String District,
                                                             @Query("Panchayat")String Panchayat,
                                                             @Query("GrievanceNo")String GrievanceNo
    );
    @GET("/Etown/PanchayatComplaintResponse")
    Call<ReceivedUpdateResult> updateResult(@Header("accessToken")String accessToken,
                                            @Query("UniqueId")String UniqueId,
                                            @Query("GrievanceNo")String GrievanceNo,
                                            @Query("District")String District,
                                            @Query("Panchayat")String Panchayat,
                                            @Query("Remarks")String Remarks,
                                            @Query("Status")String Status,
                                            @Query("UserDesgination")String UserDesgination,
                                            @Query("UserId")String UserId,
                                            @Query("UserName")String UserName,
                                            @Query("EntryType")String EntryType
    );
    @GET("/Etown/ETPanchayatGrievanceDetails")
    Call<List<DashboardPojo>> getDashboardDatas(@Header("accessToken")String accessToken,
                                                @Query("Type")String Type,
                                                @Query("District")String District,
                                                @Query("Panchayat")String Panchayat,
                                                @Query("GrievanceNo")String GrievanceNo
    );

    @GET("/Etown/Trade_OnlineTradersLicenceRequest")
    Call<Object> saveTradeLicenseDetails(@Header("accessToken")String accessToken,
                                         @Query("qType")String qType,
                                         @Query("Sno")String Sno,
                                         @Query("ExApplicationNo")String ExApplicationNo,
                                         @Query("ApplicationType")String ApplicationType,
                                         @Query("ApplicantInitial")String ApplicantInitial,
                                         @Query("ApplicantFirstName")String ApplicantFirstName,
                                         @Query("ApGender")String ApGender,
                                         @Query("ApFatherInitial")String ApFatherInitial,
                                         @Query("ApFatherFirstName")String ApFatherFirstName,
                                         @Query("ApMobileNo")String ApMobileNo,
                                         @Query("ApEmail")String ApEmail,
                                         @Query("ApDoorNo")String ApDoorNo,
                                         @Query("ApStreetName")String ApStreetName,
                                         @Query("ApCityName")String ApCityName,
                                         @Query("ApDistrict")String ApDistrict,
                                         @Query("ApPINCode")String ApPINCode,
                                         @Query("ApAadharNo")String ApAadharNo,
                                         @Query("ApGSTNo")String ApGSTNo,
                                         @Query("District")String District,
                                         @Query("Panchayat")String Panchayat,
                                         @Query("WardNo")String WardNo,
                                         @Query("StreetName")String StreetName,
                                         @Query("DoorNo")String DoorNo,
                                         @Query("LicenceValidity")String LicenceValidity,
                                         @Query("LicenceTypeId")String LicenceTypeId,
                                         @Query("TradeName")String TradeName,
                                         @Query("TradeCode")String TradeCode,
                                         @Query("TradeRate")String TradeRate,
                                         @Query("EstablishmentName")String EstablishmentName,
                                         @Query("motorInstalled")String motorInstalled,
                                         @Query("motorTotalQty")String motorTotalQty,
                                         @Query("HorsePowerTotal")String HorsePowerTotal,
                                         @Query("BuldingType")String BuldingType,
                                         @Query("RentPaid")String RentPaid,
                                         @Query("RentalAgrStatus")String RentalAgrStatus,
                                         @Query("NOCStatus")String NOCStatus,
                                         @Query("FinancialYear")String FinancialYear,
                                         @Query("EntryUserId")String EntryUserId,
                                         @Query("EntryUser")String EntryUser,
                                         @Query("ValidFrom")String ValidFrom,
                                         @Query("ValidTo")String ValidTo,
                                         @Query("Penalty")String Penalty,
                                         @Query("PFA")String PFA,
                                         @Query("Advertisement")String Advertisement,
                                         @Query("ServiceCharge")String ServiceCharge,
                                         @Query("EntryType")String EntryType
    );

    @Multipart
    @Headers("accessToken:eyJhbGciOiJIUzI1NiJ9.UHJlbWF0aXg.VnYf5L2bruAL3IhIbhOCnqW3SADSM2qjWrZAV0yrB94")
    @POST("Trp_InsertTradersDocument")
    Call<ResponseBody> UploadTradeDoc(@Part MultipartBody.Part image, @Query("qType") String qtype,
                                      @Query("ApplicationRefId") String applicationrefId, @Query("MobileNo") String mobileno,
                                      @Query("UserId") String userid,@Query("UserName") String username,@Query("EntryType") String entrytype);


    @Multipart
    @Headers("accessToken:eyJhbGciOiJIUzI1NiJ9.UHJlbWF0aXg.VnYf5L2bruAL3IhIbhOCnqW3SADSM2qjWrZAV0yrB94")
    @POST("PropertyAssessmentUploads")
    Call<Result> UploadPropertyNewAssessment(@Part MultipartBody.Part image, @Query("RequestNo") String requestNo, @Query("District") String district, @Query("Panchayat") String panchayat,
                                             @Query("UploadType") String uploadType, @Query("EntryType") String entryType);

    @Multipart
    @Headers("accessToken:eyJhbGciOiJIUzI1NiJ9.UHJlbWF0aXg.VnYf5L2bruAL3IhIbhOCnqW3SADSM2qjWrZAV0yrB94")
    @POST("ProfessionAssessmentUploads")
    Call<Result> UploadProfessionNewAssessment(@Part MultipartBody.Part image, @Query("RequestNo") String requestNo, @Query("District") String district, @Query("Panchayat") String panchayat,
                                                     @Query("UploadType") String uploadType, @Query("EntryType") String entryType);

    @Multipart
    @Headers("accessToken:eyJhbGciOiJIUzI1NiJ9.UHJlbWF0aXg.VnYf5L2bruAL3IhIbhOCnqW3SADSM2qjWrZAV0yrB94")
    @POST("WaterConnectionUploads")
    Call<Result> UploadWaterNewAssessment(@Part MultipartBody.Part image, @Query("RequestNo") String requestNo,
                                                @Query("District") String district, @Query("Panchayat") String panchayat,
                                                @Query("EntryType") String entryType);

    @Multipart
    @Headers("accessToken:eyJhbGciOiJIUzI1NiJ9.UHJlbWF0aXg.VnYf5L2bruAL3IhIbhOCnqW3SADSM2qjWrZAV0yrB94")
    @POST("NameTransferUploads")
    Call<Result> UploadNameTransfer( @Part MultipartBody.Part image, @Query("equestNo") String requestNo,
                                           @Query("District") String district, @Query("Panchayat") String panchayat,
                                           @Query("TaxNo") String taxNo, @Query("UploadType") String uploadType,
                                           @Query("EntryTyp") String entryType);

    @Multipart
    @Headers("accessToken:eyJhbGciOiJIUzI1NiJ9.UHJlbWF0aXg.VnYf5L2bruAL3IhIbhOCnqW3SADSM2qjWrZAV0yrB94")
    @POST("Trp_InsertTradersDocument")
    Call<Result> UploadLicenceRenewal( @Part MultipartBody.Part image,
                                             @Query("qType") String queryType,
                                             @Query("ApplicationRefId") String applicationRefId,
                                             @Query("MobileNo") String mobileNo,@Query("UserId") String userId,
                                             @Query("UserName") String username,@Query("EntryType") String entryType);

    @GET("Trade_GetTemporaryApplication")
    Call<Object> getTL_IncompleteDetails(
            @Header("accessToken") String accessToken,
            @Query("UserId") String UserId
    );

    @GET("Trade_GetTradeNameByLicenceType")
    Call<Object>getTradeName(@Header("accessToken")String accessToken,
                             @Query("District")String District,
                             @Query("Panchayat")String Panchayat,
                             @Query("LicenceTypeId")String LicenceTypeId
    );

    @GET("Trade_GetLicenceTypeMaster")
    Call<Object>getLicenseTypeMaster(@Header("accessToken")String accessToken);

    @GET("Trade_GetTradeLicenceValidityFor")
    Call<Object>getLicenseFor(@Header("accessToken")String accessToken);


    @GET("Trade_GetTemporaryApplicationAllDetails")
    Call<Object> getTL_AllIncompleteDetails(
            @Header("accessToken") String accessToken,
            @Query("Sno") int Sno,
            @Query("UserId") String UserId
    );

    @GET("State_Master")
    Call<Object> getStates();

    @GET("EtownServicedemo/identifier")
    Call<Object> verifyEmailIdFromUser(
            @Query("identify") String identify
    );

    @GET("EtownServicedemo/getUserId")
    Call<Object> getUserDetails(
            @Query("emailId") String emailId
    );
}
