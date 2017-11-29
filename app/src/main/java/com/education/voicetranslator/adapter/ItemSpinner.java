package com.education.voicetranslator.adapter;

public class ItemSpinner {

	String text;
	Integer imageId;

	public ItemSpinner(String text, Integer imageId) {
		this.text = text;
		this.imageId = imageId;
	}

	public String getText() {
		return text;
	}

	public Integer getImageId() {
		return imageId;
	}
}