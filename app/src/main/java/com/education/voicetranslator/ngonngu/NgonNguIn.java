package com.education.voicetranslator.ngonngu;

import android.content.Context;

import com.education.voicetranslator.common.Common_Resource;
import com.education.voicetranslator.translation.LanguageY;

import java.util.Arrays;

/**
 * Created by Administrator on 27/11/2017.
 */

public class NgonNguIn extends NgonNgu {
    private Context mContext;
    private String mCodeSpeech;
    private String mCodeOCR;
    private String mText;
    private String mLanguageName;
    private String mCodeLanguage;
    private String mCodeDocmau;
    private boolean isOcrTextAPI = false;
    private boolean isSpeechRecognition = true;
    private boolean isTextToSpeech = true;
    private int mPosition;

    public NgonNguIn(Context mContext, String mLanguageName) {
        super(mLanguageName);
        this.mContext = mContext;
        this.mLanguageName = mLanguageName;
        Common_Resource commonResource = new Common_Resource(mContext);
        mPosition = commonResource.getPosition(this.mLanguageName,commonResource.getContriesin());
        this.mCodeDocmau = commonResource.getCodelangin()[mPosition];
        this.mCodeSpeech = commonResource.getFromLanguageName(mLanguageName,
                commonResource.getContriesin(), commonResource.getCodespeech());
        this.mCodeOCR = commonResource.getFromLanguageName(mLanguageName,
               commonResource.getCameraOcr(), commonResource.getCodeocr());
        this.mCodeLanguage = commonResource.getFromLanguageName(mLanguageName,
                commonResource.getContriesin(),commonResource.getCodelangin());
        this.mText = "";
        if(!getmCodeOCR().isEmpty())
            this.isOcrTextAPI = Arrays.asList(commonResource.getOcrTextAPI()).contains(this.mLanguageName);
        this.isSpeechRecognition = !Arrays.asList(commonResource.getNovoice()).contains(this.mLanguageName);
        this.isTextToSpeech = !Arrays.asList(commonResource.getNoSpeaker()).contains(this.mLanguageName);
        if(this.getLanguageY().equals(LanguageY.TAGALOG))
            this.mCodeDocmau = "fil";
    }

    public String getmCodeSpeech() {
        return mCodeSpeech;
    }

    public String getmCodeOCR() {
        return mCodeOCR;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    @Override
    public String getmLanguageName() {
        return mLanguageName;
    }

    @Override
    public void setmLanguageName(String mLanguageName) {
        this.mLanguageName = mLanguageName;
    }

    public String getmCodeDocmau() {
        return mCodeDocmau;
    }

    public void setmCodeDocmau(String mCodeDocmau) {
        this.mCodeDocmau = mCodeDocmau;
    }

    public String getmCodeLanguage() {
        return mCodeLanguage;
    }

    public void setmCodeLanguage(String mCodeLanguage) {
        this.mCodeLanguage = mCodeLanguage;
    }

    public boolean isTextToSpeech() {
        return isTextToSpeech;
    }

    public void updateNgonNguIn(String mLanguageName){
        this.mLanguageName = mLanguageName;
        Common_Resource commonResource = new Common_Resource(mContext);
        mPosition = commonResource.getPosition(this.mLanguageName,commonResource.getContriesin());
        this.mCodeDocmau = commonResource.getCodelangin()[mPosition];
        if(this.getLanguageY().equals(LanguageY.TAGALOG))
            this.mCodeDocmau = "fil";
        this.mCodeSpeech = commonResource.getFromLanguageName(mLanguageName,
                commonResource.getContriesin(), commonResource.getCodespeech());
        this.mCodeOCR = commonResource.getFromLanguageName(mLanguageName,
                commonResource.getCameraOcr(), commonResource.getCodeocr());
        this.mCodeLanguage = commonResource.getFromLanguageName(mLanguageName,
                commonResource.getContriesin(),commonResource.getCodelangin());
        if(!getmCodeOCR().isEmpty())
            this.isOcrTextAPI = Arrays.asList(commonResource.getOcrTextAPI()).contains(this.mLanguageName);
        this.isSpeechRecognition = !Arrays.asList(commonResource.getNovoice()).contains(this.mLanguageName);
    }
    public LanguageY getLanguageY(){
        return LanguageY.fromString(this.getmCodeLanguage());
    }

    public boolean isOCRTextAPI(){
        return this.isOcrTextAPI;
    }

    public boolean isSpeechRecognition() {
        return isSpeechRecognition;
    }

    public int getmPosition() {
        return mPosition;
    }
}
