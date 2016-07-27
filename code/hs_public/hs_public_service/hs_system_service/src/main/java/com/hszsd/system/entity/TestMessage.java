package com.hszsd.system.entity;

public class TestMessage {
	public static void main(String[] args) {
		System.out.println(MessageEnum.APP); 
		System.out.println(MessageEnum.APP.getValue()); 
		System.out.println(MessageEnum.APP.getIndex()); 
	}
}
