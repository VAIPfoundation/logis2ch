package com.sdc2ch.prcss.ds.t.receiver;

import java.util.Arrays;
import java.util.List;

import com.sdc2ch.nfc.enums.NfcEventType;
import com.sdc2ch.nfc.event.INfcFireEvent;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext;
import com.sdc2ch.prcss.ds.impl.NfcTagEventImpl;
import com.sdc2ch.require.event.I2ChEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShippingNfcHandler extends AbstractShippingReceiver {
	
	private List<NfcEventType> supported = Arrays.asList(NfcEventType.IDENTITY_SUCCESS_FINGERPRINT, 
			NfcEventType.VERIFY_SUCCESS_CARD, NfcEventType.VERIFY_SUCCESS_ID_PIN);
	
	public ShippingNfcHandler(ShippingPlanContext context) {
		super(context, INfcFireEvent.class);
	}

	@Override
	protected void onReceive(I2ChEvent<?> e) {
		
		if(e instanceof INfcFireEvent) {
			try {
				INfcFireEvent event = (INfcFireEvent) e;
				if(supported.contains(event.getEvent())) {
					NfcTagEventImpl nfc = NfcTagEventImpl.of(e.user(), ((INfcFireEvent) e).getEventTime(),
							((INfcFireEvent) e).getSetupLcTy());
					
					nfc.setFactoryType(event.getFactoryType());


					
					super.fireEvent(nfc);
				}

			}catch (Exception ex) {
				log.error("{}", ex);
			}
		}
	}

}
