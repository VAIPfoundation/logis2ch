package com.sdc2ch.prcss.msngr;

import java.util.List;

public interface IMessengerService {

	String REG_MSNGR_MSG = "[dbo].[SP_2CH_MSNGR_MSG_REG]";

	void sendMessengerMsg(String trnmisUserId, String trnsmisUserNm, String rcverUserId, String title, String body);

	void sendMessengerMsg(String trnmisUserId, String trnsmisUserNm, List<String> rcverUserId, String title, String body);



}
