package com.education.voicetranslator.ngonngu;

import com.education.voicetranslator.ngonngu.NgonNguIn;
import com.education.voicetranslator.ngonngu.NgonNguOut;

public class item_translate {
	private int id;
	private NgonNguIn mNgonNguIn;
	private NgonNguOut mNgonNguOut;
	private String lagin, lagout;
	private String textin, textout;
	private String docmauin, docmauout;

	/**
	 * @param id
	 * @param lagin
	 * @param lagout
	 * @param textin
	 * @param textout
	 * @param docmauin
	 * @param docmauout
	 */
	public item_translate(int id, String lagin, String lagout, String textin,
			String textout, String docmauin, String docmauout) {
		super();
		this.id = id;
		this.lagin = lagin;
		this.lagout = lagout;
		this.textin = textin;
		this.textout = textout;
		this.docmauin = docmauin;
		this.docmauout = docmauout;
	}

	public item_translate(int id,NgonNguIn ngonnguin,NgonNguOut ngonnguout){
	    this.id = id;
	    this.mNgonNguIn = ngonnguin;
	    this.mNgonNguOut = ngonnguout;
	    this.lagin = mNgonNguIn.getmLanguageName();
	    this.lagout = mNgonNguOut.getmLanguageName();
	    this.textin = mNgonNguIn.getmText();
	    this.textout = mNgonNguOut.getmText();
	    this.docmauin = mNgonNguIn.getmCodeDocmau();
	    this.docmauout = mNgonNguOut.getmCodeDocmau();
    }
	public item_translate() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NgonNguIn getmNgonNguIn() {
        return mNgonNguIn;
    }

    public void setmNgonNguIn(NgonNguIn mNgonNguIn) {
        this.mNgonNguIn = mNgonNguIn;
        this.lagin = mNgonNguIn.getmLanguageName();
        this.textin = mNgonNguIn.getmText();
        this.docmauin = mNgonNguIn.getmCodeDocmau();
    }

    public NgonNguOut getmNgonNguOut() {
        return mNgonNguOut;
    }

    public void setmNgonNguOut(NgonNguOut mNgonNguOut) {
        this.mNgonNguOut = mNgonNguOut;
        this.lagout = mNgonNguOut.getmLanguageName();
        this.textout = mNgonNguOut.getmText();
        this.docmauout = mNgonNguOut.getmCodeDocmau();
    }
	public String getDocmauin() {
		return docmauin;
	}

	public void setDocmauin(String docmauin) {
		this.docmauin = docmauin;
	}

	public String getDocmauout() {
		return docmauout;
	}

	public void setDocmauout(String docmauout) {
		this.docmauout = docmauout;
	}

	public item_translate(int id, String lagin, String lagout, String textin,
			String textout) {
		super();
		this.id = id;
		this.lagin = lagin;
		this.lagout = lagout;
		this.textin = textin;
		this.textout = textout;
	}


	public String getLagin() {
		return lagin;
	}

	public void setLagin(String lagin) {
		this.lagin = lagin;
	}

	public String getLagout() {
		return lagout;
	}

	public void setLagout(String lagout) {
		this.lagout = lagout;
	}

	public String getTextin() {
		return textin;
	}

	public void setTextin(String textin) {
		this.textin = textin;
	}

	public String getTextout() {
		return textout;
	}

	public void setTextout(String textout) {
		this.textout = textout;
	}

}
