package com.hszsd.webpay.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MyHostnameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(String hostname, SSLSession arg1) {
		return true;
		/* if("hszsdshop.com".equals(hostname)){  
	            return true;  
	        } else {  
	            return false;  
	        }  */
	}

}
