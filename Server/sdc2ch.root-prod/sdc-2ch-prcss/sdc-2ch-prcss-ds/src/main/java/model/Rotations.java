package model;

import java.util.ArrayList;

public class Rotations extends ArrayList<Route>{

	
	private static final long serialVersionUID = -875616443377435570L;
	
	private long w_start_time;
	private long w_end_time;
	
	public long getW_start_time() {
		return w_start_time;
	}
	public void setW_start_time(long w_start_time) {
		this.w_start_time = w_start_time;
	}
	public long getW_end_time() {
		return w_end_time;
	}
	public void setW_end_time(long w_end_time) {
		this.w_end_time = w_end_time;
	}
	
	
	public boolean orderBy() {

		
		return false;
	}
}
