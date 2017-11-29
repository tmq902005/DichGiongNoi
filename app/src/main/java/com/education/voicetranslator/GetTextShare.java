package com.education.voicetranslator;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

public class GetTextShare extends Activity {
	boolean overlaysPermission, onservice, turnoff = false;
	Tracker mTracker;
	@TargetApi(23)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_text_share);
		// Get intent, action and MIME type
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		turnoff = true;
		GoogleAnalytics sAnalytics = GoogleAnalytics.getInstance(this);
		if(mTracker==null){
			mTracker = sAnalytics.newTracker(R.xml.app_tracker);
		}
		mTracker.setScreenName("GetTextShare");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			overlaysPermission = Settings.canDrawOverlays(this);
		else
			overlaysPermission = true;

		if (isMyServiceRunning(ChatHeadService.class)) {
			overlaysPermission = true;
			onservice = true;
		}

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
				if(sharedText==null){
					sharedText = "";
				}
				if (!overlaysPermission) {
					AskPermissionOverlay().show();
				} else {
					Intent serIntent = new Intent(GetTextShare.this,
							ChatHeadService.class);
					serIntent.putExtra("inputtext", sharedText);
					serIntent.putExtra("turnoff", turnoff);
					serIntent.putExtra("onservice", onservice);
					serIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					PendingIntent pendingIntent = PendingIntent.getService(this,1,serIntent,PendingIntent.FLAG_UPDATE_CURRENT);
					try {
						pendingIntent.send();
					} catch (PendingIntent.CanceledException e) {
						e.printStackTrace();
					}
					//startService(serIntent);
						finish();
				}
			}
		}

		if (Intent.ACTION_PROCESS_TEXT.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				String sharedText = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT);
				if(sharedText==null){
					sharedText = "";
				}
				if (!overlaysPermission) {
					AskPermissionOverlay().show();
				} else {
					Intent serIntent = new Intent(GetTextShare.this,
								ChatHeadService.class);
					serIntent.putExtra("inputtext", sharedText);
					serIntent.putExtra("turnoff", turnoff);
					serIntent.putExtra("onservice", onservice);
					serIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					PendingIntent pendingIntent = PendingIntent.getService(this,1,serIntent,PendingIntent.FLAG_UPDATE_CURRENT);
					try {
						pendingIntent.send();
					} catch (PendingIntent.CanceledException e) {
						e.printStackTrace();
					}
					finish();
				}
			}
		}
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		if (manager != null) {
			for (RunningServiceInfo service : manager
                    .getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
		}
		return false;
	}

	private AlertDialog AskPermissionOverlay() {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder
				.setMessage("You must request permission on SettingActivity before turn on Quick Translate");
		alertDialogBuilder.setTitle("Quick Translate");
		alertDialogBuilder.setIcon(R.drawable.logo_small);
		alertDialogBuilder.setPositiveButton("Go SettingActivity Now!",
				new DialogInterface.OnClickListener() {
					@TargetApi(23)
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						Intent intent = new Intent(
								Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri
										.parse("package:" + getPackageName()));
						startActivity(intent);
						finish();
					}
				});

		alertDialogBuilder.setNeutralButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						finish();
					}
				});
		alertDialogBuilder.setCancelable(false);

		return alertDialogBuilder.create();

	}

}
