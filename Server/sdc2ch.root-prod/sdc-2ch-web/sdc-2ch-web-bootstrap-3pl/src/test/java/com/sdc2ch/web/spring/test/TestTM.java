package com.sdc2ch.web.spring.test;

import com.sdc2ch.service.ILgistTransService;

public class TestTM {

	
	public static void main(String[] args) {
		
		System.out.println(ILgistTransService.incomming);
		System.out.println(" ---- ");
		System.out.println(ILgistTransService.outgoing);
		System.out.println(" ---- ");
		System.out.println(ILgistTransService.ITEM_LIST);
		System.out.println(" ---- ");
		System.out.println(ILgistTransService.ITEM_PRODUCT);
		System.out.println(" ---- ");
		System.out.println(ILgistTransService.SHIP_QTY);
		
	}
}
