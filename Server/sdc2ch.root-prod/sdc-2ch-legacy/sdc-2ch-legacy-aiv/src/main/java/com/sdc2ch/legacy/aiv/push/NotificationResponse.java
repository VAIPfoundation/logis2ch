package com.sdc2ch.legacy.aiv.push;

import java.util.List;

public class NotificationResponse {

	private long multicast_id;
	private int success;
	private int failure;
	private int canonical_ids;
	private List<Results> results;
	
	
	public long getMulticast_id() {
		return multicast_id;
	}


	public void setMulticast_id(long multicast_id) {
		this.multicast_id = multicast_id;
	}


	public int getSuccess() {
		return success;
	}


	public void setSuccess(int success) {
		this.success = success;
	}


	public int getFailure() {
		return failure;
	}


	public void setFailure(int failure) {
		this.failure = failure;
	}


	public int getCanonical_ids() {
		return canonical_ids;
	}


	public void setCanonical_ids(int canonical_ids) {
		this.canonical_ids = canonical_ids;
	}


	public List<Results> getResults() {
		return results;
	}


	public void setResults(List<Results> results) {
		this.results = results;
	}


	@Override
	public String toString() {
		return "NotificationResponse [multicast_id=" + multicast_id + ", success=" + success + ", failure=" + failure
				+ ", canonical_ids=" + canonical_ids + ", results=" + results + "]";
	}


	public static class Results {
		
		private String message_id;
		private String error;
		

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getMessage_id() {
			return message_id;
		}

		public void setMessage_id(String message_id) {
			this.message_id = message_id;
		}

		@Override
		public String toString() {
			return "Results [message_id=" + message_id + ", error=" + error + "]";
		}

	}
	
}
