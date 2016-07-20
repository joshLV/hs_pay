package com.hszsd.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import net.sf.json.JSONObject;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public abstract class AbstractJunit {


	
	// 用户编号，实际操作时请从session中获取
	public String userId = "6427";

	//将对象转换为JSON格式再控制台输出
	public void outObjectJson(Object object, String message) {
		System.out.println("----------------->" + message);
//		System.out.println(JSONObject.fromObject(object));
	}
}