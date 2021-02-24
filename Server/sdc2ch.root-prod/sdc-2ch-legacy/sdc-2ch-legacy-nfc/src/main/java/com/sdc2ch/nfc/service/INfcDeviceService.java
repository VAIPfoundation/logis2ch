package com.sdc2ch.nfc.service;

import com.sdc2ch.nfc.domain.entity.T_dev;

public interface INfcDeviceService {
	
	T_dev findById(int id);

}
