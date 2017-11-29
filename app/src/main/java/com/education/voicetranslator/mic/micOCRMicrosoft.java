package com.education.voicetranslator.mic;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.education.voicetranslator.callback.IOCRCallBack;
import com.education.voicetranslator.common.Common_Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.net.ssl.HttpsURLConnection;

public class micOCRMicrosoft extends AsyncTask<String, Long, String> {

	private static final String TAG = micOCRMicrosoft.class.getName();
    private int randomkey;
	private boolean isOverlayRequired = false;
	private File mImageUrl;
	private String mLanguage;
	private Context mContext;
	private IOCRCallBack mIOCRCallBack;
	private boolean isTextAPI = false;
    private boolean errorOCRText = false;
    private boolean errorSendPost = false;
    private Common_Resource commonResource;

	public micOCRMicrosoft(Context mContext, boolean isOverlayRequired,
			File imageUrl, String language, boolean isTextAPI, IOCRCallBack iOCRCallBack) {
		this.mContext = mContext;
		commonResource = new Common_Resource(this.mContext);
		this.randomkey = randInt(0, commonResource.getListKeyOCR().length - 1);
		this.isOverlayRequired = isOverlayRequired;
		this.mImageUrl = imageUrl;
		this.mLanguage = language;
		this.mIOCRCallBack = iOCRCallBack;
		this.isTextAPI = isTextAPI;

	}

    private static final String[] ERROR_TEXT_API_LIST = {"j7elte","hero2lte","zeroflte","herolte",
            "j7xelte","zerolte","on7xelte","a5xelte","a3xelte","j7e3g","a5y17lte","zerofltevzw",
            "s5neolte","on5xelte","noblelte","zenlte","j7eltetmo","zerofltespr","nobleltevzw",
            "zeroflteatt","a3y17lte","zerofltetmo","marinelteatt","j7eltemtr","j3popeltemtr",
            "nobleltetmo","heroltebmc","a7y17lte","zerofltebmc","a7xelte","nobleltespr",
            "s5neoltecan","noblelteatt","zeroltetmo","zeroltevzw","zerolteatt","j5y17lte",
            "zenltevzw","zenltetmo","hero2ltebmc","j7y17lte","j3popeltetmo","zenlteatt",
            "zeroltespr","a5y17ltecan","zeroltebmc","j7popeltemtr","zeroflteusc","zerofltemtr",
            "zerofltetfnvzw","j3popelteatt","j7popeltetmo","zenltespr","gtaxlwifi","j3y17lte",
            "nobleltebmc","zeroflteaio","j7velte","a8xelte","noblelteusc","zerofltechn",
            "j7popelteatt","j7xeltecmcc","zeroltechn","gtaxllte","xcover4lte","j3popelteaio",
            "noblelteskt","nobleltehk","on7elte","zeroflteskt","zerolteusc","nobleltelgt",
            "gvlteatt","nobleltektt","zeroflteacg","a7xeltextc","zerofltektt","j7xlte",
            "herolteskt","zerofltelra","zenltebmc","j7popelteaio","hero2lteskt","zerofltelgt",
            "zerolteskt","j5y17ltedx","a5xeltextc","heroltektt","noblelteacg","j3popeltetfntmo",
            "zerolteacg","heroltelgt","zeroltektt","hero2ltektt","zeroltelra","zenlteusc",
            "hero2ltelgt","nobleltelra","zeroltelgt","j3popelteue","j7xeltektt","dream2lte",
            "a3xeltekx","a8xelteskt","a7xeltektt","dreamlte","a5y17lteskt","j7popelteue",
            "gtanotexllte","zenlteskt","matisse10wifikx","a7xelteskt","a5y17ltektt","a7xeltelgt",
            "gtanotexlwifikx","a5xelteskt","a5y17ltelgt","gracelte","zenltektt","xcover4ltecan",
            "a5xeltektt","on7xelteskt","gracerlteskt","on7xeltelgt","zerofltexx","zenltelgt",
            "a5xeltelgt","on7xeltektt","gvwifiue","dreamlteks","gracerltektt","a7xeltecmcc",
            "a5xeltecmcc","gvltevzw","gracerltelgt","j5y17ltektt","j7popelteskt","zenltechn",
            "nobleltechn","j5y17ltelgt","a7y17lteskt","j5y17lteskt","j7y17ltektt","gracelteskt",
            "dream2qltesq","gtaxlltekx","gvlte","dream2qltechn","hero2ltexx","graceqltechn",
            "gtesveltevzw","heroltexx","j3y17ltelgt","gtanotexlltekx","graceltektt","nobleltedcm",
            "graceltelgt","j7popeltetfntmo","zeroltexx","zenltekx","dream2qltecan","zerofltectc",
            "j5y17ltextc","nobleltejv","graceltexx","zerofltedcm","graceqltetmo","shamu",
            "dream2lteks","zeroltesbm","j700lte","gvltexsp","f5121","gts210vewifi","a9xltechn",
            "so-02j","f5321","gts28vewifi","zerofltexx-user","gts210velte","asus_a001","cph1611",
            "kate","pb2pro","f5122","HWWAS-H","HWBLN-H"
    }; //update list 29112017
    //public boolean ERROR_TEXT_API = isErrorTextAPI();
    public boolean isErrorTextAPI(){
        String mDeviceName = android.os.Build.DEVICE;
        return Arrays.asList(ERROR_TEXT_API_LIST).contains(mDeviceName);
    }

    public String sendPostOCR(){
        try {
            Log.d("Random Key", randomkey + "");
            return sendPost(commonResource.getListKeyOCR()[randomkey], isOverlayRequired,
                    mImageUrl, mLanguage);
        } catch (Exception e) {
            e.printStackTrace();
            errorSendPost = true;
            return null;
        }
    }
	@Override
	protected String doInBackground(String... params) {
	    if(isTextAPI){
	        if(isErrorTextAPI()){
                isTextAPI = false;
                return sendPostOCR();
            }else {
                VisionTextDetect mTextDetect = new VisionTextDetect(mContext);
                String textResult = mTextDetect.getTextFromFile(mImageUrl);
                errorOCRText = mTextDetect.isErrorOCRText();
                if (errorOCRText) {
                    isTextAPI = false;
                    return sendPostOCR();
                } else
                    return textResult;
            }
        }else {
            return sendPostOCR();
        }

	}


	@Override
	protected void onPostExecute(String result) {
		//if (result != null) {
		    if(isTextAPI) {
                mIOCRCallBack.getOCRCallBackVisionOCR(result,errorOCRText);
            }else
		        mIOCRCallBack.getOCRCallBackResult(result,errorSendPost);
			Log.d("MicOCRMicrosoft", "Get OCR CallBack result");
			//Log.d(TAG, result);
		/*} else {
			Log.d(TAG, "response is NULL");
		}*/
        super.onPostExecute(result);
	}

	private static int randInt(int min, int max) {

		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

    private String sendPost(String apiKey, boolean isOverlayRequired,
                            File imageUrl, String language) throws Exception {

        String url = "https://api.ocr.space/parse/image";
        URL obj = new URL(url); // OCR API Endpoints
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();


        try {
            String boundary = "*****";
            MultipartUtility multipart = new MultipartUtility(url, "UTF-8",
                    boundary);
            multipart.addFormField("apikey", apiKey);
            multipart.addFormField("isOverlayRequired",
                    Boolean.toString(isOverlayRequired));
            multipart.addFormField("language", language);
            multipart.addFilePart("file", imageUrl);
            Log.d("send JSON", "Sending JSON to Server");

            con = multipart.execute();
        } catch (IOException ex) {
            Log.d("Error send post",ex.toString());
            errorSendPost = true;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        con.disconnect();
        // return result
        return String.valueOf(response);
    }


}
