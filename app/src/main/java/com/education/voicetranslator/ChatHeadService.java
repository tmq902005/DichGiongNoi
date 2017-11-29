package com.education.voicetranslator;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import com.education.voicetranslator.adapter.ItemSpinner;
import com.education.voicetranslator.adapter.QspinnerAdapter;
import com.education.voicetranslator.callback.YTranslatorCallBack;
import com.education.voicetranslator.common.Common_Preferences;
import com.education.voicetranslator.common.Common_Resource;
import com.education.voicetranslator.mic.micTranslatorYandex;
import com.education.voicetranslator.ngonngu.NgonNguIn;
import com.education.voicetranslator.ngonngu.NgonNguOut;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

/*import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
*/
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.LayoutInflater;

@SuppressLint("InflateParams")
public class ChatHeadService extends Service  implements OnInitListener {
    private Vibrator vib;
    private WindowManager windowManager;
    private View chatHead;
    private ImageView removeImg;
    private View qview;
    private Toast thongbao;
    private Common_Preferences common_preferences;
    private boolean noads = false;
    private boolean transnow = false;
    private TextToSpeech docmau;
    private Common_Resource commonResource = new Common_Resource(this);
    private Point szWindow = new Point();
    private Dialog mqtranslator;
    WindowManager.LayoutParams params;
    WindowManager.LayoutParams paramRemove;
    public static final int DELAY_TRANS = 1500;
    private NgonNguIn mNgonNguIn;
    private NgonNguOut mNgonNguOut;


    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getApplication().getBaseContext().getSystemService(WINDOW_SERVICE);
        qview = LayoutInflater.from(this).inflate(R.layout.quick_translate,null);
        chatHead = qview.findViewById(R.id.img_qchathead);

        View removeView = LayoutInflater.from(this).inflate(R.layout.remove, null);
        removeImg = removeView.findViewById(R.id.remove_img);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        common_preferences = new Common_Preferences(this);

        noads = common_preferences.ismNoAds();
        String contrynamein = common_preferences.getmContryNameIn();
        String contrynameout = common_preferences.getmContryNameOut();
        mNgonNguIn = new NgonNguIn(this,contrynamein);
        mNgonNguOut = new NgonNguOut(this,contrynameout);
        docmau = new TextToSpeech(this, this, "com.google.android.tts");
        docmau.setSpeechRate((float) 0.8);
        docmau.setPitch((float) 0.9);

        paramRemove = getlayoutparams();
        paramRemove.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;


        params = getlayoutparams();
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        // this code is for dragging the chat head
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("ChatHead onTouch","In onTouch Event");
                windowManager.getDefaultDisplay().getSize(szWindow);
                int w, h;
                w = windowManager.getDefaultDisplay().getWidth()
                        - chatHead.getHeight();
                h = windowManager.getDefaultDisplay().getHeight()
                        - chatHead.getWidth();
                    windowManager.getDefaultDisplay().getSize(szWindow);
                int x_cord_remove = (szWindow.x
                        - (removeImg.getLayoutParams().height))/2;/* - chatHead_closed
                        .getWidth()) / 2;*/
                int y_cord_remove = szWindow.y
                        - (chatHead.getWidth() + getStatusBarHeight());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        removeImg.setVisibility(View.VISIBLE);
                        initialX = params.x;
                        if (initialX > w)
                            initialX = w;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:

                        removeImg.setVisibility(View.GONE);
                        if (params.x == x_cord_remove && params.y == y_cord_remove) {
                            vib.vibrate(30);
                            stopSelf();
                            break;
                        }

                        int moveX = initialX - params.x;
                        int moveY = initialY - params.y;

                        if ((moveX < 10 && moveX > -10)
                                && (moveY < 10 && moveY > -10)) {

                            mqtranslator = qtranslate();
                            vib.vibrate(30);
                            chatHead.setVisibility(View.GONE);
                            mqtranslator.show();
                            return true;

                        }
                        // fromLevel = params.x;
                        int mLevel = params.x;
                        if (params.x < (w / 2)) {
                            toLevel = 0;
                            doDownTheAnimation(mLevel, toLevel);
                        } else {
                            toLevel = w;
                            doUpTheAnimation(mLevel, toLevel);
                        }

                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        if (params.x <= 0)
                            params.x = 0;
                        else if (params.x >= w)
                            params.x = w;
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
                        if (params.y <= 0)
                            params.y = 0;
                        else if (params.y >= h)
                            params.y = h;

                        if (params.y >= y_cord_remove - (y_cord_remove / 6)) {
                            params.x = x_cord_remove;
                            params.y = y_cord_remove;
                            removeImg.setVisibility(View.GONE);
                        } else {
                            removeImg.setVisibility(View.VISIBLE);
                        }

                        windowManager.updateViewLayout(qview, params);
                        return true;

                }
                return false;
            }

        });

        windowManager.addView(qview, params);
        windowManager.addView(removeView, paramRemove);
    }


    private int toLevel = 0;
    public static final int LEVEL_DIFF = 50;
    public static final int DELAY = 10;
    private Handler mUpHandler = new Handler();
    private Handler mDownHandler = new Handler();
    private Runnable downanimateImage = new Runnable() {
        @Override
        public void run() {
            doDownTheAnimation(params.x, toLevel);
            windowManager.updateViewLayout(qview, params);
        }
    };

    private void doDownTheAnimation(int fromLevel, int toLevel) {
        fromLevel -= LEVEL_DIFF;
        if (fromLevel <= toLevel)
            fromLevel = toLevel;
        if (fromLevel > toLevel) {
            params.x = fromLevel;
            mDownHandler.postDelayed(downanimateImage, DELAY);
        } else {
            mDownHandler.removeCallbacks(downanimateImage);
            params.x = toLevel;
            windowManager.updateViewLayout(qview, params);
        }
    }

    private Runnable upanimateImage = new Runnable() {
        @Override
        public void run() {
            windowManager.updateViewLayout(qview, params);
            doUpTheAnimation(params.x, toLevel);

        }
    };

    private void doUpTheAnimation(int fromLevel, int toLevel) {
        fromLevel += LEVEL_DIFF;
        if (fromLevel >= toLevel)
            fromLevel = toLevel;
        if (fromLevel < toLevel) {
            params.x = fromLevel;

            mUpHandler.postDelayed(upanimateImage, DELAY);
        } else {
            mUpHandler.removeCallbacks(upanimateImage);
            // /Toast.makeText(this, ""+mLevel, Toast.LENGTH_SHORT).show();
            params.x = toLevel;
            windowManager.updateViewLayout(qview, params);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null)
            windowManager.removeView(qview);
       /* if (removeView != null)
            windowManager.removeView(removeView);*/
        showNotification("Voice Translator", "Turn on Quick Translate");
        shudownDocmau();

        if(mAdView != null){
            Log.d("mAdview: ","Destroied");
            mAdView.destroy();
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        if (intent != null) {
            if (intent.getFlags() == Intent.FLAG_ACTIVITY_NEW_TASK) {
                String sharetextin = "";
                boolean turnoff = intent.getBooleanExtra("turnoff", false);
                //boolean onservice = intent.getBooleanExtra("onservice", true);
                if (intent.getStringExtra("inputtext") != null) {
                    sharetextin = intent.getStringExtra("inputtext");
                    transnow = true;

                }
                if (turnoff)
                    if (sharetextin == null) sharetextin = "";
                //this.serviceintent = intent;
                if (!sharetextin.equalsIgnoreCase("")) {
                    mNgonNguIn.setmText(sharetextin);
                    mqtranslator = qtranslate();
                    mqtranslator.show();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private AdView mAdView;
    private Dialog qtranslate() {

        final ClipboardManager clipboard = (ClipboardManager) getApplicationContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        final Spinner spinnerout;
        final Spinner spinnerin;
        final EditText txt_textin;
        final EditText txt_textout;
        final ProgressBar pro_qtransResult;
        final ImageView delText, copyText, speakText,hoandoi;
        final LinearLayout lo_off;
        final Dialog dung = new Dialog(getApplicationContext(), R.style.FullHeightDialog);
        docmau.setLanguage(new Locale(mNgonNguOut.getmCodeDocmau()));
        dung.setContentView(R.layout.quick_translate);
        chatHead.setVisibility(View.GONE);
        dung.getWindow().setFormat(PixelFormat.TRANSPARENT);
        dung.getWindow().setBackgroundDrawable(null);
        dung.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        dung.setCancelable(true);
        dung.setCanceledOnTouchOutside(true);

        if (!noads) {
            if(mAdView!=null) mAdView.destroy();
            mAdView = new AdView(dung.getContext(), "1757797214490189_1969589469977628", AdSize.BANNER_HEIGHT_50);
            final LinearLayout adContainer = dung.findViewById(R.id.adsbanner_audience);
            adContainer.addView(mAdView);
            mAdView.loadAd();
            mAdView.setAdListener(new AbstractAdListener() {
                @Override
                public void onAdLoaded(Ad ad) {
                    super.onAdLoaded(ad);
                    adContainer.setVisibility(View.VISIBLE);
                }
            });
        }

        dung.findViewById(R.id.lo_qchathead).setVisibility(View.GONE);
        dung.findViewById(R.id.img_offqtran).setVisibility(View.VISIBLE);
        dung.findViewById(R.id.lo_qmain).setVisibility(View.VISIBLE);
        delText = dung.findViewById(R.id.img_qdeleteText);
        copyText = dung.findViewById(R.id.img_qcopyText);
        speakText = dung.findViewById(R.id.img_qspeakText);
        hoandoi = dung.findViewById(R.id.img_qhoandoi);
        pro_qtransResult = dung.findViewById(R.id.probar_qtranResult);
        lo_off = dung.findViewById(R.id.lo_offqtran);

        txt_textin = dung.findViewById(R.id.txt_qtextin);
        txt_textin.setText(mNgonNguIn.getmText());
        if (!mNgonNguIn.getmText().equalsIgnoreCase(""))
            delText.setVisibility(View.VISIBLE);


        txt_textout = dung.findViewById(R.id.txt_qtextout);
        txt_textout.setText(mNgonNguOut.getmText());
        if (!(mNgonNguOut.getmText().equalsIgnoreCase("") ||
                mNgonNguOut.getmText().equalsIgnoreCase("translate false")))
            copyText.setVisibility(View.VISIBLE);


        // Setup Spinner
        spinnerout = dung.findViewById(R.id.sp_qlangout);
        spinnerin = dung.findViewById(R.id.sp_qlangin);

        ArrayList<ItemSpinner> list = new ArrayList<>();
        ArrayList<ItemSpinner> listout = new ArrayList<>();

        QspinnerAdapter adapter = new QspinnerAdapter(getApplicationContext(),
                R.layout.qrow, R.id.qnamelanguage, list);
        for (int i = 0; i < commonResource.getContriesin().length; i++) {
            list.add(new ItemSpinner(commonResource.getContriesin()[i], commonResource.getFlagcontriesin().getResourceId(
                    i, -1)));
        }
        QspinnerAdapter adapterout = new QspinnerAdapter(getApplicationContext(),
                R.layout.qrow, R.id.qnamelanguage, listout);
        for (int i = 0; i < commonResource.getContriesout().length; i++) {
            listout.add(new ItemSpinner(commonResource.getContriesout()[i], commonResource.getFlagcontriesout()
                    .getResourceId(i, -1)));
        }
        spinnerout.setAdapter(adapterout);
        spinnerin.setAdapter(adapter);

        int spin = mNgonNguIn.getmPosition();
        int spout = mNgonNguOut.getmPosition();
        spinnerout.setSelection(spout);
        spinnerin.setSelection(spin);

        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable trans = new Runnable() {
            @Override
            public void run() {

                mNgonNguIn.setmText(txt_textin.getText().toString().trim());
                Boolean ketnoi = isOnline();
                if (ketnoi) {
                    //docmau.setLanguage(lc_docmau_out);
                    if(!mNgonNguIn.getmText().equalsIgnoreCase(""))
                        pro_qtransResult.setVisibility(View.VISIBLE);
                    YTranslatorCallBack mCallBack = new YTranslatorCallBack() {
                        @Override
                        public void getYTranslateCallBack(String responcse, String progressingName) {
                            if(progressingName.equalsIgnoreCase("QTRANSLATE")) {
                                mNgonNguOut.setmText(responcse);
                                txt_textout.setText(mNgonNguOut.getmText());
                                pro_qtransResult.setVisibility(View.GONE);
                                if (mNgonNguOut.isTextToSpeech())
                                    speakText.setVisibility(View.VISIBLE);
                                else
                                    speakText.setVisibility(View.GONE);
                            }
                        }
                    };
                    if (!mNgonNguIn.getmText().equalsIgnoreCase("")) {
                       micTranslatorYandex mTranslator =  new micTranslatorYandex(getApplicationContext(),mNgonNguIn.getmText(),
                               mNgonNguIn.getLanguageY(), mNgonNguOut.getLanguageY(),mCallBack,"QTRANSLATE");
                        mTranslator.execute();
                    }
                } else {
                    showToast("You must be connected to the internet first");
                    dung.cancel();
                }
            }
        };

        if (transnow) {
            handler.postDelayed(trans, DELAY_TRANS);
            transnow = false;
        }




        spinnerin.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (mNgonNguIn.getmPosition() != position) {
                    mNgonNguIn.updateNgonNguIn(commonResource.getContriesin()[position]);
                    handler.removeCallbacks(trans);
                    handler.postDelayed(trans, DELAY_TRANS);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        spinnerout.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                // TODO Auto-generated method stub
                if (mNgonNguOut.getmPosition() != position) {
                    mNgonNguOut.updateNgonNguOut(commonResource.getContriesout()[position]);
                    handler.removeCallbacks(trans);
                    handler.postDelayed(trans, DELAY_TRANS);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        txt_textin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    delText.setVisibility(View.VISIBLE);
                } else {
                    delText.setVisibility(View.GONE);
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
                handler.removeCallbacks(trans);
                handler.postDelayed(trans, DELAY_TRANS);

            }
        });

        delText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txt_textin.setText("");
                mNgonNguIn.setmText("");
                mNgonNguOut.setmText("");
                txt_textout.setText("");
            }
        });

        speakText.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                vib.vibrate(30);
                if (docmau.isSpeaking())
                    docmau.stop();
                docmau.speak(mNgonNguOut.getmText(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        txt_textout.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (txt_textout.getText().toString()
                        .equalsIgnoreCase("Translating......")
                        || txt_textout.getText().toString()
                        .equalsIgnoreCase("")
                        || txt_textout.getText().toString()
                        .equalsIgnoreCase("translate false")) {
                    copyText.setVisibility(View.GONE);
                    speakText.setVisibility(View.GONE);
                } else {
                    copyText.setVisibility(View.VISIBLE);
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

        copyText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!mNgonNguOut.getmText().equalsIgnoreCase("")) {
                    ClipData clip = ClipData.newPlainText("copy_out", mNgonNguOut.getmText());
                    clipboard.setPrimaryClip(clip);
                    showToast("Copied");
                    dung.cancel();
                }
            }
        });

		/*btn_translate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(dung.getCurrentFocus()
						.getWindowToken(), 0);
				vib.vibrate(30);
				btn_translate.setVisibility(View.GONE);
				pro_qtrans.setVisibility(View.VISIBLE);
				pro_qtransResult.setVisibility(View.VISIBLE);
				// TODO Auto-generated method stub
				textin = txt_textin.getText().toString().trim();
				Boolean ketnoi = isOnline();
				
				if (ketnoi) {
					if (!(Arrays.asList(nospeaker).contains(contriesout[spout]
							.toString()))) {
						checkdocmauout = true;
					} else {
						checkdocmauout = false;
					}
					str_docmau_out = lc_docmau_out.toString();
					docmau.setLanguage(new Locale(str_docmau_out));
					if (!textin.equalsIgnoreCase("")) {
						txt_textout.setText("");
						new micTranslatorYandex(textin, lagInY, lagOutY) {
							@Override
							protected void onPostExecute(String result) {
								textout = result;
								vib.vibrate(30);
								if (!textout.equals("translate false")) {
									// insert into database
									item_translate item = new item_translate(0,
											contriesin[spin],
											contriesout[spout], textin,
											textout, lc_docmau_in.toString(),
											lc_docmau_out.toString());
									// list_item.add(item);
									db.additem_translate(item);
								}
								txt_textout.setText(textout);
								btn_translate.setVisibility(View.VISIBLE);
								pro_qtrans.setVisibility(View.GONE);
								pro_qtransResult.setVisibility(View.GONE);
							}
						}.execute();
					} else {
						btn_translate.setVisibility(View.VISIBLE);
						pro_qtrans.setVisibility(View.GONE);
						pro_qtransResult.setVisibility(View.GONE);
					}
				} else {
					showToast("You must be connected to the internet first");
					dung.cancel();
				}
			}
		});*/
        lo_off.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dung.cancel();
            }
        });
		/*
		 * offqTran.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub vib.vibrate(30); dung.cancel(); Intent settingIntent =
		 * new Intent(ChatHeadService.this,SettingActivity.class);
		 * startActivity(settingIntent); } });
		 */

        hoandoi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Animation animationrotate = AnimationUtils.loadAnimation(
                        v.getContext(), R.anim.anim_rotate);
                animationrotate.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        vib.vibrate(50);
                        int spout = commonResource.getPosition(mNgonNguIn.getmLanguageName(),
                                                            commonResource.getContriesout());
                        int spin = commonResource.getPosition(mNgonNguOut.getmLanguageName(),
                                                            commonResource.getContriesin());
                        // Hoan Doi Ngon Ngu
                        spinnerin.setSelection(spin);
                        spinnerout.setSelection(spout);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }
                });
                hoandoi.startAnimation(animationrotate);
            }
        });
        dung.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                chatHead.setVisibility(View.GONE);
            }
        });
        dung.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                // chatHead.setVisibility(View.VISIBLE);
                // doDownTheAnimation(mLevel, backx);
                // qtrans.setVisibility(View.GONE);
                // dung.dismiss();

				/*
				 * if(turnoff && !onservice) { stopSelf();
				 * 
				 * }else{ chatHead.setVisibility(View.VISIBLE); }
				 */
                chatHead.setVisibility(View.VISIBLE);
                common_preferences.setmContryNameIn(mNgonNguIn.getmLanguageName());
                common_preferences.setmContryNameOut(mNgonNguOut.getmLanguageName());
				/*GoogleAnalytics.getInstance(ChatHeadService.this)
						.reportActivityStop(dung.getOwnerActivity());*/

            }
        });

        /*dung.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if(mAdView!=null) mAdView.destroy();
            }
        });*/

        return dung;
    }

    private void showNotification(String sub, String body) {
        // Put Notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.logo_small).setContentTitle(sub)
                .setContentText(body);
        Intent resultIntent = new Intent(this, ChatHeadService.class);
        resultIntent.putExtra("notification", true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // stackBuilder.addParentStack(WelcomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = PendingIntent.getService(this,
                Intent.FLAG_ACTIVITY_NEW_TASK /* Request code */, resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent).addAction(
                R.drawable.enter_icon, "Turn On", resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

    }

    void showToast(String text) {
        if (thongbao != null) {
            thongbao.cancel();
        }
        if (text.equalsIgnoreCase("")) {
            thongbao.cancel();
        } else {
            thongbao = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            thongbao.setGravity(Gravity.CENTER, 0, 0);
            thongbao.show();
        }

    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub
        String TAG = "TextToSpeech";
        if (status == TextToSpeech.LANG_MISSING_DATA
                || status == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e(TAG, "TTS missing or not supported (" + status + ")");
            showToast("Ngôn ngữ không hỗ trợ đọc!");
            // Language data is missing or the language is not supported.
            // showError(R.string.tts_lang_not_available);
        } else {

            // Initialization failed.
            Log.e(TAG, "Error occured");
        }

    }


    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        docmau.shutdown();
        /*flagcontriesin.();
        flagcontriesout.recycle();*/
        return super.onUnbind(intent);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();

    }

    /*
     * private AlertDialog AskTurnOffService(){ final AlertDialog.Builder
     * alertDialogBuilder = new AlertDialog.Builder(getBaseContext());
     * alertDialogBuilder.setMessage("Do you wanna turn off Quick Translate?");
     * alertDialogBuilder.setTitle("Quick Translate");
     * alertDialogBuilder.setIcon(R.drawable.logo_small);
     * alertDialogBuilder.setPositiveButton("Turn Off", new
     * DialogInterface.OnClickListener() {
     *
     * @TargetApi(23)
     *
     * @Override public void onClick(DialogInterface dialog, int which) {
     * dialog.cancel(); stopService(new Intent(ChatHeadService.this,
     * ChatHeadService.class)); return; } });
     *
     * alertDialogBuilder.setNeutralButton("Cancel",new
     * DialogInterface.OnClickListener() {
     *
     * @Override public void onClick(DialogInterface dialog, int which) {
     * dialog.cancel(); return; } }); alertDialogBuilder.setCancelable(false);
     * AlertDialog alertDialog = alertDialogBuilder.create();
     *
     *
     * return alertDialog;
     *
     * }
     */
    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        //stopSelf();
        showToast("Low memory");
        super.onLowMemory();
    }

    private int getsp(String countryname, String[] listcountry) {
        int trave = 0;
        for (int i = 0; i < listcountry.length; i++) {
            if (listcountry[i].equalsIgnoreCase(countryname)) {
                trave = i;
                return trave;
            }
        }
        return trave;
    }

    private int getStatusBarHeight() {
        int statusBarHeight = (int) Math.ceil(25 * getApplicationContext()
                .getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        return net != null && net.isAvailable() && net.isConnected();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
	
	/*private Tracker mTracker; 
	synchronized public Tracker getDefaultTracker() {
	    // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
		GoogleAnalytics sAnalytics;
		sAnalytics = GoogleAnalytics.getInstance(this);
	    if (mTracker == null) {
	      mTracker = sAnalytics.newTracker(R.xml.global_tracker);
	    }

	    return mTracker;
	  }*/
	private WindowManager.LayoutParams getlayoutparams(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            return new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSPARENT);
        else
            return new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSPARENT);
    }


    public void shudownDocmau() {
        if(docmau!=null){
            if(docmau.isSpeaking()) {
                Log.d("Text to speech","Run Stop TTS");
                docmau.stop();
            }
            Log.d("Text to speech","Run Shutdown TTS");
            docmau.shutdown();

        }

    }
    /*private void resultProgressText(String result){
        mNgonNguOut.setmText(result);
        txt_textout.setText(mNgonNguOut.getmText());
        pro_qtransResult.setVisibility(View.GONE);
        if (mNgonNguOut.isTextToSpeech())
            speakText.setVisibility(View.VISIBLE);
        else
            speakText.setVisibility(View.GONE);
    }*/
}