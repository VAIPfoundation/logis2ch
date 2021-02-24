package sdc2ch.test;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class RxJava2Test {
	
	
	static Integer subscriber1 = 0;
	static Integer subscriber2 = 0;
	
	public static void main(String[] args) {
		



		PublishSubject<Integer> subject = PublishSubject.create(); 
		subject.subscribe(getFirstObserver()); 
		subject.onNext(1); 
		subject.onNext(2); 
		subject.onNext(3); 
		subject.subscribe(getSecondObserver()); 
		subject.onNext(4); 
		subject.onComplete();
		subject.onNext(5);
		
		
		System.out.println(subject.count());
		
		System.out.println(subscriber1 + "    :    " + subscriber2);
	}
	
	public static Observer<Integer> getSecondObserver() {
	    return new Observer<Integer>() {
	        @Override
	        public void onNext(Integer value) {
	            subscriber2 += value;
	        }
	 
	        @Override
	        public void onError(Throwable e) {
	            System.out.println("error");
	        }

			@Override
			public void onComplete() {
				
				System.out.println("Subscriber2 completed");
				
			}

			@Override
			public void onSubscribe(Disposable arg0) {
				
				
			}
	    };
	}
	
	public static Observer<Integer> getFirstObserver() {
	    return new Observer<Integer>() {
	        @Override
	        public void onNext(Integer value) {
	           subscriber1 += value;
	        }
	 
	        @Override
	        public void onError(Throwable e) {
	            System.out.println("error");
	        }
	 

			@Override
			public void onComplete() {
				
				System.out.println("Subscriber1 completed");
				
			}

			@Override
			public void onSubscribe(Disposable arg0) {
				
				
			}
	    };
	}

}
