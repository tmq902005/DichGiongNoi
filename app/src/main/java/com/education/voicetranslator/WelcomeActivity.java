package com.education.voicetranslator;

import java.util.Locale;
import java.util.Random;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.education.voicetranslator.common.Common;
import com.education.voicetranslator.common.Common_Preferences;
import com.education.voicetranslator.common.Common_Resource;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends Activity implements BillingProcessor.IBillingHandler {

	private final int WAIT_TIME = 500;
	private BillingProcessor bp;
	private Tracker mTracker;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		GoogleAnalytics sAnalytics = GoogleAnalytics.getInstance(this);
        if(mTracker==null){
            mTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }
        FirebaseInstanceId.getInstance().getToken();

		setContentView(R.layout.welcome);
		Common_Resource commonResource = new Common_Resource(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow()
					.addFlags(
							WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(
					getResources().getColor(R.color.maincolor));
			getWindow().setNavigationBarColor(
					getResources().getColor(R.color.maincolor));
		}
		ImageView logo = findViewById(R.id.logo_welcome);
		final TextView sayHello = findViewById(R.id.txt_sayHello);
		final ImageView quocky = findViewById(R.id.img_quocky);
		bp = new BillingProcessor(getBaseContext(), Common.base64EncodedPublicKey,
				"07718416122122078249", this);
		Log.d("NoAds Version From BP",
				" " + bp.isPurchased("hotronhaphattrien"));
		boolean noads = bp.isPurchased("hotronhaphattrien");

		TypedArray flagcontries = commonResource.getFlagcontriesout();
		String[] loichao = commonResource.getHelloWelcome();
		String[] idlanguage = commonResource.getCodelangout();

		String curidlanguage = Locale.getDefault().getLanguage();
		Log.d("Current Language",curidlanguage);

		int vitrilanguage = getsp(curidlanguage,idlanguage);

		GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
		sayHello.setText(loichao[vitrilanguage]);
		quocky.setImageResource(flagcontries.getResourceId(vitrilanguage, 1));
		final ObjectAnimator anim = ObjectAnimator.ofFloat(logo, "rotationY",
				0.0f, 360f);
		anim.setDuration(1600);
		// anim.setRepeatCount(1);
		anim.setInterpolator(new AccelerateDecelerateInterpolator());
		final Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
		final Intent guideIntent = new Intent(WelcomeActivity.this, GuideActivity.class);
		final Intent goIntent;

		Common_Preferences common_preferences = new Common_Preferences(this);
		int inapp = common_preferences.getmInApp();
		common_preferences.setmNoAds(noads);
		if (inapp > 0) {
			goIntent = mainIntent;
		} else {
			common_preferences.setmInApp(1);
			goIntent = guideIntent;
		}
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

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {

						if (getIntent().getExtras() != null) {
							for (String key : getIntent().getExtras().keySet()) {
								Object value = getIntent().getExtras().get(key);
								Log.d("MainActivity: ", "Key: " + key + " Value: " + value);
							}
						}

						startActivity(goIntent);
						finish();
					}
				}, WAIT_TIME);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mTracker.setScreenName("Welcome Application");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		bp.release();
		super.onDestroy();
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	@Override
	public void onBillingError(int arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBillingInitialized() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProductPurchased(String arg0, TransactionDetails arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPurchaseHistoryRestored() {
		// TODO Auto-generated method stub
		
	}

    private int getsp(String countryname, String[] listcountry) {
        int trave = 0;
        for (int i = 0; i < listcountry.length; i++) {
            if (listcountry[i].equalsIgnoreCase(countryname)) {
                trave = i;
                return trave;
            }
        }
        return getsp("en", listcountry);
    }
}
