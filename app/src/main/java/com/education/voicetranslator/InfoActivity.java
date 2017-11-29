package com.education.voicetranslator;

import java.util.LinkedList;
import java.util.List;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.education.voicetranslator.common.Common;
import com.education.voicetranslator.common.Common_Preferences;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
//import android.provider.SyncStateContract.Constants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends Activity implements BillingProcessor.IBillingHandler{

	public BillingProcessor bp;
	private boolean noads;
	private Toast thongbao;
	private String versionname = null;
	private TextView version;
	private Button btnupdate;
	private String hauto = "";
	private final int WAIT_TIME = 600;
	private Tracker mTracker;
	private Common_Preferences common_preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		// Obtain the shared Tracker instance.
		//getDefaultTracker();
		common_preferences = new Common_Preferences(this);
		noads = common_preferences.ismNoAds();
		GoogleAnalytics sAnalytics = GoogleAnalytics.getInstance(this);
		if(mTracker==null){
			mTracker = sAnalytics.newTracker(R.xml.app_tracker);
		}
		setContentView(R.layout.info);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow()
					.addFlags(
							WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(
					getResources().getColor(R.color.maincolor));
			getWindow().setNavigationBarColor(
					getResources().getColor(R.color.maincolor));
		}

		AdView mAdView_info = findViewById(R.id.adView_info);
		AdRequest.Builder builder = new AdRequest.Builder();
		AdRequest adRequest = builder.build();
		mAdView_info.loadAd(adRequest);

		bp = new BillingProcessor(getBaseContext(), Common.base64EncodedPublicKey,
				"07718416122122078249", this);
		common_preferences.setmNoAds(bp.isPurchased("hotronhaphattrien"));
		Log.d("NoAds Version", "" + noads);
		ImageView logo = findViewById(R.id.logo_info);
		final TextView appname = findViewById(R.id.txt_nameApp_info);
		final TextView info = findViewById(R.id.txt_info);

		try {
			versionname = getPackageManager().getPackageInfo(getPackageName(),
					0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		version = findViewById(R.id.txt_App_ver);
		final TextView notenoads = findViewById(R.id.txt_notenoads);

		if (noads) {
			hauto = " NoAds";
			mAdView_info.setVisibility(View.GONE);
		}
		final ImageView back = findViewById(R.id.imgback_info);
		btnupdate = findViewById(R.id.btn_update_app);
		final Button btnshare = findViewById(R.id.btn_share_app);
		final Button btnnoads = findViewById(R.id.btn_noads);
		final LinearLayout lo_button = findViewById(R.id.share_update_info);
		version.setText("Current Version: " + versionname + hauto);
		final Vibrator vib = (Vibrator) this
				.getSystemService(Context.VIBRATOR_SERVICE);
		final ObjectAnimator anim = ObjectAnimator.ofFloat(logo, "rotationY",
				0.0f, 360f);
		anim.setDuration(2000);
		anim.setRepeatCount(0);
		anim.setInterpolator(new AccelerateDecelerateInterpolator());
		final Animation animAlphaHienlen1 = AnimationUtils.loadAnimation(this,
				R.anim.anim_alpha_hienlen);
		final Animation animAlphaHienlen2 = AnimationUtils.loadAnimation(this,
				R.anim.anim_alpha_hienlen);
		final Animation animAlphaHienlen3 = AnimationUtils.loadAnimation(this,
				R.anim.anim_alpha_hienlen);
		final Animation animAlphaHienlen4 = AnimationUtils.loadAnimation(this,
				R.anim.anim_alpha_hienlen);
		final Animation animAlphaHienlen5 = AnimationUtils.loadAnimation(this,
				R.anim.anim_alpha_hienlen);

		// final Intent mainIntent = new Intent(InfoActivity.this,MainActivity.class);
		anim.start();
		anim.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				appname.setVisibility(View.VISIBLE);
				appname.startAnimation(animAlphaHienlen1);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						info.setVisibility(View.VISIBLE);
						version.setVisibility(View.VISIBLE);
						version.startAnimation(animAlphaHienlen4);
						info.startAnimation(animAlphaHienlen2);

					}
				}, WAIT_TIME);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						back.setVisibility(View.VISIBLE);
						lo_button.setVisibility(View.VISIBLE);
						if (!noads) {
							btnnoads.setVisibility(View.VISIBLE);
							btnnoads.startAnimation(animAlphaHienlen5);
						}

						back.startAnimation(animAlphaHienlen3);
						lo_button.startAnimation(animAlphaHienlen3);
					}
				}, WAIT_TIME + 1000);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub

			}
		});
		btnupdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animationscale = AnimationUtils.loadAnimation(
						v.getContext(), R.anim.anim_scale);
				btnupdate.startAnimation(animationscale);
				vib.vibrate(40);
				animationscale.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {

						try {
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri
									.parse("market://details?id="
											+ getPackageName()));
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						} catch (android.content.ActivityNotFoundException anfe) {
							Intent intent = new Intent(
									Intent.ACTION_VIEW,
									Uri.parse("http://play.google.com/store/apps/details?id="
											+ getPackageName()));
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
					}
				});
			}
		});
		btnshare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animationscale = AnimationUtils.loadAnimation(
						v.getContext(), R.anim.anim_scale);
				btnshare.startAnimation(animationscale);
				vib.vibrate(40);
				animationscale.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Intent sharingIntent = new Intent(
								android.content.Intent.ACTION_SEND);
						sharingIntent.setType("text/plain");
						sharingIntent
								.putExtra(android.content.Intent.EXTRA_TEXT,
										"http://play.google.com/store/apps/details?id="+getPackageName());
						startActivity(Intent.createChooser(sharingIntent,
								"Share To Friends"));


					}
				});

			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animationscale = AnimationUtils.loadAnimation(
						v.getContext(), R.anim.anim_scale);
				back.startAnimation(animationscale);
				vib.vibrate(40);
				animationscale.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// startActivity(mainIntent);

						Intent i = new Intent(InfoActivity.this, MainActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
                        finish();
					}
				});
			}
		});

		btnnoads.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Animation animationscale = AnimationUtils.loadAnimation(
						v.getContext(), R.anim.anim_scale);
				btnnoads.startAnimation(animationscale);
				vib.vibrate(40);

				animationscale.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// bp.subscribe(InfoActivity.this, "voicetranslator.domate");
						if (BillingProcessor
								.isIabServiceAvailable(getBaseContext())) {
							bp.purchase(InfoActivity.this, "hotronhaphattrien");
							notenoads.setVisibility(View.VISIBLE);
						} else {
							showToast("In-app billing service is unavailable",
									Toast.LENGTH_LONG);
						}

						/*
						 * Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						 * Uri.parse("https://www.paypal.me/tmq902005"));
						 * startActivity(browserIntent);
						 */
					}
				});

			}
		});
		notenoads.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				restartapp();
			}
		});



	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(InfoActivity.this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i("InfoActivity", "SettingActivity screen name: " );
		mTracker.setScreenName("Info Application"+hauto);
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// if (bp != null) bp.release();
		super.onDestroy();
		bp.release();
	}

	void payingsuccessful() {
		noads = true;
		common_preferences.setmNoAds(noads);
	}

	void restartapp() {
		showToast("Restart Application", Toast.LENGTH_LONG);
		Intent i = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage(getBaseContext().getPackageName());
		startActivity(i);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!bp.handleActivityResult(requestCode, resultCode, data))
			super.onActivityResult(requestCode, resultCode, data);
	}

	void showToast(String text, int length) {
		if (thongbao != null) {
			thongbao.cancel();
		}
		if (text.equalsIgnoreCase("")) {
			thongbao.cancel();
		} else {
			thongbao = Toast.makeText(this, text, length);
			thongbao.setGravity(Gravity.CENTER, 0, 0);
			thongbao.show();
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
		Log.d("Billing Processor", "onBillingInitialized");
	}

	@Override
	public void onProductPurchased(String arg0, TransactionDetails arg1) {
		// TODO Auto-generated method stub
		payingsuccessful();
	}

	@Override
	public void onPurchaseHistoryRestored() {
		// TODO Auto-generated method stub

	}


	public boolean getUsername(String username) {
	    AccountManager manager = AccountManager.get(this);
	    Account[] accounts = manager.getAccountsByType("com.google");
	    List<String> possibleEmails = new LinkedList<String>();

	    for (Account account : accounts) {
	        // TODO: Check possibleEmail against an email regex or treat
	        // account.name as an email address only for certain account.type
	        // values.
	        possibleEmails.add(account.name);
	    }

	    
	    if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
	    	for (String string : possibleEmails) {
	    		String email = string;
		        String[] parts = email.split("@");
		        if (parts.length > 0 && parts[0] != null)
		            if(parts[0].toString().equalsIgnoreCase(username)){
		            	return true;
		            }
			}
	        return false;
	    } else
	        return false;
	}

}
