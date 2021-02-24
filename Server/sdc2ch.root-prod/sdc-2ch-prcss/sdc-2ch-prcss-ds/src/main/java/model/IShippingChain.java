package model;

import java.util.List;


public interface IShippingChain {
	
	public enum ChainNm {
		
		HOME("집"),
		FACTORY("공장"),
		WAREHOUSE("창고"),
		CUSTOMER_CENTER("고객센터")
		;
		public String chainNm;
		ChainNm(String chainNm){
			this.chainNm = chainNm;
		}
	}

	public IShippingChain stateBuilder();
	
	public void setStates(List<IShippingState> states);

	
	public boolean removeState(IShippingState state);

	
	public boolean addState(IShippingState state);

	
	public void removeAllState();
	
	
	public List<IShippingState> getLogisticStates();

	public ChainNm chainName();

}
