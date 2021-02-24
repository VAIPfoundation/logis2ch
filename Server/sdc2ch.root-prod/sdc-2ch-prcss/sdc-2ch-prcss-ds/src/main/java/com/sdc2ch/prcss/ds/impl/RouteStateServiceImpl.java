package com.sdc2ch.prcss.ds.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.prcss.ds.IRouteStateService;
import com.sdc2ch.prcss.ds.repo.T_RouteStateRepository;

@Service
public class RouteStateServiceImpl implements IRouteStateService{

	@Autowired T_RouteStateRepository routeStateRepo;

}
