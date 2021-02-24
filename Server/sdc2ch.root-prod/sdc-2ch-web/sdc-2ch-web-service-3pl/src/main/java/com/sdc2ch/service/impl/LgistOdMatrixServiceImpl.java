package com.sdc2ch.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sdc2ch.service.ILgistOdMatrixService;
import com.sdc2ch.service.factory.TmapNavigationApiFactory;
import com.sdc2ch.service.model.OdMatrixInfoVo;
import com.sdc2ch.service.model.TmapApiParam;
import com.sdc2ch.service.util.HttpClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class LgistOdMatrixServiceImpl implements ILgistOdMatrixService {

	@Value("${tmap.api.url:https:
	private String url;
	@Value("${tmap.api.key:ea8ff587-6ade-4287-88af-7ad7ac2d60aa}")
	private String key;
	
	private TmapNavigationApiFactory factory;
	
	private Map<Double, OdMatrixInfoVo> mapped = new HashMap<>();
	
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
			OdMatrixInfoVo info = containsMap(param);
			if(info == null ) {
				String query = factory.createQuery(param);
				info = factory.execute(query);
				info = checkZeroDistance(param, info);
				if(param.getPaths() != null)
					info.setRoutePathCount(param.getPaths().size());
				mapped.put(getKey(param), info);
			}
			return info;
		} catch (IOException e) {
			log.error("{}", e);
		}
		return null;
	}
	
	private OdMatrixInfoVo containsMap(TmapApiParam param) {
		Double key = 0d;
		if(param != null) {
			key = param.getStart().plus() + param.getEnd().plus();
			
			if(param.getPaths() != null) {
				
				for(int i = 0 ; i <param.getPaths().size() ; i++) {
					key += param.getPaths().get(i).plusThousand(i);
				}
				
			}
		}
		
		return mapped.get(key);
		
	}
	
	private double getKey(TmapApiParam param) {
		Double key = 0d;
		if(param != null) {
			key = param.getStart().plus() + param.getEnd().plus();
			
			if(param.getPaths() != null) {
				
				for(int i = 0 ; i <param.getPaths().size() ; i++) {
					key += param.getPaths().get(i).plusThousand(i);
				}
				
			}
		}
		return key;
	}


	
	private OdMatrixInfoVo checkZeroDistance(TmapApiParam param, OdMatrixInfoVo info) {
		try {
			
			while(info.getTotalDistance() == 0 && param.getPaths().size() > 1) {
				param.getPaths().remove(param.getPaths().size() - 1);
				String query = factory.createQuery(param);
				info = factory.execute(query);
			}
			return info;
		} catch (IOException e) {
			log.error("{}", e);
		}
		return null;
	}
}
