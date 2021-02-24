package sdc2ch.test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

public class RxJava2Test2 {
	
	
	static Integer subscriber1 = 0;
	static Integer subscriber2 = 0;
	
	public static void main(String[] args) throws InterruptedException {
		
		
		Observable<String> source1 = Observable.interval( 1, TimeUnit.SECONDS).map(v -> (v + 1) + " sec"); 
		
		Observable<String> source2 = Observable.interval(3 , TimeUnit.SECONDS).map(v -> (v + 1) + " msec"); 
		
		final PublishSubject<String> subject = PublishSubject.create(); 
		

		source1.subscribe( subject );
		source2.subscribe( subject );

		


		subject.filter(new Predicate<String>() {

			@Override
			public boolean test(String t) throws Exception {
				return t.endsWith("sec");
			}
		});
		
		subject.filter(new Predicate<String>() {

			@Override
			public boolean test(String t) throws Exception {
				return t.endsWith("msec");
			}
		});
		
        
		

		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				subject.subscribe(new Consumer<String>() {

					@Override
					public void accept(String arg0) throws Exception {
						System.out.println("t1 :> " + arg0);
						
					}
				});
				
			}
		});
		
		t.setDaemon(true);
		t.run();
		Thread.sleep(3000); 
		
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				subject.subscribe(new Consumer<String>() {

					@Override
					public void accept(String arg0) throws Exception {
						System.out.println("t2 :> " + arg0);
						
					}
				});
				
			}
		});

		t2.setDaemon(true);
		t2.start();
		
		Thread.sleep(3000); 
	}
	


}
