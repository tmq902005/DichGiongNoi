package com.education.voicetranslator;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
//import java.util.Random;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ClipDrawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.Vibrator;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.education.voicetranslator.adapter.Adapter_Translator;
import com.education.voicetranslator.adapter.ItemSpinner;
import com.education.voicetranslator.adapter.SpinnerAdapter;
import com.education.voicetranslator.callback.DownloadImageCallBack;
import com.education.voicetranslator.callback.IOCRCallBack;
import com.education.voicetranslator.callback.ResizeImageCallBack;
import com.education.voicetranslator.callback.YTranslatorCallBack;
import com.education.voicetranslator.common.Common;
import com.education.voicetranslator.common.Common_Preferences;
import com.education.voicetranslator.common.Common_Resource;
import com.education.voicetranslator.data.DataTranslate;
import com.education.voicetranslator.mic.DownloadImage;
import com.education.voicetranslator.mic.Resizeimage;
import com.education.voicetranslator.mic.micOCRMicrosoft;
import com.education.voicetranslator.mic.micTranslatorYandex;
import com.education.voicetranslator.ngonngu.NgonNguIn;
import com.education.voicetranslator.ngonngu.NgonNguOut;
import com.education.voicetranslator.ngonngu.item_translate;
import com.education.voicetranslator.translation.LanguageY;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements
        BillingProcessor.IBillingHandler, IOCRCallBack, YTranslatorCallBack {
    private ImageView imgHoanDoi;
    private FrameLayout fLayoutSpeech;
    private ProgressBar proTalk, proText;
    private Spinner mSpinnerOut;
    private Spinner mSpinnerIn;
    private ToggleButton mToggleSpeech;
    private Button btnPay2Dolar, btnRestartApp;
    private SpeechRecognizer mSpeechReco;
    private Intent recognizerIntent;
    private Toast mThongBao;
    private NgonNguIn mNgonNguIn;
    private NgonNguOut mNgonNguOut;
    private String mCurrentPhotoPath;
    private boolean isSpeechRecoAvalable = false;
    private InterstitialAd interstitialAd;
    private SharedPreferences pre_permissions;
    private boolean overlaysPermission;
    private Common_Preferences mCommon_Preferences;
    private int countRequest;
    private boolean isRated, isNoAds, isMuchAds;
    private EditText et_Translate;
    private ImageView img_Translate;
    private ImageView img_DeleteText;
    private ImageView img_ClearTranslation;
    private ImageView img_QTranslate;
    private ImageView img_Camera;
    private ImageView img_Info;
    private ImageView img_Speaker;
    private ImageView img_Gallery;
    private Common_Resource commonResource = new Common_Resource(this);
    private ListView lv_Tranlation;
    private Adapter_Translator adapter_Translation;
    private Vibrator vib;
    private Long freeMemory;
    public boolean isSoftKeyboardDisplayed = false;
    private int mLevel = 0;
    private int fromLevel = 0;
    private int toLevel = 0;
    private ClipDrawable mImageDrawable;
    private boolean isSpeeching = false;
    private boolean isCameraTranslate = false;
    private boolean isNewLang = true;
    public static final int MAX_LEVEL = 10000;
    public static final int LEVEL_DIFF = 100;
    public static final int DELAY = 10;
    private YTranslatorCallBack mYCallBack = this;
    private Handler mUpHandler = new Handler();
    private Runnable animateUpImage = new Runnable() {

        @Override
        public void run() {
            doTheUpAnimation(fromLevel, toLevel);
        }
    };

    private Handler mDownHandler = new Handler();
    private Runnable animateDownImage = new Runnable() {

        @Override
        public void run() {
            doTheDownAnimation(fromLevel, toLevel);
        }
    };
    private InputMethodManager imm;
    private AdView mAdView;
    private DataTranslate db;
    private AudioManager amThanh;
    private int amLuong;
    private ArrayList<String> permissionsToRequest;
    private boolean isTTSAvalable = true;
    private Dialog dialogResult;
    // private ArrayList<String> permissionsRejected;
    private String[] listPermission = {"android.permission.RECORD_AUDIO",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            //"android.permission.CAMERA",
            //"android.permission.ACTION_MANAGE_OVERLAY_PERMISSION"
            };// ,"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"

    private int thoiGianCho = Common.SPEECH_WAIT;
    private RecognitionListener mRecoListener = new RecognitionListener() {

        @Override
        public void onReadyForSpeech(Bundle params) {
            // TODO Auto-generated method stub
            thoiGianCho = Common.SPEECH_WAIT;
            Log.d("speechtotext", "onReadyForSpeech");
            mToggleSpeech.setVisibility(View.GONE);
            fLayoutSpeech.setVisibility(View.VISIBLE);
            isSpeeching = true;
            Log.d("speechtotext repeat", "" + isSpeeching);
            showToast("Let's Speak", Toast.LENGTH_LONG);

        }

        @Override
        public void onBeginningOfSpeech() {
            // TODO Auto-generated method stub
            Log.d("speechtotext", "onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            isSpeeching = true;
            // TODO Auto-generated method stub
            Log.i("speechtotext", "onRmsChanged: " + rmsdB);

            float dB = rmsdB;
            if (dB < 0) {
                thoiGianCho--;
                if (thoiGianCho <= 0) {
                    this.onError(SpeechRecognizer.ERROR_CLIENT);
                }
            }
            if (rmsdB < 0) {
                rmsdB = 3;
            }
            if (rmsdB > 0) {
                int temp_level = (int) ((rmsdB * MAX_LEVEL) / 10);

                if (toLevel == temp_level || temp_level > MAX_LEVEL) {
                    return;
                }
                toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
                if (toLevel > fromLevel) {
                    // cancel previous process first
                    mDownHandler.removeCallbacks(animateDownImage);
                    MainActivity.this.fromLevel = toLevel;
                    mUpHandler.post(animateUpImage);
                } else {
                    // cancel previous process first
                    mUpHandler.removeCallbacks(animateUpImage);
                    MainActivity.this.fromLevel = toLevel;
                    mDownHandler.post(animateDownImage);
                }
            }
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // TODO Auto-generated method stub
            Log.d("speechtotext", "onBufferReceiverd");
        }

        @Override
        public void onEndOfSpeech() {
            // TODO Auto-generated method stub
            fLayoutSpeech.setVisibility(View.GONE);
            if (isSpeeching) {
                proTalk.setVisibility(View.VISIBLE);
            }
            Log.d("speechtotext", "onEndOfSpeech");
        }

        @Override
        public void onError(int errorCode) {
            // TODO Auto-generated method stub
            vib.vibrate(40);
            if (!isSpeeching) {
                mSpeechReco.startListening(recognizerIntent);
                Log.d("speechtotext", "bị Repeat nè .....");
            } else {
                mToggleSpeech.setChecked(false);
                mToggleSpeech.setVisibility(View.VISIBLE);
                proTalk.setVisibility(View.GONE);
                if (errorCode == 3
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialogSettingGoogleApp().show();
                } else {
                    String errorMessage = getErrorText(errorCode);
                    Log.d("speechtotext", "FAILED " + errorMessage);
                    showToast(errorMessage, Toast.LENGTH_LONG);
                    isSpeeching = false;
                }
                mSpeechReco.destroy();
            }
        }

        @Override
        public void onResults(Bundle results) {
            // TODO Auto-generated method stub
            Log.d("speechtotext", "onResults");
            isSpeeching = false;
            mToggleSpeech.setChecked(false);
            // mToggleSpeech.setVisibility(View.INVISIBLE);
            proTalk.setVisibility(View.VISIBLE);
            ArrayList<String> lisresult = results
                    .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			if(lisresult!=null)
            if (lisresult.size() == 1) {
                mNgonNguIn.setmText(lisresult.get(0));
                translateWithSpeech();
            } else {
                dialogChonKetQua(lisresult).show();
            }
            mSpeechReco.destroy();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // TODO Auto-generated method stub

        }
    };
    private BillingProcessor mBill;
    String strNoAds = "";
    private Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		/*
		 * this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        //FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Obtain the shared Tracker instance.
        GoogleAnalytics sAnalytics = GoogleAnalytics.getInstance(this);
        if(mTracker==null){
            mTracker = sAnalytics.newTracker(R.xml.app_tracker);
        }
        mCommon_Preferences = new Common_Preferences(this);
        isNoAds = mCommon_Preferences.ismNoAds();

        mBill = new BillingProcessor(getBaseContext(), Common.base64EncodedPublicKey,
                "07718416122122078249", this);
        Log.d("NoAds Version From BP",
                " " + mBill.isPurchased("hotronhaphattrien"));
        isNoAds = mBill.isPurchased("hotronhaphattrien");
        // kiemtraNoads();
        Log.d("NoAds Version From", " " + isNoAds);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow()
                    .addFlags(
                            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(
                    getResources().getColor(R.color.maincolor));
            getWindow().setNavigationBarColor(
                    getResources().getColor(R.color.maincolor));
        }
        pre_permissions = getSharedPreferences("request_permissions",
                MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> perms = new ArrayList<String>(
                    Arrays.asList(listPermission));
            int permsRequestCode = 200;
            permissionsToRequest = findUnAskedPermissions(perms);
            if (permissionsToRequest.size() > 0) {
                requestPermissions(
                        permissionsToRequest.toArray(new String[permissionsToRequest
                                .size()]), permsRequestCode);
            }
        }


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            overlaysPermission = Settings.canDrawOverlays(this);
        else
            overlaysPermission = true;



        mAdView = findViewById(R.id.adView);
        btnPay2Dolar = findViewById(R.id.btn_pay2dolar);
        btnRestartApp = findViewById(R.id.btn_restartApp);
        //isNoAds = false;
        if (isNoAds) {
            mAdView.setVisibility(View.GONE);
            btnPay2Dolar.setVisibility(View.GONE);
            strNoAds = " NoAds";
        } else {
            AdRequest.Builder builder = new AdRequest.Builder();
            AdRequest adRequest = builder.build();
            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // TODO Auto-generated method stub
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                }
            });
            interstitialAd = new InterstitialAd(this,Common.GOOGLE_INTERSTITIAL_ADS);
            interstitialAd.setAdListener(new AbstractAdListener() {

                @Override
                public void onAdLoaded(Ad ad) {
                    super.onAdLoaded(ad);
                }

            });
            interstitialAd.loadAd();
        }

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // tạo folder chua file audio

        File appTmpPath = new File(Common.APPICATION_PATH);
        if (!appTmpPath.isDirectory()) {
            appTmpPath.mkdir();
            appTmpPath.setWritable(true, false);
        }

        freeMemory = appTmpPath.getFreeSpace();
        Log.d("Free Space", "" + freeMemory);
        img_Translate = findViewById(R.id.img_translate);
        img_DeleteText = findViewById(R.id.img_deleteText);
        et_Translate = findViewById(R.id.et_translate);
        ImageView img_resize_et = findViewById(R.id.img_resize_et);
        img_Speaker = findViewById(R.id.img_speaker);
        img_Gallery = findViewById(R.id.imgicongallery);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mToggleSpeech = findViewById(R.id.togglevoid);
        ImageView img = findViewById(R.id.imgvoid);
        img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                fLayoutSpeech.setVisibility(View.GONE);
                mToggleSpeech.setVisibility(View.VISIBLE);
                mToggleSpeech.setChecked(false);
            }
        });

        img_ClearTranslation = findViewById(R.id.imgclear);
        img_QTranslate = findViewById(R.id.imgiconqtrans);
        img_Camera = findViewById(R.id.imgcamera);
        img_Info = findViewById(R.id.imginfo);
        // Hieu chinh am thanh
        amThanh = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        amLuong = amThanh.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (amLuong == 0) {
            img_Speaker.setImageResource(R.drawable.speaker_icon_no);
        }
        btnPay2Dolar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vib.vibrate(30);
                Animation animationscale = AnimationUtils.loadAnimation(
                        v.getContext(), R.anim.anim_scale);
                btnPay2Dolar.startAnimation(animationscale);
                clickPay2Dolar();
                btnPay2Dolar.setVisibility(View.GONE);
                btnRestartApp.setVisibility(View.VISIBLE);

            }
        });
        btnRestartApp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                restartApp();
            }
        });
        img_QTranslate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vib.vibrate(30);
                Animation animationscale = AnimationUtils.loadAnimation(
                        v.getContext(), R.anim.anim_scale);
                img_QTranslate.startAnimation(animationscale);
                final Intent intent = new Intent(MainActivity.this,
                        ChatHeadService.class);
                final Intent intentsetting = new Intent(
                        MainActivity.this, SettingActivity.class);
                if (overlaysPermission) {
                    intent.putExtra("inputtext", "");
                    intent.putExtra("onservice", true);
                    intent.putExtra("turnoff", false);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if(!isFinishing()) {
                        PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        try {
                            pendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }else{
                        showToast("Quick Translate Service is not Ready, Please try again!",Toast.LENGTH_LONG);
                    }
                    //startService(intent);
                } else {

                    PendingIntent pendingIntent2 = PendingIntent.getActivity(MainActivity.this,1,intentsetting,PendingIntent.FLAG_UPDATE_CURRENT);
                    try {
                        pendingIntent2.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                    //startActivity(intentsetting);
                }


            }
        });
        //mIOCRCallBack = this;
        img_Info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vib.vibrate(30);
                Animation animationscale = AnimationUtils.loadAnimation(
                        v.getContext(), R.anim.anim_scale);
                img_Info.startAnimation(animationscale);
                Intent infoIntent = new Intent(MainActivity.this,
                        InfoActivity.class);
                startActivity(infoIntent);
                /*animationscale.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {



                    }
                });*/
            }
        });

        img_Speaker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vib.vibrate(30);
                Animation animationscale = AnimationUtils.loadAnimation(
                        v.getContext(), R.anim.anim_scale);
                img_Speaker.startAnimation(animationscale);
                dialogVolumeControl().show();
                /*animationscale.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }
                });*/

            }
        });

        img_Gallery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean ktquyentruycapcamera = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ArrayList<String> perms = new ArrayList<String>(Arrays
                            .asList(listPermission));
                    int permsRequestCode = 200;
                    permissionsToRequest = findUnAskedPermissions(perms);
                    if (permissionsToRequest.size() > 0) {
                        requestPermissions(
                                permissionsToRequest
                                        .toArray(new String[permissionsToRequest
                                                .size()]), permsRequestCode);
                    } else {
                        ktquyentruycapcamera = true;
                    }
                } else {
                    ktquyentruycapcamera = true;
                }

                vib.vibrate(30);
                if (ktquyentruycapcamera) {
                    Animation animationscale = AnimationUtils.loadAnimation(
                            v.getContext(), R.anim.anim_scale);
                    img_Gallery.startAnimation(animationscale);
                    showProgressDialog(v.getContext(), "Opening Gallery...");
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, Common.RESULT_LOAD_IMAGE);
                    /*animationscale.setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {


                        }
                    });*/
                }
            }
        });

        img_Camera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Request Permission bef run
                boolean ktquyentruycapcamera = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ArrayList<String> perms = new ArrayList<String>(Arrays
                            .asList(listPermission));
                    int permsRequestCode = 200;
                    permissionsToRequest = findUnAskedPermissions(perms);
                    if (permissionsToRequest.size() > 0) {
                        requestPermissions(
                                permissionsToRequest
                                        .toArray(new String[permissionsToRequest
                                                .size()]), permsRequestCode);
                    } else {
                        ktquyentruycapcamera = true;
                    }
                } else {
                    ktquyentruycapcamera = true;
                }
                vib.vibrate(30);
                if (ktquyentruycapcamera) {
                    Animation animationscale = AnimationUtils.loadAnimation(
                            v.getContext(), R.anim.anim_scale);
                    img_Camera.startAnimation(animationscale);
                    showProgressDialog(v.getContext(), "Opening Camera...");
                    Intent cameraIntent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent
                            .resolveActivity(getPackageManager()) != null) {
                        // Create file photo
                        File photoFile = new File(Common.APPICATION_PATH + "ocr_camera.png");
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            Log.d("Create file photo",
                                    "Error");
                            showToast("Can't create image file", Toast.LENGTH_LONG);
                        }

                        if (photoFile.isFile()) {
                            mCurrentPhotoPath = photoFile.getAbsolutePath();
                            mCommon_Preferences.setmCurrentPhotoPath(mCurrentPhotoPath);
                            Uri photoUri = FileProvider
                                    .getUriForFile(
                                            MainActivity.this,
                                            "com.education.voicetranslator",
                                            photoFile);
                            cameraIntent
                                    .putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            photoUri);
                            startActivityForResult(
                                    cameraIntent,
                                    Common.CAMERA_REQUEST);
                        }

                    }
                }

            }
        });
        lv_Tranlation = findViewById(R.id.lv_translate);

        db = new DataTranslate(this);
        // Log.d("Item Count", db.getItemCount()+"");
        ArrayList<item_translate> list_item = db.getAllItem();

        adapter_Translation = new Adapter_Translator(this, list_item);

        if (adapter_Translation.getCount() > 0) {
            img_ClearTranslation.setVisibility(View.VISIBLE);
        }
        img_ClearTranslation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vib.vibrate(30);
                Animation animationscale = AnimationUtils.loadAnimation(
                        v.getContext(), R.anim.anim_scale);
                img_ClearTranslation.startAnimation(animationscale);
                dialogXoaAllItem().show();
            }
        });
        lv_Tranlation.setAdapter(adapter_Translation);

        fLayoutSpeech = findViewById(R.id.flvoid);
        // flsearchvoid = (FrameLayout) findViewById(R.id.fl_searchvoid);

        mImageDrawable = (ClipDrawable) img.getDrawable();
        mImageDrawable.setLevel(0);
        // imgrespeak = (ImageView) findViewById(R.id.respeak);
        // imgrespeak.setVisibility(View.INVISIBLE);
        // imgsetting = (ImageView) findViewById(R.id.imgsetting);
        imgHoanDoi = findViewById(R.id.imghoandoi);
        // imgcopy = (ImageView) findViewById(R.id.imgcopy);
        proTalk = findViewById(R.id.progressBarTalk);

        proText = findViewById(R.id.progressBarText);
        // Setup Spinner
        mSpinnerOut = findViewById(R.id.spOut);
        mSpinnerIn = findViewById(R.id.spIn);

        ArrayList<ItemSpinner> list = new ArrayList<ItemSpinner>();
        ArrayList<ItemSpinner> listout = new ArrayList<ItemSpinner>();

        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.row,
                R.id.namelanguage, list);
        for (int i = 0; i < commonResource.getContriesin().length; i++) {
            list.add(new ItemSpinner(commonResource.getContriesin()[i], commonResource.getFlagcontriesin().getResourceId(
                    i, 0)));
            // ar_in.recycle();
        }
        SpinnerAdapter adapterout = new SpinnerAdapter(this, R.layout.row,
                R.id.namelanguage, listout);
        for (int i = 0; i < commonResource.getContriesout().length; i++) {
            listout.add(new ItemSpinner(commonResource.getContriesout()[i], commonResource.getFlagcontriesout()
                    .getResourceId(i, 0)));
            // ar_out.recycle();
        }
        mSpinnerOut.setAdapter(adapterout);
        mSpinnerIn.setAdapter(adapter);

        countRequest = mCommon_Preferences.getmSoRequest();
        isRated = mCommon_Preferences.ismRated();
        isMuchAds = mCommon_Preferences.ismMuchAds();
        isNewLang = mCommon_Preferences.ismNewLanguage();
        mCurrentPhotoPath = mCommon_Preferences.getmCurrentPhotoPath();
        isCameraTranslate = mCommon_Preferences.ismTranslateCamera();
        String ContryNameIn = mCommon_Preferences.getmContryNameIn();
        String ContryNameOut = mCommon_Preferences.getmContryNameOut();
        mNgonNguIn = new NgonNguIn(this,ContryNameIn);
        mNgonNguOut = new NgonNguOut(this,ContryNameOut);
        Log.d("LanguageIn Name",mCommon_Preferences.getmContryNameIn());
        Log.d("LanguageOut Name",mCommon_Preferences.getmContryNameOut());
        Log.d("Position",""+mNgonNguIn.getmPosition());
        Log.d("Position",""+mNgonNguOut.getmPosition());
        mSpinnerOut.setSelection(mNgonNguIn.getmPosition());
        mSpinnerIn.setSelection(mNgonNguOut.getmPosition());

        isSpeechRecoAvalable = SpeechRecognizer.isRecognitionAvailable(getBaseContext());
        if (!isSpeechRecoAvalable) {
            // showToast("Opps! Your device doesn't support Speech to Text");
            dialogAskInstallSTT().show();
        } else {

            mSpeechReco = SpeechRecognizer
                    .createSpeechRecognizer(
                            this,
                            ComponentName
                                    .unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"));
            // mSpeechReco.startListening(recognizerIntent);
            // mSpeechReco.setRecognitionListener(mRecoListener);
        }

        // docmau.setLanguage(Locale.ENGLISH);


        mSpinnerOut.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                Log.d("Language Out ", "Position " + position);
                if(mNgonNguOut.getmPosition()!=position) {
                    mNgonNguOut.updateNgonNguOut(commonResource.getContriesout()[position]);
                    mCommon_Preferences.setmContryNameOut(mNgonNguOut.getmLanguageName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });
        mSpinnerIn.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
				Log.d("Language in ", "Position " + position);
                et_Translate.setHint("Touch & Start Typing...");
                et_Translate.setCompoundDrawablesWithIntrinsicBounds(
                        commonResource.getFlagcontriesin().getResourceId(position, 0), 0, 0, 0);
                if(mNgonNguIn.getmPosition()!= position) {
                    mNgonNguIn.updateNgonNguIn(commonResource.getContriesin()[position]);
                    mCommon_Preferences.setmContryNameIn(mNgonNguIn.getmLanguageName());

                    if (mNgonNguIn.isSpeechRecognition()) {
                        mToggleSpeech.setVisibility(View.VISIBLE);
                        fLayoutSpeech.setVisibility(View.VISIBLE);
                    } else {
                        mToggleSpeech.setVisibility(View.GONE);
                        fLayoutSpeech.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        imgHoanDoi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Animation animationrotate = AnimationUtils.loadAnimation(
                        v.getContext(), R.anim.anim_rotate);
                imgHoanDoi.startAnimation(animationrotate);
                vib.vibrate(50);
                int lgin, newin;
                int lgout, newout;
                lgin = mSpinnerIn.getSelectedItemPosition();
                lgout = mSpinnerOut.getSelectedItemPosition();
                newin = lgout + 1;

                if (lgin > 0)
                    newout = lgin - 1;
                else
                    newout = getViTri("English", commonResource.getContriesout());
                mSpinnerIn.setSelection(newin);
                mSpinnerOut.setSelection(newout);
            }
        });

        mToggleSpeech.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // Request Permission bef run
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ArrayList<String> perms = new ArrayList<String>(Arrays
                            .asList(listPermission));
                    int permsRequestCode = 200;
                    permissionsToRequest = findUnAskedPermissions(perms);
                    if (permissionsToRequest.size() > 0) {
                        requestPermissions(
                                permissionsToRequest
                                        .toArray(new String[permissionsToRequest
                                                .size()]), permsRequestCode);
                    }
                }

                if (isChecked) {
                    // The toggle is enabled
                    if (isSpeechRecoAvalable) {
                        if (isOnline()) {
                            if (!Arrays.asList(commonResource.getNovoice()).contains(
                                    commonResource.getContriesin()[mNgonNguIn.getmPosition()])) {

                                try {
                                    if (!vib.hasVibrator()) {
                                        vib.vibrate(40);
                                    }
                                    thoiGianCho = Common.SPEECH_WAIT;
                                    Intent recognizerIntent1;
                                    recognizerIntent1 = new Intent(
                                            RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                    recognizerIntent1
                                            .putExtra(
                                                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                                    RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                                    recognizerIntent1
                                            .putExtra(
                                                    RecognizerIntent.EXTRA_CALLING_PACKAGE,
                                                    getPackageName());
                                    recognizerIntent1.putExtra(
                                            RecognizerIntent.EXTRA_MAX_RESULTS,
                                            5);

                                    recognizerIntent1
                                            .putExtra(
                                                    RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,
                                                    3000);
                                    recognizerIntent1.putExtra(
                                            RecognizerIntent.EXTRA_LANGUAGE,
                                            mNgonNguIn.getmCodeSpeech());

                                    recognizerIntent = recognizerIntent1;
                                    mSpeechReco.setRecognitionListener(mRecoListener);
                                    mSpeechReco.startListening(recognizerIntent1);

                                } catch (Exception e) {
                                    showToast(e.getMessage(),
                                            Toast.LENGTH_LONG);
                                }
                            } else {
                                mToggleSpeech.setChecked(false);
                                showToast(
                                        "Opps! "
                                                + mNgonNguIn.getmLanguageName()
                                                + " language was not supported Speech to Text",
                                        Toast.LENGTH_LONG);
                            }
                        } else {
                            mToggleSpeech.setChecked(false);
                            showToast(
                                    "Opps! No Internet Access, Please Try Again",
                                    Toast.LENGTH_LONG);
                        }
                    } else {
                        // showToast("Opps! Your device doesn't support Speech to Text");
                        mToggleSpeech.setChecked(false);
                        dialogAskInstallSTT().show();
                    }
                    Log.d("mToggleSpeech", "onTrue");
                } else {
                    if (isSpeechRecoAvalable) {

                        mSpeechReco.stopListening();
                        // mSpeechReco.destroy();
                        // mSpeechReco = null;
                    } else {
                        // showToast("Opps! Your device doesn't support Speech to Text");
                        dialogAskInstallSTT().show();
                    }
                    Log.d("mToggleSpeech", "onFalse");
                }

            }
        });

        et_Translate.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_ENTER) {
                    translateWithText();
                }
                return false;
            }
        });
        img_Translate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vib.vibrate(30);
                translateWithText();
            }
        });
        img_DeleteText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                et_Translate.setText("");
                img_DeleteText.setVisibility(View.GONE);
            }
        });
        img_resize_et.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vib.vibrate(20);
                // Lấy Text từ et_Translate nếu có
                String text = et_Translate.getText().toString().trim();
                dialogCameraResultText(text, false).show();
            }
        });

        et_Translate.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                if (et_Translate.getText().toString().trim().length() > 0) {
                    img_DeleteText.setVisibility(View.VISIBLE);
                    img_Translate.setVisibility(View.VISIBLE);
                } else {
                    img_DeleteText.setVisibility(View.GONE);
                    img_Translate.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        //isNewLang = true;
        if (isNewLang && countRequest >= 1) {
            String newlanguage = "Hungarian";
            dialogNewLanguage(newlanguage).show();
        } else {
            isNewLang = false;
        }

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if (type.startsWith("image/")) {
                Uri imageUri = intent
                        .getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    File sharefileimg = getPathFromUri(imageUri);
                    mCurrentPhotoPath = sharefileimg.getAbsolutePath();
                    mCommon_Preferences.setmCurrentPhotoPath(mCurrentPhotoPath);
                    dialogResult = dialogCameraResultImage(sharefileimg);
                    dialogResult.show();
                }
            }
        }
    }

    // Park Request Permissions Android M
    private boolean shouldWeAsk(String permission) {
        return pre_permissions.getBoolean(permission, true);
    }

    @TargetApi(23)
    private boolean hasPermission(String permission) {
        return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();
        for (String perm : wanted) {
            if (!hasPermission(perm) && shouldWeAsk(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File strfile = new File(mCurrentPhotoPath);

        if (requestCode == Common.CAMERA_REQUEST && resultCode == RESULT_OK) {
            isCameraTranslate = true;
            Log.d("onActivityResult", "CAMERA");
            // performCrop(strfile);
            if (strfile.isFile()) {
				/*File fileresize = resizeImg(strfile.getPath());*/
				dialogResult = dialogCameraResultImage(strfile);
            } else {
                showToast("Image file is not available, Try again!",
                        Toast.LENGTH_LONG);
            }
        }
        if (requestCode == Common.CROPING_CODE) {
            Log.d("isCameraTranslate", "Set True");
            Log.d("onActivityResult", "CROPING");
            isCameraTranslate = true;
            if (strfile.isFile()) {

                if (strfile.length() >= 1000) {
                    if (dialogResult != null) {
                        if (dialogResult.isShowing()) dialogResult.dismiss();
                    }
                    dialogResult = dialogCameraResultImage(strfile);

                } else {
                    showToast("Image file is too small, Try again!",
                            Toast.LENGTH_LONG);
                }

            } else {
                showToast("Image file is not available, Try again!",
                        Toast.LENGTH_LONG);
            }
        }

        if (requestCode == Common.RESULT_LOAD_IMAGE
                && resultCode == Activity.RESULT_OK && data != null) {
            isCameraTranslate = true;
            Log.d("Select Picture Gallery", "Result Oke");
            if(data.getData()!=null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaColumns.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if(cursor!=null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.d("Picture Path Gallery", picturePath);
                    File pictureFile = new File(picturePath);
                    mCurrentPhotoPath = picturePath;
                    dialogResult = dialogCameraResultImage(pictureFile);
                }
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];
            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
                    .getBottom())) {
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                        .getWindowToken(), 0);
                // This is where I set the flag back to false when my soft
                // keyboard is hidden
                isSoftKeyboardDisplayed = false;

            }
        }
        return ret;
    }
    /*private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }*/

    @Override
    protected void onResume() {
        mSpinnerOut.setSelection(mNgonNguOut.getmPosition());
        mSpinnerIn.setSelection(mNgonNguIn.getmPosition());
        if (mCurrentPhotoPath != null) {
            if (dialogResult != null && isCameraTranslate) {
                Log.d("mCurrentPhotoPath", mCurrentPhotoPath);
                dialogResult.show();
            } else if (dialogResult == null && isCameraTranslate) {
                Log.d("mCurrentPhotoPath", " " + mCurrentPhotoPath);
                File strfile = new File(mCurrentPhotoPath);
                if (strfile.isFile()) {
                    dialogResult = dialogCameraResultImage(strfile);
                    if (dialogResult.isShowing()) dialogResult.dismiss();
                    dialogResult.show();
                } else {
                    isCameraTranslate = false;
                }
            }
        }
        hideProgressDialog();
        Log.d("MainActivity", "onResume");
        mTracker.setScreenName("Main Application" + strNoAds);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        isTTSAvalable = appInstalledOrNot("com.google.android.tts");
        if (!isTTSAvalable) {
            dialogAskInstallTTS().show();
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            overlaysPermission = Settings.canDrawOverlays(this);
        else
            overlaysPermission = true;
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSpeechReco != null) {
            mSpeechReco.destroy();
        }
        Log.d("MainActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        db.close();
        mBill.release();
        adapter_Translation.shudownDocmau();
        if(interstitialAd!=null){
            interstitialAd.destroy();
        }
        if (dialogResult != null && dialogResult.isShowing()) {
            dialogResult.cancel();
        }
        Log.d("MainActivity", "onDestroy");
        commonResource.recycleIdResource();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mToggleSpeech.isChecked()) {
            mToggleSpeech.setChecked(false);
        }
        if (mThongBao != null)
            mThongBao.cancel();
        proTalk.setVisibility(View.GONE);
        mCommon_Preferences.setAllValue(countRequest, isRated, isMuchAds, isNewLang,
                mCommon_Preferences.getmInApp()+1, isCameraTranslate, isNoAds,
                mNgonNguIn.getmLanguageName(),mNgonNguOut.getmLanguageName(),mCurrentPhotoPath);
        Log.d("MainActivity", "onPause");
        super.onPause();
    }

    private boolean doubleBackToExitPressedOnce = false;
    private ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        hideProgressDialog();
        if (doubleBackToExitPressedOnce) {
            if (mThongBao != null)
                mThongBao.cancel();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        showToast("Please click BACK again to exit", Toast.LENGTH_LONG);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, Common.DELAY_TIME_DOUBLE_BACK_PRESS);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio Error, Please Try Again";

                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "I Can't Hear You, Please Try Again";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Không có quyền truy cập";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network Error, Please Check Your Internet";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "I Don't Understand, Please Try Again";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "Reconizer Busy, Please Try Later";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "I Can not Connect To Server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "Speech Timeout, Please Try Again";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    void showToast(String text, int length) {
        if (mThongBao != null) {
            mThongBao.cancel();
        }
        if (text.equalsIgnoreCase("")) {
            mThongBao.cancel();
        } else {
            mThongBao = Toast.makeText(this, text, length);
            mThongBao.setGravity(Gravity.CENTER, 0, 0);
            mThongBao.show();
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();

    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private void doTheUpAnimation(int fromLevel, int toLevel) {
        mLevel += LEVEL_DIFF;
        mImageDrawable.setLevel(mLevel);
        if (mLevel <= toLevel) {
            mUpHandler.postDelayed(animateUpImage, DELAY);
        } else {
            mUpHandler.removeCallbacks(animateUpImage);
            MainActivity.this.fromLevel = toLevel;
        }
    }

    private void doTheDownAnimation(int fromLevel, int toLevel) {
        mLevel -= LEVEL_DIFF;
        mImageDrawable.setLevel(mLevel);
        if (mLevel >= toLevel) {
            mDownHandler.postDelayed(animateDownImage, DELAY);
        } else {
            mDownHandler.removeCallbacks(animateDownImage);
            MainActivity.this.fromLevel = toLevel;
        }
    }


    private void translateWithText() {
        String textin = "";
        textin = et_Translate.getText().toString().trim();
        mNgonNguIn.setmText(textin);
        if (!mNgonNguIn.getmText().equalsIgnoreCase("")) {
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                    .getWindowToken(), 0);
            if (isOnline()) {
                img_Translate.setVisibility(View.GONE);
                proText.setVisibility(View.VISIBLE);
                showProgressDialog(this, "Translating...");
                micTranslatorYandex stranslateAsys = new micTranslatorYandex(this,mNgonNguIn.getmText(),
                        mNgonNguIn.getLanguageY(), mNgonNguOut.getLanguageY(),mYCallBack,
                        Common.CALLBACK_TEXT);
                showProgressDialog(this,"Translating...");
                stranslateAsys.execute();
            } else {
                showToast("No Internet Access, Please Try Again! ",
                        Toast.LENGTH_LONG);
            }

        } else {
            et_Translate.setText("");
            showToast("YOUR TEXT IS EMPTY", Toast.LENGTH_LONG);
        }
    }

    private Dialog dialogChonKetQua(ArrayList<String> ketqua) {
        final Dialog dung = new Dialog(MainActivity.this,
                R.style.FullHeightDialog);
        dung.setContentView(R.layout.speech_result_list);
        dung.setCancelable(true);
        dung.getWindow().setFormat(PixelFormat.TRANSPARENT);
        dung.setCanceledOnTouchOutside(true);
        dung.getWindow().setBackgroundDrawableResource(
                R.drawable.rounded_header);
        dung.getWindow().setDimAmount(0.8f);
        // dung.setTitle("March Your Result");
        final ArrayList<String> listitem = ketqua;
        final ListView lv_result = dung
                .findViewById(R.id.lv_result_speech);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                dung.getContext(), R.layout.row_list_result,
                R.id.tv_list_result, listitem);
        lv_result.setAdapter(adapter);
        lv_result.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item value
                String itemValue = (String) lv_result
                        .getItemAtPosition(position);
                mNgonNguIn.setmText(itemValue.trim());
                translateWithSpeech();
                dung.cancel();
            }

        });
        return dung;

    }

    private Dialog dialogVolumeControl() {
        final Dialog dung = new Dialog(MainActivity.this,
                R.style.FullHeightDialog);
        dung.setContentView(R.layout.volume_control);
        dung.setCancelable(true);
        dung.getWindow().setFormat(PixelFormat.TRANSPARENT);
        dung.setCanceledOnTouchOutside(true);
        dung.getWindow().setBackgroundDrawableResource(
                R.drawable.rounded_header);
        final ImageView imgsounddialog = dung
                .findViewById(R.id.img_sound_dialog);
        SeekBar sbarvolume = dung.findViewById(R.id.sbar_volume);
        int amluongmax = amThanh.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        amLuong = amThanh.getStreamVolume(AudioManager.STREAM_MUSIC);
        sbarvolume.setMax(amluongmax);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (amLuong == 0) {
            imgsounddialog.setImageResource(R.drawable.speaker_icon_no);
            img_Speaker.setImageResource(R.drawable.speaker_icon_no);
        }
        sbarvolume.setProgress(amLuong);
        // amThanh.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume, 0);
        sbarvolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                vib.vibrate(30);
                if (amLuong == 0) {
                    imgsounddialog.setImageResource(R.drawable.speaker_icon_no);
                    img_Speaker.setImageResource(R.drawable.speaker_icon_no);
                } else {
                    imgsounddialog
                            .setImageResource(R.drawable.speaker_icon_yes);
                    img_Speaker.setImageResource(R.drawable.speaker_icon_yes);
                }
                amThanh.setStreamVolume(AudioManager.STREAM_MUSIC, amLuong,
                        AudioManager.FLAG_PLAY_SOUND);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                amLuong = progress;
            }
        });
        return dung;

    }

    private Dialog dialogCameraResultText(String textin, final boolean autodetect) {

        final Dialog dung = new Dialog(MainActivity.this,
                R.style.FullHeightDialog);
        dung.setContentView(R.layout.camera_result);
        dung.setCancelable(true);
        dung.getWindow().setFormat(PixelFormat.TRANSPARENT);
        dung.setCanceledOnTouchOutside(false);
        dung.getWindow().setBackgroundDrawableResource(
                R.drawable.rounded_header);
        dung.getWindow().setDimAmount(0.8f);
		/*
		 * int oldsp_in = sp_in; mSpinnerIn.setSelection(0);
		 */

        final EditText et_camera_result = dung
                .findViewById(R.id.et_camera_result);
        et_camera_result.setHint("Touch & Start Typing...");
        et_camera_result.setText(textin);

        Button btn_ok_translate = dung
                .findViewById(R.id.btn_ok_translate);
        Button btn_cancel_translate = dung
                .findViewById(R.id.btn_cancel_translate);
        btn_cancel_translate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vib.vibrate(30);
                // et_Translate.setText(et_camera_result.getText().toString().trim());
                dung.cancel();
            }
        });
        btn_ok_translate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                dung.cancel();
                vib.vibrate(30);

                Log.d("Text to Translate: ", et_camera_result.getText()
                        .toString());
                mNgonNguIn.setmText(et_camera_result.getText().toString().trim());
                if (!mNgonNguIn.getmText().equals("")) {
                    // final ProgressDialog progressDialog =
                    // ProgressDialog.show(v.getContext(), "",
                    // "Translating...");
                    showProgressDialog(v.getContext(), "Translating...");
                    if (autodetect)
                        mNgonNguIn.setmCodeLanguage(LanguageY.AUTODETECT.toString());

                    micTranslatorYandex stranslateAsys = new micTranslatorYandex(dung.getContext(),mNgonNguIn.getmText(),
                            mNgonNguIn.getLanguageY(), mNgonNguOut.getLanguageY(), mYCallBack,Common.CALLBACK_CAMERA);
                    stranslateAsys.execute();
                    showProgressDialog(dung.getContext(),"Translating...");
                }
            }
        });
        return dung;

    }

    private File fin;

    private Dialog dialogCameraResultImage(File filein) {
        fin = filein;

        final Dialog dung = new Dialog(MainActivity.this,
                R.style.FullHeightDialog);
        dung.setContentView(R.layout.camera_img_result);
        dung.setCancelable(true);
        dung.getWindow().setFormat(PixelFormat.TRANSPARENT);
        dung.setCanceledOnTouchOutside(false);
        dung.getWindow().setBackgroundDrawableResource(
                R.drawable.rounded_header);
        dung.getWindow().setDimAmount(0.8f);
        boolean downloadhinh = false;

        final ImageView img_camera_result = dung
                .findViewById(R.id.img_result_camera);
        Button btn_cancel = dung.findViewById(R.id.btn_cancel_result);
        Button btn_prgress = dung.findViewById(R.id.btn_progress);
        Button btn_rotate = dung.findViewById(R.id.btn_rotate);
        Button btn_crop = dung.findViewById(R.id.btn_crop);

        Spinner spn_language_img = dung.findViewById(R.id.spIn_image);
        ArrayList<ItemSpinner> list = new ArrayList<ItemSpinner>();

        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.row_flags,
                R.id.namelanguage, list);
        for (int i = 0; i < commonResource.getCameraOcr().length; i++) {

            list.add(new ItemSpinner(commonResource.getCameraOcr()[i],
                    commonResource.getFlagcontriesocr().getResourceId(i, 0)));
            // ar_in.recycle();
        }


        spn_language_img.setAdapter(adapter);
        int sp = commonResource.getPosition(mNgonNguIn.getmLanguageName(),commonResource.getCameraOcr());
        spn_language_img.setSelection(sp);
        spn_language_img
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        // TODO Auto-generated method stub
                        String contryname = commonResource.getCameraOcr()[position];
                        int spin = commonResource.getPosition(contryname, commonResource.getContriesin());
                        mSpinnerIn.setSelection(spin);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });


        boolean pathnull = true;
        if (mCurrentPhotoPath != null) pathnull = false;
        if (!pathnull) {
            if (mCurrentPhotoPath.substring(0, 4).equalsIgnoreCase("http")) {
                downloadhinh = true;
            }
            if (downloadhinh) {
                if (isOnline()) {
                    Runnable rundownload = new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            fin = createFileDownImage();
                            DownloadImageCallBack mCallBack = new DownloadImageCallBack() {
                                @Override
                                public void getDownloadImageCallBack(File mfile) {
                                    if (mfile != null) {
                                        fin = mfile;
                                        mCurrentPhotoPath = fin.getAbsolutePath();
                                        Log.d("mCurrentPhotoPath", mCurrentPhotoPath);
                                        Log.d("File Downloaded", fin.getName());
                                        Log.d("File Downloaded Size", "" + fin.length());
                                        try {
                                            resizeImg(fin.getAbsolutePath(), img_camera_result);

                                        } catch (ExecutionException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                            showToast("Error image file!", Toast.LENGTH_LONG);
                                            dung.dismiss();
                                            hideProgressDialog();
                                            isCameraTranslate = false;
                                        }
                                        Log.d("mCurrentPhotoPath", mCurrentPhotoPath);
                                    } else {
                                        showToast("Error download image file!", Toast.LENGTH_LONG);
                                        dung.dismiss();
                                        hideProgressDialog();
                                        isCameraTranslate = false;
                                    }
                                }
                            };
                            DownloadImage mDownloadImage = new DownloadImage(mCurrentPhotoPath, fin,mCallBack);
                            mDownloadImage.execute();

                        }
                    };
                    Handler handler = new Handler();
                    handler.post(rundownload);

                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            showToast("Can't download Image, Please try again!", Toast.LENGTH_LONG);
                            dung.dismiss();
                            isCameraTranslate = false;
                            hideProgressDialog();
                        }
                    }, 2000);

                }
            } else {
                try {
                    resizeImg(fin.getAbsolutePath(), img_camera_result);
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    showToast("Error image file!", Toast.LENGTH_LONG);
                    dung.dismiss();
                    hideProgressDialog();
                    isCameraTranslate = false;
                }
            }
        } else {
            dung.dismiss();
            isCameraTranslate = false;
            hideProgressDialog();
        }


        btn_crop.setOnClickListener(new OnClickListener() {

            File crfin = fin;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // dung.dismiss();
                if (resized) {
                    vib.vibrate(30);
                    fin = new File(mCurrentPhotoPath);
                    crfin = fin;
                    Log.d("btn_crop", "Send to CROP");
                    img_camera_result.setVisibility(View.INVISIBLE);
                    performCrop(crfin);

                }

            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dung.dismiss();
                vib.vibrate(30);
                isCameraTranslate = false;
                hideProgressDialog();
            }
        });
        final IOCRCallBack mIOCRCallBack = this;
        btn_prgress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //if(!isOcrTextAPI) {
                    if (resized) {
                        fin = new File(mCurrentPhotoPath);
                        vib.vibrate(30);
                        isCameraTranslate = false;
                        File fileresize = fin;
                        if (fileresize.isFile()) {
                            long filesize = fileresize.length();
                            Log.d("Language Camera", mNgonNguIn.getmCodeOCR());
                            if (filesize < 1000000) {
                                dung.dismiss();
                                Log.d("File Image", "< 1MB");
                                micOCRMicrosoft oCRAsyncTask = new micOCRMicrosoft(
                                        MainActivity.this, false, fileresize,
                                        mNgonNguIn.getmCodeOCR(), mNgonNguIn.isOCRTextAPI(), mIOCRCallBack);
                                showProgressDialog(dung.getContext(),"Progressing...");
                                oCRAsyncTask.execute();
                            } else {
                                showToast("Image file is too large! please CROP it",
                                        Toast.LENGTH_LONG);
                            }

                        }
                    }

            }
        });

        btn_rotate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (resized) {
                    vib.vibrate(30);
                    //fin = new File(mCurrentPhotoPath);
                    Animation anim_alpha_hide = AnimationUtils.loadAnimation(
                            dung.getContext(), R.anim.anim_alpha);
                    final Animation anim_alpha_show = AnimationUtils.loadAnimation(
                            dung.getContext(), R.anim.anim_alpha_hienlen);
                    img_camera_result.startAnimation(anim_alpha_hide);
                    fin = new File(mCurrentPhotoPath);
                    Log.d("Image File Rotate", fin.getAbsolutePath());
                    if (fin.isFile()) {
                        anim_alpha_hide.setAnimationListener(new AnimationListener() {
                            Bitmap show = null;

                            @Override
                            public void onAnimationStart(Animation animation) {
                                // TODO Auto-generated method stub
                                show = rotate90(90, fin, img_camera_result);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                // TODO Auto-generated method stub
                                if (show != null) {
                                    img_camera_result.startAnimation(anim_alpha_show);
                                }
                            }
                        });
                    } else {
                        showToast("Unknown Error, Please try again!", Toast.LENGTH_LONG);
                    }
                }

            }
        });
        dung.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    isCameraTranslate = false;
                    hideProgressDialog();
                    dung.dismiss();
                }
                return true;
            }
        });
        return dung;

    }

    public static Bitmap rotate90(float angle, File fileResize, ImageView imageview) {

        Bitmap source = decodeFile(fileResize, Common.IMAGE_MAX_SIZE);
        Bitmap retVal;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, true);

        FileOutputStream fo;
        try {
            Log.d("Rotate File", "Xử lý");
            fo = new FileOutputStream(fileResize);
            retVal.compress(Bitmap.CompressFormat.PNG, 100, fo);
            imageview.setImageBitmap(retVal);
            //retVal.recycle();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return source;
    }

    // set Corner Image Result
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private Dialog dialogNewLanguage(String newla) {

        final Dialog dung = new Dialog(MainActivity.this,
                R.style.FullHeightDialog);
        dung.setContentView(R.layout.warning);
        dung.setCancelable(true);
        dung.getWindow().setFormat(PixelFormat.TRANSPARENT);
        dung.setCanceledOnTouchOutside(true);
        dung.getWindow().setBackgroundDrawableResource(
                R.drawable.rounded_header);
        dung.getWindow().setDimAmount(0.8f);
        isNewLang = false;
        final int sp = getViTri(newla, commonResource.getContriesout());
        TextView languagenew = dung
                .findViewById(R.id.txt_languagenew);
        ImageView imglanguage = dung.findViewById(R.id.img_warning);
        imglanguage.setImageResource(commonResource.getFlagcontriesout().getResourceId(sp, 0));
        languagenew.setText(newla + " Language");
        languagenew.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vib.vibrate(20);
                mSpinnerOut.setSelection(sp);
                dung.cancel();

            }
        });
        imglanguage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vib.vibrate(20);
                mSpinnerOut.setSelection(sp);
                dung.cancel();
            }
        });

        return dung;

    }


    public void translateWithSpeech() {
        // Translate choose text
        mToggleSpeech.setVisibility(View.VISIBLE);
        proTalk.setVisibility(View.GONE);
        if (!mNgonNguIn.getmText().equals("")) {
            showProgressDialog(this, "Translating...");
            micTranslatorYandex stranslateAsys = new micTranslatorYandex(this,mNgonNguIn.getmText(),
                    mNgonNguIn.getLanguageY(), mNgonNguOut.getLanguageY(), mYCallBack,Common.CALLBACK_SPEECH);
            stranslateAsys.execute();
            showProgressDialog(this,"Translating...");

        }

    }

    private AlertDialog dialogXoaAllItem() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setMessage("Do You Want to Delete All Items?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        Animation animTranslate = AnimationUtils.loadAnimation(
                                alertDialogBuilder.getContext(),
                                R.anim.anim_translate);
                        img_ClearTranslation.startAnimation(animTranslate);
                        vib.vibrate(30);
                        animTranslate
                                .setAnimationListener(new AnimationListener() {

                                    @Override
                                    public void onAnimationStart(
                                            Animation animation) {
                                        // TODO Auto-generated method stub
                                        lv_Tranlation.startAnimation(animation);
                                    }

                                    @Override
                                    public void onAnimationRepeat(
                                            Animation animation) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void onAnimationEnd(
                                            Animation animation) {
                                        // TODO Auto-generated method stub
                                        adapter_Translation.clear();
                                        int i = db.delAllItem();
                                        showToast("Deleted " + i + " Items ",
                                                Toast.LENGTH_LONG);
                                        img_ClearTranslation.setVisibility(View.INVISIBLE);
                                    }
                                });
                        return;
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        return;
                    }
                });

        return alertDialogBuilder.create();


    }

    private AlertDialog dialogAskInstallTTS() {
        AlertDialog.Builder tbcaidattts = new AlertDialog.Builder(this);

        // tbtrudiem.setTitle("Thông báo");
        tbcaidattts.setMessage("Install Google TTS For Best Voice Speaker");
        tbcaidattts.setPositiveButton("Install Now!",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // final String appPackageName = getPackageName(); //
                        // getPackageName() from Context or Activity object
                        try {
                            Intent i = new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.google.android.tts"));
                            //.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } catch (android.content.ActivityNotFoundException anfe) {
                            Intent j = new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.tts"));
                            //j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(j);
                        }
                        finish();
                    }
                });

        tbcaidattts.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return tbcaidattts.create();
    }

    private AlertDialog dialogAskInstallSTT() {
        AlertDialog.Builder tbcaidatstt = new AlertDialog.Builder(this);

        // tbtrudiem.setTitle("Thông báo");
        tbcaidatstt.setMessage("Install Google Voice For Voice Translate");
        tbcaidatstt.setPositiveButton("Install Now!",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // final String appPackageName = getPackageName(); //
                        // getPackageName() from Context or Activity object
                        try {
                            Intent i = new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.google.android.googlequicksearchbox"));
                            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } catch (android.content.ActivityNotFoundException anfe) {
                            Intent j = new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.googlequicksearchbox"));
                            //j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(j);
                        }
                        finish();
                    }
                });

        tbcaidatstt.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return tbcaidatstt.create();
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void goToGoogleSettings() {
        Intent myAppSettings = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:"
                        + "com.google.android.googlequicksearchbox"));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }

    @Override
    public void getOCRCallBackResult(String response , boolean errorSendPost) {
        JSONObject jsonRootObject;
        String name = "";
        if(!errorSendPost) {
            if (!response.equals("")) {
                try {
                    jsonRootObject = new JSONObject(response);
                    JSONArray jsonArray = jsonRootObject
                            .optJSONArray("ParsedResults");
                    if (jsonArray != null) {

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        name = jsonObject.optString("ParsedText");
                        name = name.trim();
                        Log.d("getOCRCallBackResult", "ParsedText: " + name);
                    } else {
                        Log.d("getOCRCallBackResult", "jsonArray is NULL");
                    }

                    if (!name.equalsIgnoreCase("")) {
                        if (mNgonNguIn.getmCodeOCR().equals("jpn")
                                || mNgonNguIn.getmCodeOCR().equals("chs")) {
                            name = name.replaceAll("\\s+", "");
                            Log.d("Control Result",
                                    "Control String With Japanese or Chinies");
                        } else {
                            name = name.replaceAll("\\s+", " ");
                            Log.d("Control Result",
                                    "Control String Delele All Space");
                        }

                        dialogCameraResultText(name, false).show();

                    } else {
                        showToast("TEXT WAS NOT DETECTED!!", Toast.LENGTH_LONG);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                showToast("TEXT WAS NOT DETECTED!!", Toast.LENGTH_LONG);
            }
        }else{
            //Reshow dialog image OCR
            showToast("Can't Connect to OCR Server, Please try again!!",Toast.LENGTH_LONG);
            dialogResult.show();
        }
        hideProgressDialog();
        // Get the instance of JSONArray that contains JSONObjects
    }

    @Override
    public void getOCRCallBackVisionOCR(String response, boolean errorOCRText) {
        if(!errorOCRText)
            if(response!=null)
                dialogCameraResultText(response.trim(),false).show();
            else
                showToast("TEXT WAS NOT DETECTED!!",Toast.LENGTH_LONG);
        else {
            showToast("Some Error OCR, Please try again!!",Toast.LENGTH_LONG);
            dialogResult.show();
        }
        hideProgressDialog();
    }

    private AlertDialog dialogRateApp() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setMessage("Hi, if you love my app, please take a moment to rate it. Thank you!");
        alertDialogBuilder.setTitle("Review App");
        alertDialogBuilder.setIcon(R.drawable.logo_small);
        alertDialogBuilder.setPositiveButton("RATE NOW!",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        isRated = true;
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse("market://details?id="
                                            + getPackageName())));

                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id="
                                            + getPackageName())));
                        }
                        return;
                    }
                });

        alertDialogBuilder.setNeutralButton("Remind me later",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        return;
                    }
                });
        alertDialogBuilder.setCancelable(false);
        return alertDialogBuilder.create();

    }

    private AlertDialog DialogSorryforAds() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setMessage("I need to show ads to continue improving the app and better support users. "
                        + "I hope you understand and click ads. You can also remove ads "
                        + "to use all features without ads.");
        alertDialogBuilder.setTitle("Why do you see Ads");
        alertDialogBuilder.setIcon(R.drawable.logo_small);
        alertDialogBuilder.setPositiveButton("Remove Ads",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        clickPay2Dolar();
                        btnPay2Dolar.setVisibility(View.GONE);
                        btnRestartApp.setVisibility(View.VISIBLE);

                    }
                });
        alertDialogBuilder.setNeutralButton("I understood",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        isMuchAds = true;
        alertDialogBuilder.setCancelable(true);
        return alertDialogBuilder.create();

    }

    private AlertDialog dialogSettingGoogleApp() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setMessage("Please Enable Micro Permission");
        alertDialogBuilder.setTitle("SettingActivity Permissions");
        alertDialogBuilder.setPositiveButton("Go To SettingActivity",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        goToGoogleSettings();
                    }
                });
        return alertDialogBuilder.create();
    }

    private void showNotification(String sub, String body) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.logo_small).setContentTitle(sub)
                .setContentText(body);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent
                .putExtra(android.content.Intent.EXTRA_TEXT,
                        "http://play.google.com/store/apps/details?id=" + getPackageName());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(WelcomeActivity.class);
        stackBuilder.addNextIntent(sharingIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent).addAction(
                R.drawable.enter_icon, "Share Now", resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private boolean resized = false;

    private void resizeImg(String filepath, final ImageView imageview) throws ExecutionException {
        ResizeImageCallBack mCallBack = new ResizeImageCallBack() {
            @Override
            public void getResizeImageCallBack(File mFile,Bitmap mBitmap) {
                resized = true;
                fin = mFile;
                if (mFile!=null) {
                    mCurrentPhotoPath = mFile.getAbsolutePath();
                    Log.d("mCurrentPhotoPath", mCurrentPhotoPath);
                    //Bitmap resultBitmap = decodeFile(result, IMAGE_MAX_SIZE);
                    mCommon_Preferences.setmCurrentPhotoPath(mCurrentPhotoPath);
                    if (mBitmap != null && imageview != null) {
                        imageview.setImageBitmap(mBitmap);
                        Log.d("Rezied", "" + resized);
                    }
                } else {
                    Log.d("Resize Image File", "NULL");
                    if (mBitmap != null && imageview != null) {
                        imageview.setImageBitmap(mBitmap);
                        Log.d("Rezied", "" + resized);
                    }
                }
            }
        };
        Resizeimage mResizeImage = new Resizeimage(filepath,mCallBack);
        mResizeImage.execute();

    }

    private File getPathFromUri(Uri contentUri) {
        // String path = null;
        int sorandomocr = randInt(0, 99999);
        File fileresize = null;
        if (freeMemory >= 10000000) {
            fileresize = new File(Common.APPICATION_PATH
                    + "ocr_resize_" + sorandomocr + ".png");
        } else {
            fileresize = new File(Common.APPICATION_PATH + "ocr_resize.png");
        }
        try {
            FileOutputStream fo = new FileOutputStream(fileresize);
            Bitmap bitmap = getBitmapFromUri(contentUri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fo);
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // path = fileresize
        // showToast(path, Toast.LENGTH_LONG);
        Log.d("Kiem Tra Share Via", "Path: " + fileresize.getAbsolutePath());
        return fileresize;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver()
                .openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor
                .getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    private File createImageFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        freeMemory = new File(Common.APPICATION_PATH).getFreeSpace();
        File image = File.createTempFile("cameraocr", /* prefix */
                ".png", /* suffix */
                storageDir /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        mCommon_Preferences.setmCurrentPhotoPath(mCurrentPhotoPath);
        return image;
    }


    private int getViTri(String countryname, String[] listcountry) {
        int trave = 0;
        for (int i = 0; i < listcountry.length; i++) {
            if (listcountry[i].equalsIgnoreCase(countryname)) {
                trave = i;
                return trave;
            }
        }
        return getViTri("English", listcountry);
    }

    private void performCrop(File f) {
        try {
            final Intent cropIntent = new Intent(
                    "com.android.camera.action.CROP");
            final Uri contentUri = getImageContentUri(this.getBaseContext(), f);
            if (contentUri != null) {
                try {
                    cropIntent.setDataAndType(contentUri, "image/*");
                    cropIntent.putExtra("crop", "true");
                    cropIntent.putExtra("return-data", false);
                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                    startActivityForResult(cropIntent, Common.CROPING_CODE);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            } else {
                showToast("Can't Crop This File", Toast.LENGTH_LONG);
            }
        } catch (ActivityNotFoundException e) {
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast
                    .makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        if (imageFile != null) {
            String filePath = imageFile.getAbsolutePath();
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{BaseColumns._ID},
                    MediaColumns.DATA + "=? ", new String[]{filePath}, null);
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                cursor.close();
                return Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
            } else {
                if (imageFile.exists()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaColumns.DATA, filePath);
                    return context.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values);
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public void showProgressDialog(Context context, String noidung) {
        hideProgressDialog();
        this.progressDialog = ProgressDialog.show(context, "", noidung);
        this.progressDialog.setCancelable(true);
    }

    public void hideProgressDialog() {
        if (this.progressDialog != null) {
            if (this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
            this.progressDialog = null;
        }
    }

    @Override
    public void onBillingError(int errorCode, Throwable arg1) {
        // TODO Auto-generated method stub
        // showToast("onBillingError: " +
        // Integer.toString(errorCode),Toast.LENGTH_LONG);
    }

    @Override
    public void onBillingInitialized() {
        // TODO Auto-generated method stub
        // showToast("onBillingInitialized",Toast.LENGTH_SHORT);
        Log.d("Billing Processor", "onBillingInitialized");
        // readyToPurchase = true;
    }

    @Override
    public void onProductPurchased(String arg0, TransactionDetails arg1) {
        // TODO Auto-generated method stub
        showToast("onProductPurchased: " + arg0, Toast.LENGTH_LONG);
        payingSuccessful();
    }

    @Override
    public void onPurchaseHistoryRestored() {
        // TODO Auto-generated method stub

    }

    void payingSuccessful() {
        isNoAds = true;
        mCommon_Preferences.setmNoAds(isNoAds);
    }

    void clickPay2Dolar() {
        if (BillingProcessor.isIabServiceAvailable(getBaseContext())) {
            mBill.purchase(this, "hotronhaphattrien");
        } else {
            showToast("In-app billing service is unavailable",
                    Toast.LENGTH_LONG);

        }
    }

    void restartApp() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public File createFileDownImage() {
        return new File(Common.APPICATION_PATH
                + "ocr_download_" + randInt(0, 1000) + ".png");
    }


    public boolean getUsername(String username) {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();
        for (Account account : accounts) {
            possibleEmails.add(account.name);
        }
        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            for (String email : possibleEmails) {
                String[] parts = email.split("@");
                if (parts.length > 0 && parts[0] != null)
                    if (parts[0].equalsIgnoreCase(username)) {
                        return true;
                    }
            }
            return false;
        } else
            return false;
    }

    public static Bitmap decodeFile(File f, int WIDTH) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            int width = o.outWidth;
            int height = o.outHeight;
            int sosanh = width;
            if (height > width) sosanh = height;
            //Find the correct scale value. It should be the power of 2.
            int scale = 1;

            while (sosanh / scale / 2 >= WIDTH)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Log.d("Resize Scale:", "" + scale);
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            Log.d("Resize Scale:", e.getMessage());
        }
        return null;
    }

    private void resultProgressTranslateText(String result){
        vib.vibrate(100);
        img_Translate.setVisibility(View.VISIBLE);
        proText.setVisibility(View.GONE);
        mNgonNguOut.setmText(result);

        if (!mNgonNguOut.getmText().equals("translate false")) {
                            /*item_translate item = new item_translate(0,
                                    commonResource.getContriesin()[sp_in], commonResource.getContriesout()[sp_out],
                                    this.str_in, textout, lc_docmau_in.toString(),
                                    lc_docmau_out.toString());*/
            item_translate item = new item_translate(0,mNgonNguIn,mNgonNguOut);
            int iditem;
            iditem = db.additem_translate(item);
            item.setId(iditem);
            adapter_Translation.insert(item, 0);
            lv_Tranlation.smoothScrollToPosition(0);
            img_ClearTranslation.setVisibility(View.VISIBLE);
            Log.d("ID OF ITEM ADD", item.getId() + "");
            et_Translate.setText("");
            //textin = "";
            Log.d("Translate from", "Press Img");
            countRequest++;
            if (countRequest % 8 == 0) {
                showNotification(
                        "Do You Love Voice Translator?",
                        "Share it to your friends and learn more languages together.");
            }
            if (!isNoAds && countRequest % 8 == 0) {
                if (interstitialAd.isAdLoaded()) {
                    interstitialAd.show();
                } else {
                    // qcgoogle.loadAds();
                }
                // qcfacebook.showInterstitialAd();
            }
            // }
            if (countRequest % 10 == 0 && !isRated) {
                dialogRateApp().show();
            }
            if (!isNoAds && countRequest % 5 == 0 && !isMuchAds) {
                DialogSorryforAds().show();
            }
        } else {
            showToast("Error Translating, Please Try Again!",
                    Toast.LENGTH_LONG);
        }
        hideProgressDialog();

        if (!isTTSAvalable) {
            dialogAskInstallTTS().show();
        }
    }
    private void resultProgressCameraResult(String result){
        vib.vibrate(100);

        mNgonNguOut.setmText(result);
        if (!mNgonNguOut.getmText().equals("translate false")) {
            item_translate item;
                                /*if (autodetect) {
                                    item = new item_translate(0,
                                            commonResource.getContriesin()[0],
                                            commonResource.getContriesout()[sp_out], itemValue,
                                            textout, lc_docmau_in.toString(),
                                            lc_docmau_out.toString());
                                } else {
                                    item = new item_translate(0,
                                            commonResource.getContriesin()[sp_in],
                                            commonResource.getContriesout()[sp_out], itemValue,
                                            textout, lc_docmau_in.toString(),
                                            lc_docmau_out.toString());
                                }*/
            item = new item_translate(0,mNgonNguIn,mNgonNguOut);
            // list_item.add(item);
            int iditem;
            iditem = db.additem_translate(item);
            item.setId(iditem);
            adapter_Translation.insert(item, 0);
            lv_Tranlation.setSelection(0);
            img_ClearTranslation.setVisibility(View.VISIBLE);
            Log.d("ID OF ITEM ADD", item.getId() + "");
            countRequest++;
            if (countRequest % 8 == 0) {
                showNotification(
                        "Do You Love Voice Translator?",
                        "Share it to your friends and learn more languages together.");
            }
            if (countRequest % 10 == 0 && !isRated) {
                dialogRateApp().show();
            }
            if (!isNoAds && countRequest % 5 == 0 && !isMuchAds) {
                DialogSorryforAds().show();
            }
        } else {
            showToast(
                    "Error Translating, Please Try Again!",
                    Toast.LENGTH_LONG);
        }
        // progressDialog.dismiss();
        hideProgressDialog();
        if (!isTTSAvalable) {
            dialogAskInstallTTS().show();
        }
    }
    private void resultProgressSpeech(String result){
        vib.vibrate(100);
        mNgonNguOut.setmText(result);
        if (!mNgonNguOut.getmText().equals("translate false")) {
            item_translate item = new item_translate(0,mNgonNguIn,mNgonNguOut);
            int iditem;
            iditem = db.additem_translate(item);
            item.setId(iditem);
            adapter_Translation.insert(item, 0);
            lv_Tranlation.smoothScrollToPosition(0);
            img_ClearTranslation.setVisibility(View.VISIBLE);
            Log.d("ID OF ITEM ADD", item.getId() + "");
            countRequest++;
            if (countRequest % 8 == 0) {
                showNotification("Do You Love Voice Translator?",
                        "Share it to your friends and learn more languages together.");
            }
            if (!isNoAds && countRequest % 8 == 0) {
                if (interstitialAd.isAdLoaded()) {
                    interstitialAd.show();
                }
            }
            if (countRequest % 10 == 0 && !isRated) {
                dialogRateApp().show();
            }
            if (!isNoAds && countRequest % 5 == 0 && !isMuchAds) {
                DialogSorryforAds().show();
            }
        } else {
            showToast("Error Translating, Please Try Again!",
                    Toast.LENGTH_LONG);
        }
        hideProgressDialog();
        if (!isTTSAvalable) {
            dialogAskInstallTTS().show();
        }
    }
    @Override
    public void getYTranslateCallBack(String responcse, String progressingName) {
        if(progressingName.equalsIgnoreCase(Common.CALLBACK_TEXT))
            resultProgressTranslateText(responcse);
        else if(progressingName.equalsIgnoreCase(Common.CALLBACK_CAMERA))
            resultProgressCameraResult(responcse);
        else if(progressingName.equalsIgnoreCase(Common.CALLBACK_SPEECH))
            resultProgressSpeech(responcse);
        hideProgressDialog();
    }
}
