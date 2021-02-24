package com.sdc2ch.prcss.test;

public class A extends ABC{
	
	public A(ABC parent) {
		super(parent);
	}

	public static void main(String[] args) {
		
		A a = new A(null);
		B b = new B(a);
		C c = new C(a);
		


		a .add(b);
		a .add(c);
		a.onSubscribe();
		
		b.fireEvent("안녕B");

		c.fireEvent("안녕C");
		
		System.out.println("");
		
	}

	public void onSubscribe() {
		
		childs.stream().forEach(c -> {
			c.onSubscribe();
			c.subscribe().subscribe(s -> {
				
				System.out.println(c + " " + s);
				
			});
		});
	}

	@Override
	protected void onCreated() {
		
	}

}
