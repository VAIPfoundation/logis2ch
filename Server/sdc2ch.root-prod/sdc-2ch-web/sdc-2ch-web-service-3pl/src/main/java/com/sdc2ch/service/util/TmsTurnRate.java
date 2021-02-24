package com.sdc2ch.service.util;

import java.util.Arrays;

import lombok.Getter;

@Getter
public class TmsTurnRate {
	
	private int[] distanceRules;
	private int[] hourRules;
	private int[] dlvyPointRules;
	private float turnRate;
	private String fctryCd;
	private String caralcTy;
	public TmsTurnRate(int minDistance, int maxDistance, int minLdng, int maxLdng, int minHour, int maxHour, float turnRate) {
		this.distanceRules = makeRule(minDistance, maxDistance);
		this.hourRules = makeRule(minHour, maxHour);
		this.dlvyPointRules = makeRule(minLdng, maxLdng);
		this.turnRate = turnRate;
	}
	public TmsTurnRate(String fctryCd, String caralcTy, int minDistance, int maxDistance, int minLdng, int maxLdng, int minHour, int maxHour, float turnRate) {
		this.distanceRules = makeRule(minDistance, maxDistance);
		this.hourRules = makeRule(minHour, maxHour);
		this.dlvyPointRules = makeRule(minLdng, maxLdng);
		this.turnRate = turnRate;
		this.fctryCd = fctryCd;
		this.caralcTy = caralcTy;
	}
	
	private int[] makeRule(int min, int max) {
		int[] rules = {min, max};
		return rules;
	}

	@Override
	public String toString() {
		return "회전율(기준) -> [거리셋팅값=" + Arrays.toString(distanceRules) + ", 시간셋팅값="
				+ Arrays.toString(hourRules) + ", 점착지셋팅값=" + Arrays.toString(dlvyPointRules) + ", 회전율="
				+ turnRate + "]";
	}
	
	public static TmsTurnRateBuilder create() {
		return new TmsTurnRateBuilder();
	}
	
	public static class TmsTurnRateBuilder {
		String fctryCd;String caralcTy;int minDistance;int maxDistance;int minLdng;int maxLdng;int minHour;int maxHour;float turnRate;
		
		public TmsTurnRateBuilder fctryCd(String fctryCd) {
			this.fctryCd = fctryCd;
			return this;
		}
		public TmsTurnRateBuilder caralcTy(String caralcTy) {
			this.caralcTy = caralcTy;
			return this;
		}
		public TmsTurnRateBuilder minDistance(int minDistance) {
			this.minDistance = minDistance;
			return this;
		}
		public TmsTurnRateBuilder maxDistance(int maxDistance) {
			this.maxDistance = maxDistance;
			return this;
		}
		public TmsTurnRateBuilder minLdng(int minLdng) {
			this.minLdng = minLdng;
			return this;
		}
		public TmsTurnRateBuilder maxLdng(int maxLdng) {
			this.maxLdng = maxLdng;
			return this;
		}
		public TmsTurnRateBuilder minHour(int minHour) {
			this.minHour = minHour;
			return this;
		}
		public TmsTurnRateBuilder maxHour(int maxHour) {
			this.maxHour = maxHour;
			return this;
		}
		public TmsTurnRateBuilder turnRate(float turnRate) {
			this.turnRate = turnRate;
			return this;
		}
		public TmsTurnRate build() {
			return new TmsTurnRate(fctryCd, caralcTy, minDistance, maxDistance, minLdng, maxLdng, minHour, maxHour, turnRate);
		}
	}
	
}
