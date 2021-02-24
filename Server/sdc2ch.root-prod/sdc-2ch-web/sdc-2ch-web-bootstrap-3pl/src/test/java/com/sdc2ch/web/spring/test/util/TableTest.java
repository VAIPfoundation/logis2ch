package com.sdc2ch.web.spring.test.util;

public class TableTest {

	
	public static void main(String[] args) {
		
		
		
		MetricsPrinter tp = new MetricsPrinter(18, 2, "-");
		String oneRow[] = new String[18];
		int[] span = new int[18];

		span[0] = 2; 
		span[1] = 0; 
		span[2] = 2; 
		span[3] = 0; 
		span[4] = 2; 
		span[5] = 0; 
		span[6] = 2; 
		span[7] = 0; 
		span[8] = 2; 
		span[9] = 0; 
		span[10] = 2; 
		span[11] = 0; 
		span[12] = 2; 
		span[13] = 0; 
		span[14] = 2; 
		span[15] = 0; 
		span[16] = 2; 
		span[17] = 0; 

		tp.setTitleAlign(MultiColumnPrinter.CENTER);
		oneRow[0] = "회전율";
		oneRow[1] = "";
		oneRow[2] = "운행료";
		oneRow[3] = "";
		oneRow[4] = "유류비";
		oneRow[5] = "";
		oneRow[6] = "차량지원유류";
		oneRow[7] = "";
		oneRow[8] = "냉동지원유류";
		oneRow[9] = "";
		oneRow[10] = "도로비";
		oneRow[11] = "";
		oneRow[12] = "유류단가";
		oneRow[13] = "";
		oneRow[14] = "운행거리";
		oneRow[15] = "";
		oneRow[16] = "총거리";
		oneRow[17] = "";
		tp.addTitle(oneRow, span);

		oneRow[0] = "TMS원장";
		oneRow[1] = "2CH";
		oneRow[2] = "TMS원장";
		oneRow[3] = "2CH";
		oneRow[4] = "TMS원장";
		oneRow[5] = "2CH";
		oneRow[6] = "TMS원장";
		oneRow[7] = "2CH";
		oneRow[8] = "TMS원장";
		oneRow[9] = "2CH";
		oneRow[10] = "TMS원장";
		oneRow[11] = "2CH";
		oneRow[12] = "TMS원장";
		oneRow[13] = "2CH";
		oneRow[14] = "TMS원장";
		oneRow[15] = "2CH";
		oneRow[16] = "TMS원장";
		oneRow[17] = "2CH";
		tp.addTitle(oneRow);

		oneRow[0] = "TMS원장";
		oneRow[1] = "2CH";
		oneRow[2] = "TMS원장";
		oneRow[3] = "2CH";
		oneRow[4] = "TMS원장";
		oneRow[5] = "2CH";
		oneRow[6] = "TMS원장";
		oneRow[7] = "2CH";
		oneRow[8] = "TMS원장";
		oneRow[9] = "2CH";
		oneRow[10] = "TMS원장";
		oneRow[11] = "2CH";
		oneRow[12] = "TMS원장";
		oneRow[13] = "2CH";
		oneRow[14] = "TMS원장";
		oneRow[15] = "2CH";
		oneRow[16] = "TMS원장";
		oneRow[17] = "2CH";
		tp.add(oneRow);

		oneRow[0] = "TMS원장";
		oneRow[1] = "2CH";
		oneRow[2] = "TMS원장";
		oneRow[3] = "2CH";
		oneRow[4] = "TMS원장";
		oneRow[5] = "2CH";
		oneRow[6] = "TMS원장";
		oneRow[7] = "2CH";
		oneRow[8] = "TMS원장";
		oneRow[9] = "2CH";
		oneRow[10] = "TMS원장";
		oneRow[11] = "2CH";
		oneRow[12] = "TMS원장";
		oneRow[13] = "2CH";
		oneRow[14] = "TMS원장";
		oneRow[15] = "2CH";
		oneRow[16] = "TMS원장";
		oneRow[17] = "2CH";
		tp.add(oneRow);

		tp.println();
	}
}
