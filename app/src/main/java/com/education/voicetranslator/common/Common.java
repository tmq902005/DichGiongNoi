package com.education.voicetranslator.common;

import android.os.Environment;

import java.util.Arrays;

/**
 * Created by Administrator on 28/11/2017.
 */

public class Common {
    public static final String APPICATION_PATH = Environment.
            getExternalStorageDirectory().getAbsolutePath()
            + "/VoiceTranslator/";
    public static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2NnCGSu7QvSNHLG+"
            + "boyjKt9JiYw7wN5883qbBTYm2BxYldvoRX3NZafuM/NP5N4LJUW+l9M9W2OlOTp0WAH2Ukm0J/6kV528SippmdV4/zL2LfafyR8XzaR"
            + "N4QgCvP0ol7L3VxG3Oher3ShDrSBH/2Zf6puT0wSDZgQ5goOlTPHHLtXR2X+w0qBhCCiD8nzm1jeGf9VIXd8Y9r9/5G03gEf8teoe/oN"
            + "wcpWPKc5409CcsDUu4BM/dDFHe5WdRy5c77AA/z3gOE8afJnqh31GxMJgLJ5io2WtiSUjDCB1cNf1hJ7MN6oDugxVCDV6VssIoGq6r6gR"
            + "8pdYrPJr3J7Z6QIDAQAB";
    public static final String CALLBACK_TEXT = "TEXT";
    public static final String CALLBACK_SPEECH = "SPEECH";
    public static final String CALLBACK_CAMERA = "CAMERA";
    public static int CAMERA_REQUEST = 400;
    public static int CROPING_CODE = 401;
    public static int RESULT_LOAD_IMAGE = 402;
    public static int SPEECH_WAIT = 100;
    public static int DELAY_TIME_DOUBLE_BACK_PRESS = 2000;
    public static final int IMAGE_MAX_SIZE = 700;
    public static final String GOOGLE_INTERSTITIAL_ADS = "1757797214490189_1969585669978008";
}
