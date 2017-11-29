/*
 * Copyright 2013 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.education.voicetranslator.translation;

/**
 * Language - an enum of language codes supported by the Yandex API
 */
public enum LanguageY {
	AUTODETECT(""), ALBANIAN("sq"), ARMENIAN("hy"), ARABIC("ar"), AZERBAIJANI(
			"az"), BELARUSIAN("be"), BENGALI("bn"), BULGARIAN("bg"), CATALAN(
			"ca"), CROATIAN("hr"), CZECH("cs"), DANISH("da"), DUTCH("nl"), ENGLISH(
			"en"), ESTONIAN("et"), FINNISH("fi"), FRENCH("fr"), GERMAN("de"), GEORGIAN(
			"ka"), GREEK("el"), HUNGARIAN("hu"), ITALIAN("it"), LATVIAN("lv"), LITHUANIAN(
			"lt"), MACEDONIAN("mk"), NORWEGIAN("no"), POLISH("pl"), PORTUGUESE(
			"pt"), ROMANIAN("ro"), RUSSIAN("ru"), SERBIAN("sr"), SLOVAK("sk"), SLOVENIAN(
			"sl"), SPANISH("es"), SWEDISH("sv"), TURKISH("tr"), UKRAINIAN("uk"), CHINESE(
			"zh"), TAGALOG("tl"), HEBREW("he"), HINDI("hi"), INDONESIAN("id"), JAPANESE(
			"ja"), KOREAN("ko"), MALAY("ms"), PERSIAN("fa"), THAI("th"), VIETNAMESE(
			"vi"), URDU("ur"), GUJARATI("gu"), TELUGU("te"), TAMIL("ta"), MARATHI(
			"mr"), PUNJABI("pa"), KANNADA("kn"), MALAYALAM("ml"), BURMESE("my"), NEPALI(
			"ne"), KHMER("km"), SINHALA("si"), LAO("lo");

	/**
	 * String representation of this language.
	 */
	private final String language;

	/**
	 * Enum constructor.
	 * 
	 * @param pLanguage
	 *            The language identifier.
	 */
    LanguageY(final String pLanguage) {
		language = pLanguage;
	}

	public static LanguageY fromString(final String pLanguage) {
		for (LanguageY l : values()) {
			if (l.toString().equals(pLanguage)) {
				return l;
			}
		}
		return null;
	}

	/**
	 * Returns the String representation of this language.
	 * 
	 * @return The String representation of this language.
	 */
	@Override
	public String toString() {
		return language;
	}

}
