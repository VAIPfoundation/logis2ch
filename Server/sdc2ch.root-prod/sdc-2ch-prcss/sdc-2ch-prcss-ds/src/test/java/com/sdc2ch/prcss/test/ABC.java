package com.sdc2ch.prcss.test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public abstract class ABC {

	public PublishSubject<String> pub = PublishSubject.create();
	
	
	public ABC(ABC parent){
		this.parent = parent;
	}
	
	
	public void add(ABC abc) {
		this.childs.add(abc);
	}
	
	public ABC parent;
	public List<ABC> childs = new ArrayList<>();
	
	protected abstract void onCreated();
	
	
	
	public Observable<String> subscribe() {
		return pub.filter(s -> true);
	}
	
	
	public void fireEvent(String msg) {
		pub.onNext(msg);
	}
	
	public void onSubscribe(){
		
	}
}
