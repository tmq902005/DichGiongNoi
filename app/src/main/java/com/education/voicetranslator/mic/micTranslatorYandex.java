package com.education.voicetranslator.mic;

import java.util.Random;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.education.voicetranslator.callback.YTranslatorCallBack;
import com.education.voicetranslator.common.Common_Resource;
import com.education.voicetranslator.translation.LanguageY;
import com.education.voicetranslator.translation.TranslateY;
import com.education.voicetranslator.translation.YandexTranslatorAPI;

public class micTranslatorYandex extends AsyncTask<Void, Integer, String> {
	/** Called when the activity is first created. */
	private String str_in;
    private LanguageY lg_in;
    private LanguageY lg_out;
    private YTranslatorCallBack mYTranslatorCallBack;
    private String mProgressingName;
    private Common_Resource commonResource;
	private int server = 0;

	public micTranslatorYandex(Context mContext, String str_in, LanguageY lg_in, LanguageY lg_out,
                               YTranslatorCallBack yTranslatorCallBack, String progressingName) {
		this.str_in = str_in;
		this.lg_in = lg_in;
		this.lg_out = lg_out;
        this.commonResource = new Common_Resource(mContext);
		this.server = randInt(0, commonResource.getListKeyYTranslate().length - 1);
		this.mYTranslatorCallBack = yTranslatorCallBack;
		this.mProgressingName = progressingName;

	}

	@Override
	protected String doInBackground(Void... arg0) {

		YandexTranslatorAPI.setKey(commonResource.getListKeyYTranslate()[server]);
		Log.d("speechtotext", "onTraslate with Yandex server " + server);
        String translatedText = "translate false";

		try {
			if (!lg_in.equals(LanguageY.AUTODETECT))
				translatedText = TranslateY.execute(str_in, lg_in, lg_out);
			else
				translatedText = TranslateY.execute(str_in, lg_out);

		} catch (Exception e) {
			String loi = e.toString();
			Log.d("micTranslator Error", loi);
			translatedText = "translate false";

		}

		// }
		return translatedText;
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	@Override
	protected void onPostExecute(String result) {
		mYTranslatorCallBack.getYTranslateCallBack(result,mProgressingName);
		super.onPostExecute(result);
	}
}
