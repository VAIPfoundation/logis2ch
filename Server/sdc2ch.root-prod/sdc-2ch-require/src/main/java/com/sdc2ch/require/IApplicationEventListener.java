package com.sdc2ch.require;

import org.springframework.context.ApplicationEvent;

import io.reactivex.Observable;

public interface IApplicationEventListener {
	Observable<ApplicationEvent> subscribe(final ApplicationEventType event) ;
}
