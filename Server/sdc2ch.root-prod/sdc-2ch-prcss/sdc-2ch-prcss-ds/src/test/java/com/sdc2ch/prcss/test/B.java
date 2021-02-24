package com.sdc2ch.prcss.test;

public class B extends ABC{

	public B(ABC parent) {
		super(parent);
		
	}

	@Override
	protected void onCreated() {
		
		
	}
	
	public void onSubscribe(){
		parent.childs.get(1).subscribe().subscribe(c -> {
			System.out.println(c + " " + c);
		});
	}

}
