package com.education.voicetranslator.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Administrator on 25/11/2017.
 */

public class Common_Preferences {
    private SharedPreferences mPreference;
    private SharedPreferences.Editor mEditor;
    private final static String MAIN_NAME_PREFERENCE = "quanly_request";
    private final static String SO_REQUEST_NAME = "sorequest";
    private final static String RATED_NAME = "rated";
    private final static String MUCH_ADS = "muchads";
    private final static String NEW_LANGUAGE = "newlang_speech";
    private final static String IN_APP  = "inapp";
    private final static String TRANSLATE_CAMERA = "translate_camera";
    private final static String NO_ADS = "noads";
    /*private final static String LANGUAGE_IN = "LanguageIn";
    private final static String LANGUAGE_OUT = "LanguageOut";*/
    private final static String CONTRY_NAME_IN = "contrynamein";
    private final static String CONTRY_NAME_OUT = "contrynameout";
    private final static String CURRENT_PHOTO_PATH="photopath";

    private int mSoRequest;
    private boolean mRated;
    private boolean mMuchAds;
    private boolean mNewLanguage;
    private int mInApp;
    private boolean mTranslateCamera;
    private boolean mNoAds;
    //private int mLanguageIn,mLanguageOut;
    private String mContryNameIn,mContryNameOut;
    private String mCurrentPhotoPath;

    public Common_Preferences(Context context) {
        mPreference = context.getSharedPreferences(MAIN_NAME_PREFERENCE, context.MODE_PRIVATE);
        mEditor = mPreference.edit();
        this.getAllValue();
    }

    public void getAllValue(){
        mSoRequest = mPreference.getInt(SO_REQUEST_NAME,0);
        mRated = mPreference.getBoolean(RATED_NAME,false);
        mMuchAds = mPreference.getBoolean(MUCH_ADS,false);
        mNewLanguage = mPreference.getBoolean(NEW_LANGUAGE, false);
        mInApp = mPreference.getInt(IN_APP,0);
        mTranslateCamera = mPreference.getBoolean(TRANSLATE_CAMERA,false);
        mNoAds = mPreference.getBoolean(NO_ADS,false);
        /*mLanguageIn = mPreference.getInt(LANGUAGE_IN,0);
        mLanguageOut = mPreference.getInt(LANGUAGE_OUT,0);*/
        mContryNameIn = mPreference.getString(CONTRY_NAME_IN,"English");
        mContryNameOut = mPreference.getString(CONTRY_NAME_OUT,"Hindi");
        mCurrentPhotoPath = mPreference.getString(CURRENT_PHOTO_PATH,"");
    }


    public void setAllValue(int mSoRequest, boolean mRated, boolean mMuchAds, boolean mNewLanguage,
                            int mInApp, boolean mTranslateCamera, boolean mNoAds,String mContryNameIn,
                            String mContryNameOut,String mCurrentPhotoPath) {

        this.mSoRequest = mSoRequest;
        this.mRated = mRated;
        this.mMuchAds = mMuchAds;
        this.mNewLanguage = mNewLanguage;
        this.mInApp = mInApp;
        this.mTranslateCamera = mTranslateCamera;
        this.mNoAds = mNoAds;
        /*this.mLanguageIn = mLanguageIn;
        this.mLanguageOut = mLanguageOut;*/
        this.mContryNameIn = mContryNameIn;
        this.mContryNameOut = mContryNameOut;
        this.mCurrentPhotoPath = mCurrentPhotoPath;

        mEditor.putInt(SO_REQUEST_NAME,this.mSoRequest);
        mEditor.putBoolean(RATED_NAME,this.mRated);
        mEditor.putBoolean(MUCH_ADS,this.mMuchAds);
        mEditor.putBoolean(NEW_LANGUAGE,this.mNewLanguage);
        mEditor.putInt(IN_APP,this.mInApp);
        mEditor.putBoolean(TRANSLATE_CAMERA,this.mTranslateCamera);
        mEditor.putBoolean(NO_ADS,this.mNoAds);
        /*mEditor.putInt(LANGUAGE_IN,this.mLanguageIn);
        mEditor.putInt(LANGUAGE_OUT,this.mLanguageOut);*/
        mEditor.putString(CONTRY_NAME_IN,this.mContryNameIn);
        mEditor.putString(CONTRY_NAME_OUT,this.mContryNameOut);
        mEditor.putString(CURRENT_PHOTO_PATH,this.mCurrentPhotoPath);
        mEditor.apply();
    }



    public int getmSoRequest() {
        return mSoRequest;
    }

    public void setmSoRequest(int mSoRequest) {
        this.mSoRequest = mSoRequest;
        mEditor.putInt(SO_REQUEST_NAME,this.mSoRequest);
        mEditor.apply();
    }

    public boolean ismRated() {
        return mRated;
    }

    public void setmRated(boolean mRated) {
        this.mRated = mRated;
        mEditor.putBoolean(RATED_NAME,this.mRated);
        mEditor.apply();
    }

    public boolean ismMuchAds() {
        return mMuchAds;
    }

    public void setmMuchAds(boolean mMuchAds) {
        this.mMuchAds = mMuchAds;
        mEditor.putBoolean(MUCH_ADS,this.mMuchAds);
        mEditor.apply();
    }

    public boolean ismNewLanguage() {
        return mNewLanguage;
    }

    public void setmNewLanguage(boolean mNewLanguage) {
        this.mNewLanguage = mNewLanguage;
        mEditor.putBoolean(NEW_LANGUAGE,this.mNewLanguage);
        mEditor.apply();
    }

    public int getmInApp() {
        return mInApp;
    }

    public void setmInApp(int mInApp) {
        this.mInApp = mInApp;
        mEditor.putInt(IN_APP,this.mInApp);
        mEditor.apply();
    }

    public boolean ismTranslateCamera() {
        return mTranslateCamera;
    }

    public void setmTranslateCamera(boolean mTranslateCamera) {
        this.mTranslateCamera = mTranslateCamera;
        mEditor.putBoolean(TRANSLATE_CAMERA,this.mTranslateCamera);
        mEditor.apply();
    }

    public boolean ismNoAds() {
        return mNoAds;
    }

    public void setmNoAds(boolean mNoAds) {
        this.mNoAds = mNoAds;
        mEditor.putBoolean(NO_ADS,this.mNoAds);
        mEditor.apply();
    }

    /*public int getmLanguageIn() {
        return mLanguageIn;
    }

    public void setmLanguageIn(int mLanguageIn) {
        this.mLanguageIn = mLanguageIn;
        mEditor.putInt(LANGUAGE_IN,this.mLanguageIn);
        mEditor.apply();
    }

    public int getmLanguageOut() {
        return mLanguageOut;
    }

    public void setmLanguageOut(int mLanguageOut) {
        this.mLanguageOut = mLanguageOut;
        mEditor.putInt(LANGUAGE_OUT,this.mLanguageOut);
        mEditor.apply();
    }*/

    public String getmContryNameIn() {
        return mContryNameIn;
    }

    public void setmContryNameIn(String mContryNameIn) {
        this.mContryNameIn = mContryNameIn;
        mEditor.putString(CONTRY_NAME_IN,this.mContryNameIn);
        mEditor.apply();
    }

    public String getmContryNameOut() {
        return mContryNameOut;
    }

    public void setmContryNameOut(String mContryNameOut) {
        this.mContryNameOut = mContryNameOut;
        mEditor.putString(CONTRY_NAME_OUT,this.mContryNameOut);
        mEditor.apply();
    }

    public String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setmCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
        mEditor.putString(CURRENT_PHOTO_PATH,this.mCurrentPhotoPath);
        mEditor.apply();
    }
}
