package com.sdc2ch.require.enums;

public enum SetupLcType {
	A1COLD("A1냉장고"),
	A2COLD("A2냉장고"),
	B1COLD("B1냉장고"),
	B2COLD("B2냉장고"),
	PRCSSGD("가공품창고"),
	STERILIZED("멸균창고"),
	OFFICE("사무실"),
	CHEESE("치즈창고"),
	FLAT("평지창고"),
	HOSANG("호상창고"),
	CU("시유창고"),
	TEST("테스트창고"),
	FRONT("통문경비실")
	
	;
	public String lcName;
	SetupLcType(String name){
		this.lcName  = name;
	}
}
