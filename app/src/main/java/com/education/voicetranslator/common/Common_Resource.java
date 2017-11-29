package com.education.voicetranslator.common;

import android.content.Context;
import android.content.res.TypedArray;

import com.education.voicetranslator.R;

/**
 * Created by Administrator on 27/11/2017.
 */

public class Common_Resource {
    private Context mcontext;
    private String[] contriesout, contriesin, codeocr, cameraocr, novoice,
            codelangin, codelangout,codespeech,ocrTextAPI, noSpeaker, helloWelcome,
            listKeyOCR, listKeyYTranslate;
    private TypedArray flagcontriesin;
    private TypedArray flagcontriesout;
    private TypedArray flagcontriesocr;
    public Common_Resource(Context context) {
        this.mcontext = context;
    }

    // Repair all resource before use
    public void repairAllResource(){
        getContriesout();
        getContriesin();
        getCodeocr();
        getCameraOcr();
        getNovoice();
        getCodelangin();
        getCodelangout();
        getCodespeech();
        getOcrTextAPI();
        getNoSpeaker();
        getHelloWelcome();
        getListKeyOCR();
        getListKeyYTranslate();
        getFlagcontriesin();
        getFlagcontriesout();
        getFlagcontriesocr();
    }
    public void recycleIdResource(){
        getFlagcontriesin().recycle();
        getFlagcontriesout().recycle();
        getFlagcontriesocr().recycle();
    }
    public String[] getContriesout() {
        if(contriesout==null)
            contriesout = mcontext.getResources().getStringArray(R.array.str_language_out);
        return contriesout;
    }

    public String[] getContriesin() {
        if(contriesin==null)
            contriesin = mcontext.getResources().getStringArray(R.array.str_language_in);
        return contriesin;
    }

    public String[] getCodeocr() {
        if(codeocr==null)
            codeocr = mcontext.getResources().getStringArray(R.array.Ocr_Code);
        return codeocr;
    }

    public String[] getCameraOcr() {
        if(cameraocr==null)
            cameraocr = mcontext.getResources().getStringArray(R.array.cameraOcr);
        return cameraocr;
    }

    public String[] getNovoice() {
        if(novoice==null)
            novoice = mcontext.getResources().getStringArray(R.array.noVoice);
        return novoice;
    }

    public String[] getCodelangin() {
        if(codelangin==null)
            codelangin = mcontext.getResources().getStringArray(R.array.str_code_language_in);
        return codelangin;
    }

    public String[] getCodelangout() {
        if(codelangout==null)
            codelangout = mcontext.getResources().getStringArray(R.array.str_code_language_out);
        return codelangout;
    }

    public String[] getCodespeech() {
        if(codespeech==null)
            codespeech = mcontext.getResources().getStringArray(R.array.str_code_speech_in);
        return codespeech;
    }

    public String[] getOcrTextAPI() {
        if(ocrTextAPI==null)
            ocrTextAPI = mcontext.getResources().getStringArray(R.array.Ocr_TextAPI);
        return ocrTextAPI;
    }

    public String[] getNoSpeaker() {
        if(noSpeaker == null)
            noSpeaker = mcontext.getResources().getStringArray(R.array.noSpeaker);
        return noSpeaker;
    }

    public String[] getHelloWelcome() {
        if(helloWelcome ==null)
            helloWelcome = mcontext.getResources().getStringArray(R.array.hello_welcome);
        return helloWelcome;
    }

    public void setHelloWelcome(String[] helloWelcome) {
        this.helloWelcome = helloWelcome;
    }

    public String[] getListKeyOCR() {
        if(listKeyOCR ==null)
            listKeyOCR = mcontext.getResources().getStringArray(R.array.OCRKeyList);
        return listKeyOCR;
    }

    public void setListKeyOCR(String[] listKeyOCR) {
        this.listKeyOCR = listKeyOCR;
    }

    public String[] getListKeyYTranslate() {
        if(listKeyYTranslate ==null)
            listKeyYTranslate = mcontext.getResources().getStringArray(R.array.YTranslateKeyList);
        return listKeyYTranslate;
    }

    public void setListKeyYTranslate(String[] listKeyYTranslate) {
        this.listKeyYTranslate = listKeyYTranslate;
    }

    public TypedArray getFlagcontriesin() {
        if(flagcontriesin==null)
            flagcontriesin = mcontext.getResources().obtainTypedArray(R.array.id_language_in);
        return flagcontriesin;
    }

    public TypedArray getFlagcontriesout() {
        if(flagcontriesout==null)
            flagcontriesout = mcontext.getResources().obtainTypedArray(R.array.id_language_out);
        return flagcontriesout;
    }

    public TypedArray getFlagcontriesocr() {
        if(flagcontriesocr==null)
            flagcontriesocr = mcontext.getResources().obtainTypedArray(R.array.Ocr_Flag);
        return flagcontriesocr;
    }

    public String getFromLanguageName(String mLanguageName,String[] ArrayLanugage,String[] ArrayGetValue){
        for(int i = 0;i< ArrayLanugage.length;i++){
            if(mLanguageName.equalsIgnoreCase(ArrayLanugage[i]))
                return ArrayGetValue[i];
        }
        return "";
    }
    public int getPosition(String mInput, String[] arrayContain){
        for(int i = 0; i< arrayContain.length;i++){
            if(mInput.equalsIgnoreCase(arrayContain[i]))
                return i;
        }
        return getPosition("English",arrayContain);
    }

}
