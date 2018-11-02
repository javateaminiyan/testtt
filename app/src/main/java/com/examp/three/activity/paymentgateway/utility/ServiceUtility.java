package com.examp.three.activity.paymentgateway.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

public class ServiceUtility{
	
	public java.util.Map readProperties(String pFilePath)throws IOException {
		java.util.Map vPropertyMap=new LinkedHashMap();
        Set vTckey;
		Iterator<String> vTcPropItr;
		Properties vTCProperties=null;
		try {
			vTCProperties = new Properties();
			vTCProperties.load(ServiceUtility.class.getResourceAsStream(pFilePath));
			vTckey = vTCProperties.keySet();
			vTcPropItr = vTckey.iterator();
			vPropertyMap=new LinkedHashMap();
			while(vTcPropItr.hasNext()){
				String vKey=vTcPropItr.next();
				vPropertyMap.put(vKey, vTCProperties.get(vKey));
			}
		}finally{
			vTcPropItr=null;
			vTckey=null;
			vTCProperties=null;
		}
        return vPropertyMap;
    }
	
	public static Object chkNull(Object pData){
		return (pData==null?"":pData);
	}

	public static String addToPostParams(String paramKey, String paramValue){
		if(paramValue!=null)
			return paramKey.concat(Constants.PARAMETER_EQUALS).concat(paramValue)
					.concat(Constants.PARAMETER_SEP);
		return "";
	}
}