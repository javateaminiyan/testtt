package com.examp.three.common.helper;

import com.examp.three.model.Birth_Death.District_Pojo;
import com.examp.three.model.Panchayats;

import java.util.ArrayList;

public interface CommonInterface {

    void getDistrict(ArrayList<District_Pojo> districtPojo, ArrayList<String> arrayList);

    void getPanchayat(ArrayList<Panchayats> panchayatPojo, ArrayList<String> arrayList);


}
