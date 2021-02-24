package com.sdc2ch.service.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sdc2ch.service.model.cost.CarTonType;
import com.sdc2ch.service.model.item.ItemTransferAllocate;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.test.Vehicles.Vehicle;

import lombok.Builder;

@Builder
public class TransferSimulator {
	
	private final TransferFTF tft;
	public TransferSimulator(TransferFTF tft) {
		this.tft = tft;
	}

	public Object simulate() {
		return tft.getTransferVolumes()
				.stream()
				.map(v -> devideVolume(v))
				.collect(Collectors.toList());
	}
	
	
	public List<TransferNewRoute> devideVolume(TransferVolume v) {
		
		
		List<TransferNewRoute> newroure = TransferDivider.builder().volume(v).build().start();
		









		
		v.getOperations().insert(newroure);
		





























































		
		
		
		
		
		return null;
	}
	class Print {
		private String from;
		private String to;
		private String sqty;
		private String pqty;
		private String item;
		
		@Override
		public String toString() {
			return from + "," + to + "," + item + "," + sqty + "," + pqty;
		}
		
	}

}
