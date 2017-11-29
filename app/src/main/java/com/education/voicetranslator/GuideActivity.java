package com.education.voicetranslator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import com.education.voicetranslator.common.Common_Preferences;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class GuideActivity extends Activity {
    private SharedPreferences pre_permissions;
    private String[] listpers = { "android.permission.RECORD_AUDIO",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
			//"android.permission.CAMERA"
            //"android.permission.ACTION_MANAGE_OVERLAY_PERMISSION"
    };

    private Tracker mTracker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.app_guide);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow()
					.addFlags(
							WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(
					getResources().getColor(R.color.maincolor));
			getWindow().setNavigationBarColor(
					getResources().getColor(R.color.maincolor));
		}
		GoogleAnalytics sAnalytics = GoogleAnalytics.getInstance(this);
        if(mTracker==null){
            mTracker = sAnalytics.newTracker(R.xml.app_tracker);
        }
		pre_permissions = getSharedPreferences("request_permissions",
				MODE_PRIVATE);
        Boolean ktdichvu = SpeechRecognizer.isRecognitionAvailable(this);
		if (!ktdichvu) {
			yeucaucaidatstt().show();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			ArrayList<String> perms = new ArrayList<String>(
					Arrays.asList(listpers));
			int permsRequestCode = 200;
            ArrayList<String> permissionsToRequest = findUnAskedPermissions(perms);
			if (permissionsToRequest.size() > 0) {
				requestPermissions(
						permissionsToRequest.toArray(new String[permissionsToRequest
								.size()]), permsRequestCode);
			}
		}

		final LinearLayout lo_next = findViewById(R.id.lo_next_guide);
        lo_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animationscale = AnimationUtils.loadAnimation(
						v.getContext(), R.anim.anim_scale);
				lo_next.startAnimation(animationscale);
				animationscale.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Intent mainIntent = new Intent(GuideActivity.this,
								MainActivity.class);
						startActivity(mainIntent);
                        finish();
					}
				});

			}
		});

        setDefaultLanguage();
	}

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(GuideActivity.this,
                MainActivity.class);
        startActivity(mainIntent);
        finish();
        super.onBackPressed();
    }

    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mTracker.setScreenName("Guide Application");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

    private int getspcode(String contryname, String[] listcountry) {
        int trave = 0;
        if(!contryname.equalsIgnoreCase("en"))
            for (int i = 0; i < listcountry.length; i++) {
                if (listcountry[i].equalsIgnoreCase(contryname)) {
                    trave = i;
                    return trave;
                }
            }
        else
            return getspcode("hi", listcountry);

        return getspcode("hi", listcountry);
    }

    private void setDefaultLanguage(){
		Common_Preferences common_preferences = new Common_Preferences(this);
        // Get Default Language code
        String dlangcode = Locale.getDefault().getLanguage();
        String[] arrlangcode = getResources().getStringArray(R.array.str_code_language_out);
        String[] arrlangname = getResources().getStringArray(R.array.str_language_out);
        int sp_out = getspcode(dlangcode,arrlangcode);
        common_preferences.setmContryNameOut(arrlangname[sp_out]);
    }

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		super.onStop();
		// speech.destroy();
	}



	// Park Request Permissions Android M
	private boolean shouldWeAsk(String permission) {
		return (pre_permissions.getBoolean(permission, true));
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

	private AlertDialog yeucaucaidatstt() {
		AlertDialog.Builder tbcaidatstt = new AlertDialog.Builder(this);

		// tbtrudiem.setTitle("Thông báo");
		tbcaidatstt
				.setMessage("Please Install Google Voice Service For Voice Translate");
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
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(i);
						} catch (android.content.ActivityNotFoundException anfe) {
						    Intent j = new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.googlequicksearchbox"));
							j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						    startActivity(j);
						}
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

}
