package com.education.voicetranslator;



import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class SettingActivity extends Activity {

	private Switch swqtrans;
	private TextView desqtrans, gotosetting;
	private Boolean overlaysPermission = true;
	private ImageView imgback_setting;
	private Boolean tosetting = false;
	private Tracker mTracker;
	private GoogleAnalytics sAnalytics;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		swqtrans = findViewById(R.id.sw_Qtrans);
		desqtrans = findViewById(R.id.txt_desQtrans);
		gotosetting = findViewById(R.id.txt_gotosetting);
		imgback_setting = findViewById(R.id.imgback_setting);
		// Check if the application has draw over other apps permission or not?
		// This permission is by default available for API<23. But for API > 23
		// you have to ask for the permission in runtime.
		//getDefaultTracker();
		sAnalytics = GoogleAnalytics.getInstance(this);
		if(mTracker==null){
			mTracker = sAnalytics.newTracker(R.xml.app_tracker);
		}
		Bundle bundle = getIntent().getExtras();
		// Yeu Cau Di Vu Tu Notification
		if (bundle != null) {
			if (bundle.getBoolean("notification")) {
				Intent intent = new Intent(SettingActivity.this, ChatHeadService.class);
				intent.putExtra("inputtext", "");
				intent.putExtra("onservice", true);
				intent.putExtra("turnoff", false);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				if(!isFinishing())
				startService(intent);
				//finish();
			}
		}
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			overlaysPermission = Settings.canDrawOverlays(this);
		else
			overlaysPermission = true;

		if (isMyServiceRunning(ChatHeadService.class))
			swqtrans.setChecked(true);
		else
			swqtrans.setChecked(false);

		swqtrans.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					if (overlaysPermission) {
						// if(!isMyServiceRunning(ChatHeadService.class)){

						Intent intent = new Intent(SettingActivity.this,
								ChatHeadService.class);
						intent.putExtra("inputtext", "");
						intent.putExtra("onservice", true);
						intent.putExtra("turnoff", false);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						if(!isFinishing())
						startService(intent);

						// }
					} else {
						AskPermissionOverlay().show();
					}

				} else {
					if (isMyServiceRunning(ChatHeadService.class))
						stopService(new Intent(SettingActivity.this,
								ChatHeadService.class));
				}
				// showNotification("Voice Translator","Touch here for turn on/off Quick Translate");
			}
		});
		gotosetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tosetting = true;
				Intent intent = new Intent(
						Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri
								.parse("package:" + getPackageName()));

				startActivity(intent);
			}
		});
		imgback_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (!Settings.canDrawOverlays(this)) {
				desqtrans
						.setText("You must request permission on SettingActivity before turn on Quick Translate");
				gotosetting.setVisibility(View.VISIBLE);
				overlaysPermission = false;
				// swqtrans.setChecked(false);
			} else {
				desqtrans
						.setText("Quick Translate will display as on-screen bubble");
				gotosetting.setVisibility(View.GONE);
				overlaysPermission = true;
				// swqtrans.setChecked(true);
			}
		}
		mTracker.setScreenName("SettingActivity Application");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		super.onResume();
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
	private AlertDialog AskPermissionOverlay() {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder
				.setMessage("You must request permission on SettingActivity before turn on Quick Translate");
		alertDialogBuilder.setTitle("Quick Translate");
		alertDialogBuilder.setIcon(R.drawable.logo_small);
		swqtrans.setChecked(false);
		alertDialogBuilder.setPositiveButton("Go SettingActivity Now!",
				new DialogInterface.OnClickListener() {
					@TargetApi(23)
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						tosetting = true;
						Intent intent = new Intent(
								Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri
										.parse("package:" + getPackageName()));
						startActivity(intent);
						return;
					}
				});

		alertDialogBuilder.setNeutralButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						return;
					}
				});
		alertDialogBuilder.setCancelable(false);
		AlertDialog alertDialog = alertDialogBuilder.create();

		return alertDialog;

	}

	/*
	 * private void showNotification(String sub,String body){ // Put
	 * Notification NotificationCompat.Builder mBuilder = new
	 * NotificationCompat.Builder(this) .setSmallIcon(R.drawable.logo_small)
	 * .setContentTitle(sub) .setContentText(body); Intent resultIntent = new
	 * Intent(this, SettingActivity.class); TaskStackBuilder stackBuilder =
	 * TaskStackBuilder.create(this);
	 * stackBuilder.addParentStack(welcome.class);
	 * stackBuilder.addNextIntent(resultIntent); PendingIntent
	 * resultPendingIntent = stackBuilder.getPendingIntent( 0,
	 * PendingIntent.FLAG_UPDATE_CURRENT ); mBuilder
	 * .setContentIntent(resultPendingIntent) .addAction(R.drawable.abc_ic_go,
	 * "SettingActivity", resultPendingIntent); NotificationManager mNotificationManager
	 * = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	 * mNotificationManager.notify(0, mBuilder.build());
	 * 
	 * }
	 */

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		/*if (!tosetting)
			finish();*/
	}
}
