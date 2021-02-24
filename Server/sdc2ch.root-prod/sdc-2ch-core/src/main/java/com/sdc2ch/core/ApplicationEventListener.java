package com.sdc2ch.core;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sdc2ch.require.ApplicationEventType;
import com.sdc2ch.require.IApplicationEventListener;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

@Component
public class ApplicationEventListener implements IApplicationEventListener {
	
	PublishSubject<ApplicationEvent> subject = PublishSubject.create(); 

	  @EventListener
	  public void handleEvent(Object event) {
			if (event instanceof ApplicationStartedEvent) {
				subject.onNext((ApplicationEvent) event);
			}
			else if (event instanceof ApplicationEnvironmentPreparedEvent) {


			}
			else if (event instanceof ApplicationPreparedEvent) {

			}
			else if (event instanceof ContextClosedEvent && ((ContextClosedEvent) event)
					.getApplicationContext().getParent() == null) {

				subject.onNext((ApplicationEvent) event);
			}
	  }


	@Override
	public Observable<ApplicationEvent> subscribe(ApplicationEventType event) {
		  return subject.filter(e -> {
			  switch(event) {
			case ON_CLOSED:
				return e instanceof ContextClosedEvent && ((ContextClosedEvent) e)
						.getApplicationContext().getParent() == null;
			case ON_START:
				return e instanceof ApplicationStartedEvent;
			default:
				return false;
			  }
		  });
	}
}
