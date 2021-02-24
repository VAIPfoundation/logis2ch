package com.sdc2ch.service.test;

import java.util.ArrayList;
import java.util.List;

import com.sdc2ch.service.util.Utils;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransferFTF {
	private TransferLogisticsCenter to;
	private List<TransferVolume> transferVolumes;
	private TransferOperations operations;
	@Override
	public String toString() {
		return Utils.toJsonString(this);
	}

	static class TransferFTFBuilder {
		
		private List<TransferVolume> volums = new ArrayList<>();
		private TransferModel model;
		
		static TransferFTFBuilder builder() {
			return new TransferFTFBuilder();
		}
		public TransferFTFBuilder transferModel(TransferModel model) {
			this.model = model;
			return this;
		}
		public TransferFTF build() {
			TransferFTF tft = new TransferFTF();
			tft.operations = model.getOperations();
			tft.to = tft.operations.getTransTo();
			tft.transferVolumes = volums;
			List<ShipmentVolumeGroup> groups = tft.operations.getVolumeGroups();
			for(ShipmentVolumeGroup group : groups){
				volums.add(TransferVolume.builder()
						.transferItems(group.getItems())
						.transferTFT(tft)
						.id(group.getMapper().id)
						.volumeName(group.getMapper().name()).build());
			}
			return tft;
		}
	}
}
