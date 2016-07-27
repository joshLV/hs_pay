package com.hszsd.test;

import com.hszsd.common.util.Result;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hszsd.common.util.ReturnMsg;
import com.hszsd.user.service.UserService;

public class TestJunitUser extends AbstractJunit{

	@Autowired
	private UserService userServiceImpl;
	
	@Test
	public void isExistsUserName(){
		Result msg=userServiceImpl.isExistsUserName("bjaiwu");
		outObjectJson(msg,"");
	}
}
