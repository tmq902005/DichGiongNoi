package com.education.voicetranslator.ngonngu;

import android.content.Context;

import com.education.voicetranslator.common.Common_Resource;
import com.education.voicetranslator.translation.LanguageY;

import java.util.Arrays;

/**
 * Created by Administrator on 27/11/2017.
 */

public class NgonNguOut extends NgonNgu {
    private Context mContext;
    private String mText;
    private String mLanguageName;
    private String mCodeLanguage;
    private String mCodeDocmau;
    private boolean isTextToSpeech = true;
    private int mPosition;

    public NgonNguOut(Context mContext, String mLanguageName) {
        super(mLanguageName);
        this.mContext = mContext;
        this.mLanguageName = mLanguageName;
        Common_Resource commonResource = new Common_Resource(mContext);
        this.mPosition = commonResource.getPosition(this.mLanguageName,commonResource.getContriesout());
        this.mCodeDocmau = commonResource.getCodelangout()[mPosition];
        this.mCodeLanguage = commonResource.getFromLanguageName(mLanguageName,
                commonResource.getContriesout(),commonResource.getCodelangout());
        this.mText = "";
        if(this.getLanguageY().equals(LanguageY.TAGALOG))
            this.mCodeDocmau = "fil";
        this.isTextToSpeech = !Arrays.asList(commonResource.getNoSpeaker()).contains(this.mLanguageName);
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

    public void updateNgonNguOut(String mLanguageName){
        this.mLanguageName = mLanguageName;
        Common_Resource commonResource = new Common_Resource(mContext);
        this.mPosition = commonResource.getPosition(this.mLanguageName,commonResource.getContriesout());
        this.mCodeDocmau = commonResource.getCodelangout()[mPosition];
        if(this.getLanguageY().equals(LanguageY.TAGALOG))
            this.mCodeDocmau = "fil";
        this.mCodeLanguage = commonResource.getFromLanguageName(mLanguageName,
                commonResource.getContriesout(),commonResource.getCodelangout());
    }
    public LanguageY getLanguageY(){
        return LanguageY.fromString(this.getmCodeLanguage());
    }

    public int getmPosition() {
        return mPosition;
    }
}
