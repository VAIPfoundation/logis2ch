package com.sdc2ch.nfc.service;

import com.sdc2ch.nfc.domain.NfcFireEvent;

public interface INfcFireEventService { 
	
	void fireEvent(NfcFireEvent event);

}
