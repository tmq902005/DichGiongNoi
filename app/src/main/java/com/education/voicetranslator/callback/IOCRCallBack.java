package com.education.voicetranslator.callback;

public interface IOCRCallBack {
	void getOCRCallBackResult(String response, boolean errorSendPost);
	void getOCRCallBackVisionOCR(String response, boolean errorOCRText);
}
