package com.sdc2ch.prcss.ds.t.chain.state;

import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.prcss.ds.t.chain.state.action.ActionEventBuilder;

public class ShippingStateBuilder {

	private ShippingStateBuilder() {
	};

	private ShippingChain shipChain;

	public static ShippingStateBuilder builder() {
		return new ShippingStateBuilder();
	}

	public ShippingStateBuilder shippingChain(ShippingChain chain) {
		this.shipChain = chain;
		return this;
	}

	public ShippingChain build() {
		return shipChain.shipConfig.isFactory() ? new FactoryStateBuilder(shipChain).build()
				: shipChain.shipConfig.isCustomerCenter() ? new CustomerCenterStateBuilder(shipChain).build()
						: new WarehouseStateBuilder(shipChain).build();
	}
	

	public ShippingChain appendEventAction(ShippingChain chain) {
		return ActionEventBuilder.builder().chain(chain).build();
	}

	 
	private class FactoryStateBuilder extends ShippingStateBuilder {

		private ShippingChain chain;
		
		private EnterState enter;
		private ArrivedState arrived;
		private UnLoadingState unLoad;
		private LoadingState load;
		private DepartState depart;
		
		public FactoryStateBuilder(ShippingChain chain) {
			this.chain = chain;
			
			enter = new EnterState(chain);
			arrived = new ArrivedState(chain);
			unLoad = new UnLoadingState(chain);
			load = new LoadingState(chain);
			depart = new DepartState(chain);
		}


		
		public ShippingChain build() {
			
			
			switch (chain.shipConfig.shppTy) {
			case DELEVERY:
				setDeliveryState();
				break;
			case TRANSPORT:
				setTransportState();
				break;
			default:
				break;
			}
			return super.appendEventAction(chain);
		}
		
		private void setDefaultState() {
			
			chain.addState(enter);
			
			chain.addState(arrived);
			
			chain.addState(unLoad);
			
			chain.addState(load);
			
			chain.addState(depart);
		}
		
		
		private void setFTFState() {
			
			if(chain.shipConfig.vrn.equals("경기92자6894")){
				System.out.println();
			}
			
			
			setDefaultState();
			
			
			if(chain.isFirstChain(chain)) {
				chain.removeState(unLoad);
			}
			
			
			
			if(chain.isRouteHeader(chain)) {
				
				
				if(chain.hasPreviousChain(chain)) {
					chain.removeState(enter);
					chain.removeState(arrived);
					chain.removeState(unLoad);
				}
			}
			
			
			if(chain.isRouteTail(chain)) {
				
				
				if(chain.shipConfig.isEmpty) {
					chain.removeState(unLoad);
				}
				
				
				if(chain.hasNextChain(chain)) {
					chain.addState(new TurnningState(chain));
				}
				
				
				if(chain.isEndChain(chain)) {
					chain.removeState(load);
					chain.removeState(depart);
					chain.addState(new FinishState(chain));
				}
			}
		}		
		
		
		private void setFIFState() {
			
			
			setDefaultState();
			
			
			if(chain.isFirstChain(chain)) {
				chain.removeState(unLoad);
			}
			
			
			
			if(chain.isRouteHeader(chain)) {
				
				
				if(chain.hasPreviousRoute(chain)) {
					chain.removeState(enter);
					chain.removeState(arrived);
					chain.removeState(unLoad);
				}
				
				chain.removeState(depart);
			}
			
			
			if(chain.isRouteTail(chain)) {
				
				chain.removeAllState();
				
				
				
				if(chain.hasNextRoute(chain)) {
					
					chain.addState(load);
					
					chain.addState(depart);
				}
				
				
				if(chain.isEndChain(chain)) {
					
					chain.addState(new FinishState(chain));
				}
			}
		}
		
		
		
		private void setTransportState() {
			
			
			
			if(chain.isFIF(chain)) {
				setFIFState();
			}
			
			if(chain.isFTF(chain)) {
				setFTFState();
			}
			chain.isFTW(chain);
			chain.isFTB(chain);
			
			
			
			











































































			
			
			
			












			
		}

		
		private void setDeliveryState() {
			
			
			EnterState enter = new EnterState(chain);
			ArrivedState arrived = new ArrivedState(chain);
			UnLoadingState unLoad = new UnLoadingState(chain);
			LoadingState load = new LoadingState(chain);
			DepartState depart = new DepartState(chain);
			
			
			if(chain.isRouteHeader(chain)) {
				
				
				
				chain.addState(enter);
				
				chain.addState(arrived);


				
				chain.addState(load);
				
				chain.addState(depart);
				
				
				
				
				
				if(chain.hasPreviousRoute(chain)) {
					chain.removeState(enter);
					chain.removeState(arrived);
					chain.removeState(unLoad);
				}
				
				
				
				if(chain.isFirstChain(chain)) {
					chain.removeState(unLoad);
				}

			}
			
			if(chain.isRouteTail(chain)) {
				
				TurnningState turn = new TurnningState(chain);
				
				
				
				if(chain.hasNextRoute(chain)) {
					chain.removeState(unLoad);
					
					
					chain.addState(turn);
				}
				
				
				if(chain.isEndChain(chain)) {
					
					
					if(!chain.shipConfig.isEmpty) {
						

					}
					chain.addState(new FinishState(chain));
				}
			}
		}
		
		
		
		private boolean skipFactory() {
			
			return false;
		}

	}

	 
	private class CustomerCenterStateBuilder extends ShippingStateBuilder {

		private ShippingChain chain;
		
		public CustomerCenterStateBuilder(ShippingChain chain) {
			this.chain = chain;
		}

		public ShippingChain build() {
			chain.addState(new EnterState(chain));
			chain.addState(new ArrivedState(chain));
			chain.addState(new UnLoadingState(chain));

			chain.addState(new DepartState(chain));
			return super.appendEventAction(chain);
		}

	}

	 
	private class WarehouseStateBuilder extends ShippingStateBuilder {
		private ShippingChain chain;

		public WarehouseStateBuilder(ShippingChain chain) {
			this.chain = chain;
		}

		public ShippingChain build() {
			
			switch (chain.shipConfig.dlvyLcSeq) {
			case 1:
				chain.addState(new EnterState(chain));
				chain.addState(new ArrivedState(chain));
				break;
			default:
				chain.addState(new DepartState(chain));
				break;
			}
			return super.appendEventAction(chain);
		}

	}

}
