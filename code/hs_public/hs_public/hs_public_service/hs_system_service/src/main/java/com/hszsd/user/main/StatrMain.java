package com.hszsd.user.main;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class StatrMain {
	public static void main(String[] args) {
		//System.out.println(MessageEnum.APP); 
		
		// 测试常规服务
		FileSystemXmlApplicationContext context=new FileSystemXmlApplicationContext(args[0]);
		//ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
		//		"classpath*:applicationContext.xml");
		context.start();
	}
}
