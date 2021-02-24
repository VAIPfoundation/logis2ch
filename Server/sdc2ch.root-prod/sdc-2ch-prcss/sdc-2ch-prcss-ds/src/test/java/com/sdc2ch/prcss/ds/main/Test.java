package com.sdc2ch.prcss.ds.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Test {

	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public void beepForAnHour() {
		final Runnable beeper = new Runnable() {
			public void run() {
				System.out.println("beep");
			}
		};
		
		
		for(int i = 0 ; i< 100 ; i++) {
			
			final int idx = i;
			final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 10, 10, TimeUnit.SECONDS);
			scheduler.schedule(new Runnable() {
				public void run() {
					System.out.println(idx);
					beeperHandle.cancel(true);
				}
			}, 12, TimeUnit.SECONDS);
		}
	
	}
	
	public static void main(String[] args) {
		new Test().beepForAnHour();
		scheduler.shutdown();
	}
}
