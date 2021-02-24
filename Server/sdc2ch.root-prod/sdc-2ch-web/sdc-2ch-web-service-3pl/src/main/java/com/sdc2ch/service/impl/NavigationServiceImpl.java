package com.sdc2ch.service.impl;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sdc2ch.service.INavigationService;
import com.sdc2ch.service.factory.TmapNavigationApiFactory;
import com.sdc2ch.service.model.OdMatrixInfoVo;
import com.sdc2ch.service.model.TmapApiParam;
import com.sdc2ch.service.util.HttpClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class NavigationServiceImpl implements INavigationService {

	@Value("${tmap.api.url:https:
	private String url;
	@Value("${tmap.api.key:ea8ff587-6ade-4287-88af-7ad7ac2d60aa}")
	private String key;
	
	private TmapNavigationApiFactory factory;
	
	@PostConstruct
	private void init() {
		HttpClient client = new HttpClient(url);
		client.addHeader("Content-Type", "application/json");
		client.addHeader("appKey", key);
		this.factory = new TmapNavigationApiFactory(client);
	}






	@Override
	public OdMatrixInfoVo findNavigationInfo(TmapApiParam param) {
		try {
			String query = factory.createQuery(param);
			OdMatrixInfoVo info = factory.execute(query);
			if(param.getPaths() != null)
				info.setRoutePathCount(param.getPaths().size());
			return info;
		} catch (IOException e) {
			log.error("{}", e);
		}
		return null;
	}
}
