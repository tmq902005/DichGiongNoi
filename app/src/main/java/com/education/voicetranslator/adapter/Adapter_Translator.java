package com.education.voicetranslator.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import com.education.voicetranslator.R;
import com.education.voicetranslator.data.DataTranslate;
import com.education.voicetranslator.ngonngu.item_translate;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.common.internal.GetServiceRequest;
@SuppressWarnings("deprecation")
public class Adapter_Translator extends ArrayAdapter<item_translate> implements
		OnInitListener {

	private final Context context;
	private ArrayList<item_translate> item;
	// private item_translate curentitem;
	private TextToSpeech docmau;
	private Toast thongbao;
    private Animation animationscale;
	private Vibrator vib;
	private String[] nospeaker;
	private SharedPreferences preferences;
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
	public Adapter_Translator(Context incontext,
			ArrayList<item_translate> objects) {
		super(incontext, R.layout.list_translator, objects);
		this.context = incontext;
		this.item = objects;
		// this.docmau = docmau;
        setupdocmau();
		vib = (Vibrator) this.context
				.getSystemService(Context.VIBRATOR_SERVICE);
		nospeaker = context.getResources().getStringArray(R.array.noSpeaker);
		preferences = this.context.getSharedPreferences("quanly_request", Context.MODE_PRIVATE);
		// TODO Auto-generated constructor stub

	}

	
	@Override
	public void add(item_translate object) {
		// TODO Auto-generated method stub

		super.add(object);
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return super.getContext();
	}

	@Override
	public int getPosition(item_translate item) {
		// TODO Auto-generated method stub
		return super.getPosition(item);
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

	public int getamluong() {
		AudioManager amthanh = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		int amluong = amthanh.getStreamVolume(AudioManager.STREAM_MUSIC);
		return amluong;
	}
	private NativeExpressAdView mAdView;
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_translator, parent, false);
		TextView textin, textout;
		TextView lagin, lagout;
		final String strdocin, strdocout;
		View curView = rowView.getRootView();

		//db = new DataTranslate(rowView.getContext());
		boolean noads = true;
		noads = preferences.getBoolean("noads", true);
		final LinearLayout lo_ads_listtran;
		lo_ads_listtran = rowView.findViewById(R.id.lo_ads_listtran);
		//noads = false;
		if (noads) {
			lo_ads_listtran.setVisibility(View.GONE);
		} else if(position==0) {

			//AdSize adsize = new AdSize(AdSize.FULL_WIDTH, 132);
			//AdSize newadsize = new AdSize(getScreenWidthInDPs(context), 132);
			if(mAdView!=null) mAdView.destroy();
			mAdView = new NativeExpressAdView(context);
			mAdView.setAdSize(new AdSize(getScreenWidthInDPs(context)-22, 132));
			mAdView.setAdUnitId("ca-app-pub-0674483990944026/8267856854");
			lo_ads_listtran.addView(mAdView);
			//mAdView.setAdSize(newadsize);
			//mAdView.setAdSize(AdSize.FULL_BANNER);
			// mAdView.setAdSize(AdSize.BANNER);
			AdRequest.Builder builder = new AdRequest.Builder();
			AdRequest adRequest = builder.build();
			mAdView.loadAd(adRequest);
			mAdView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					// TODO Auto-generated method stub
					super.onAdLoaded();
					lo_ads_listtran.setVisibility(View.VISIBLE);
				}
			});
		}else{
			//lo_ads_listtran.setVisibility(View.GONE);
		}
		// final int po = position;
		final item_translate citem = this.item.get(position);
		// curentitem = citem;
		textin = rowView.findViewById(R.id.txt_list_textin);
		textout = rowView.findViewById(R.id.txt_list_textout);
		lagin = rowView.findViewById(R.id.txt_list_lagin);
		lagout = rowView.findViewById(R.id.txt_list_lagout);
		textin.setText(citem.getTextin());
		textout.setText(citem.getTextout());
		lagin.setText(citem.getLagin());
		lagout.setText(citem.getLagout());
		strdocin = citem.getDocmauin();
		strdocout = citem.getDocmauout();
		animationscale = AnimationUtils.loadAnimation(this.context,
				R.anim.anim_scale);

		final ImageView share_in;
		final ImageView copy_in;
		final ImageView speaker_in;
		final ImageView share_out;
		final ImageView copy_out;
		final ImageView speaker_out;
		final ImageView delete_item;
		final RelativeLayout layout_item = rowView
				.findViewById(R.id.layout_item);
		final ClipboardManager clipboard = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		final String str_out = textout.getText().toString();
		final String str_in = textin.getText().toString();
		/*
		 * NativeExpressAdView mAdView; mAdView = (NativeExpressAdView )
		 * rowView.findViewById(R.id.adView_Qtran_list); if(noads){
		 * mAdView.setVisibility(View.GONE); }else{ AdRequest.Builder builder =
		 * new AdRequest.Builder(); AdRequest adRequest = builder.build();
		 * mAdView.loadAd(adRequest); //}
		 */
		delete_item = rowView.findViewById(R.id.img_deleteItem);
		delete_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animTranslate = AnimationUtils.loadAnimation(context,
						R.anim.anim_translate);

				layout_item.startAnimation(animTranslate);
				vib.vibrate(30);
				showToast("Delete This Item");
				final DataTranslate db = new DataTranslate(context);
				animTranslate.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						remove(citem);
						db.delItem(citem);
						thongbao.cancel();
						/*
						 * if(ad_trans.getCount()==0){
						 * img_clear.setVisibility(View.GONE); }
						 */

					}
				});
			}
		});

		copy_out = rowView.findViewById(R.id.img_listview_copy_out);
		copy_out.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Creates a new text clip to put on the clipboard
				copy_out.startAnimation(animationscale);
				ClipData clip = ClipData.newPlainText("copy_out", str_out);
				clipboard.setPrimaryClip(clip);
				showToast("Translate Copied");
				vib.vibrate(40);
			}
		});

		copy_in = rowView.findViewById(R.id.img_listview_copy_in);
		copy_in.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Creates a new text clip to put on the clipboard
				copy_in.startAnimation(animationscale);
				ClipData clip = ClipData.newPlainText("copy_in", str_in);
				clipboard.setPrimaryClip(clip);
				showToast("Translate Copied");
				vib.vibrate(40);
			}
		});

		speaker_out = rowView
				.findViewById(R.id.img_listview_speaker_out);
		speaker_out.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				speaker_out.startAnimation(animationscale);
				vib.vibrate(40);
				if (getamluong() > 0) {
				    //setupdocmau();
					Locale docout = new Locale(strdocout);
					docmau.setLanguage(docout);
					docmau.speak(str_out, TextToSpeech.QUEUE_FLUSH, null);

				} else {
					showToast("Please adjust the volume to listen translation");
				}
			}
		});

		speaker_in = rowView
				.findViewById(R.id.img_listview_speaker_in);
		speaker_in.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				speaker_in.startAnimation(animationscale);
				vib.vibrate(40);
				if (getamluong() > 0) {
				    //setupdocmau();
					Locale docin = new Locale(strdocin);
					docmau.setLanguage(docin);
					docmau.speak(str_in, TextToSpeech.QUEUE_FLUSH, null);

				} else {
					showToast("Please adjust the volume to listen translation");
				}
			}
		});

		final boolean dieukien_in;
		final boolean dieukien_out;
		// Locale vi = new Locale(Language.VIETNAMESE.toString());
		if (Arrays.asList(nospeaker).contains(citem.getLagout())) {
			dieukien_out = false;
			speaker_out.setVisibility(View.INVISIBLE);
		} else {
			dieukien_out = true;
		}
		if (Arrays.asList(nospeaker).contains(citem.getLagin())) {
			dieukien_in = false;
			speaker_in.setVisibility(View.INVISIBLE);
		} else {
			dieukien_in = true;
		}

		share_in = rowView.findViewById(R.id.img_listview_share_in);
		share_in.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				share_in.startAnimation(animationscale);
				vib.vibrate(40);
				boolean inorout = true;
				shareItem(citem, str_in, dieukien_in, inorout).show();
			}
		});

		share_out = rowView
				.findViewById(R.id.img_listview_share_out);
		share_out.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				share_out.startAnimation(animationscale);
				vib.vibrate(40);
				boolean inorout = false;
				shareItem(citem, str_out, dieukien_out, inorout).show();
            }
		});
		/*
		 * if(!flat){ Animation aniamtion_alpha_hienlen =
		 * AnimationUtils.loadAnimation(context, R.anim.anim_alpha_hienlen);
		 * rowView.startAnimation(aniamtion_alpha_hienlen); flat = true; }
		 */
		return rowView;
	}

	// Test function
	/*
	 * public View getViewItem(item_translate item){
	 * 
	 * return rowView; }
	 */

	@Override
	public void insert(item_translate object, int index) {
		// TODO Auto-generated method stub.
		vib.vibrate(40);
		if (!(Arrays.asList(nospeaker).contains(object.getLagout()))) {
			if (getamluong() > 0) {
				Locale docmauout = new Locale(object.getDocmauout());
				docmau.setLanguage(docmauout);
				if (object.getTextout().length() <= 150) {
					docmau.speak(object.getTextout(), TextToSpeech.QUEUE_FLUSH,
							null);
				}
			} else {
				showToast("Please adjust the volume to listen translation");
			}
		}
		super.insert(object, index);
	}

	/*public item_translate getitem(int pointion) {
		return item.get(pointion);
	}*/

	
	@Override
	public void remove(item_translate object) {
		deletefileitem(object);
		super.remove(object);
	}


    @Override
	public void clear() {
		// TODO Auto-generated method stub
		/*String exStoragePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File appTmpPath = new File(exStoragePath + "/VoiceTranslator/");
		DeleteRecursive(appTmpPath);*/
		super.clear();
	}

	/*void DeleteRecursive(File fileOrDirectory) {

		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				child.delete();

		// fileOrDirectory.delete();

	}*/

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		String TAG = "TextToSpeech";
		if (status == TextToSpeech.SUCCESS)
			Log.e(TAG,"Success");
		else
			Log.e(TAG, "Error occured");
	}
	
	
	private void showToast(String text) {
		if (thongbao != null) {
			thongbao.cancel();
		}
		if (text.equalsIgnoreCase("")) {
			thongbao.cancel();
		} else {
			thongbao = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			thongbao.setGravity(Gravity.CENTER, 0, 0);
			thongbao.show();
		}

	}

	private void shareaudio(item_translate citem, boolean inorout) {
		// Hieu chinh am thanh
		final AudioManager amthanh;
		final int amluong;
		final File audioFile;
		amthanh = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		amluong = amthanh.getStreamVolume(AudioManager.STREAM_MUSIC);
		String sub;
		if (inorout) {
			sub = citem.getTextin();
			audioFile = getfilein(citem);
		} else {
			sub = citem.getTextout();
			audioFile = getfileout(citem);
		}
		final String extraText = sub;
		// final String sub1 = sub;
		if (audioFile != null) {
			if (!audioFile.exists()) {
				// Set am thanh = 0 để đọc mẫu
				final ProgressDialog progressDialog = ProgressDialog.show(
						context, "", "Loading...");
				amthanh.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
				if (inorout) {
					// sub = citem.getTextin();
					// namefile = citem.getLagin();
					Locale doc = new Locale(citem.getDocmauin());
					docmau.setLanguage(doc);
					docmau.speak(sub, TextToSpeech.QUEUE_FLUSH, null);
				} else {
					Locale doc = new Locale(citem.getDocmauout());
					docmau.setLanguage(doc);
					docmau.speak(sub, TextToSpeech.QUEUE_FLUSH, null);
				}
				HashMap<String, String> myHashRender = new HashMap<String, String>();
				myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
						sub);
				docmau.synthesizeToFile(sub, myHashRender, audioFile.getPath());
				docmau.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener() {
					@Override
					public void onUtteranceCompleted(String utteranceId) {

						if (docmau.isSpeaking()) {
							docmau.stop();
						}
						progressDialog.dismiss();
						amthanh.setStreamVolume(AudioManager.STREAM_MUSIC,
								amluong, 0);
						Intent sharingIntent = new Intent(
								android.content.Intent.ACTION_SEND);
						sharingIntent.setType("audio/*");
						// sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						// "Translate with Voice Translator App");
						sharingIntent.putExtra(Intent.EXTRA_STREAM,
								Uri.fromFile(audioFile));
						/*
						 * sharingIntent.putExtra(Intent.EXTRA_TEXT, sub1);
						 * sharingIntent.putExtra(Intent.EXTRA_TITLE, sub1);
						 * sharingIntent.putExtra(Intent.EXTRA_SUBJECT, sub1);
						 */
						sharingIntent
								.putExtra(
										Intent.EXTRA_TEXT,
										extraText
												+ "\n\n Voice Translator \nhttps://play.google.com/store/apps/details?id=com.education.voicetranslator");
						context.startActivity(Intent.createChooser(
								sharingIntent, "Share audio translation"));
					}
				});
			} else {
				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				sharingIntent.setType("audio/*");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"Translate with Voice Translator App");
				sharingIntent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(audioFile));
				// sharingIntent .putExtra(android.content.Intent.EXTRA_TEXT,
				// sub);
				
				/* * sharingIntent.putExtra(Intent.EXTRA_TEXT, sub1);
				 * sharingIntent.putExtra(Intent.EXTRA_TITLE, sub1);
				 * sharingIntent.putExtra(Intent.EXTRA_SUBJECT, sub1);
				 */
				sharingIntent
						.putExtra(
								Intent.EXTRA_TEXT,
								extraText
										+ "\n\n Voice Translator \nhttps://play.google.com/store/apps/details?id=com.education.voicetranslator");
				context.startActivity(Intent.createChooser(sharingIntent,
						"Share audio translation"));
			}
		} else {
			showToast("File Audio In Not Found");
		}
	}

	private void sharetext(String sub) {

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		// sharingIntent.putExtra(android.content.Intent.EXTRA_TITLE,
		// "Voice Translator - Free");
		// String href =
		// "http://play.google.com/store/apps/details?id=com.education.voicetranslator";
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sub);
		context.startActivity(Intent.createChooser(sharingIntent,
				"Share translated text"));
	}

	private AlertDialog shareItem(final item_translate item, final String sub,
			final boolean dieukien, final boolean inorout) {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setMessage("Do You Want to Share with?");

		alertDialogBuilder.setPositiveButton("Share Text",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						sharetext(sub);

					}
				});
		if (dieukien) {
			alertDialogBuilder.setNegativeButton("Share Voice",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							shareaudio(item, inorout);
						}
					});
		}
		return alertDialogBuilder.create();

	}

	private void deletefileitem(item_translate item) {
		String exStoragePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File appTmpPath = new File(exStoragePath + "/VoiceTranslate/");
		if (appTmpPath.isDirectory()) {
			ArrayList<File> filename = new ArrayList<File>();
			filename.add(getfilein(item));
			filename.add(getfileout(item));
			for (File file : filename) {
				if (file != null)
					file.delete();
			}
		}

	}

	private File getfilein(item_translate item) {
		String exStoragePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File appTmpPath = new File(exStoragePath + "/VoiceTranslator/");
		if (!appTmpPath.isDirectory()) {
			appTmpPath.mkdir();
			appTmpPath.setWritable(true, false);
		}
		File trave = null;
		String sub5string;
		sub5string = getsubstring(item.getTextin().toString(), 5);
		if (appTmpPath.isDirectory()) {
			String strfile = appTmpPath.getAbsolutePath() + "/translate_"
					+ item.getLagin() + "_" + item.getId() + "_" + sub5string + ".wav";
			trave = new File(strfile);
		}
		return trave;
	}

	private File getfileout(item_translate item) {
		String exStoragePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File appTmpPath = new File(exStoragePath + "/VoiceTranslator/");
		if (!appTmpPath.isDirectory()) {
			appTmpPath.mkdir();
			appTmpPath.setWritable(true, false);
		}
		File trave = null;
		String sub5string;
		sub5string = getsubstring(item.getTextout().toString(), 5);
		if (appTmpPath.isDirectory()) {
			String strfile = appTmpPath.getAbsolutePath() + "/VoiceTranslator_"
					+ item.getLagout() + "_" + item.getId() + "_" + sub5string + ".wav";
			trave = new File(strfile);
		}
		return trave;
	}

	private String getsubstring(String str,int so){
		if(str!=null){
			if(str.length()<=so){
				return str;
			}else{
				return str.substring(0, so);
			}
		}else{
			return "";
		}
	}
	public static int getScreenWidthInDPs(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int widthInDP = Math.round(dm.widthPixels / dm.density);
        return widthInDP;
    }

    private void setupdocmau(){
        if(docmau==null){
            this.docmau = new TextToSpeech(this.getContext(), this,
                    "com.google.android.tts");
            this.docmau.setSpeechRate((float) 0.8);
            this.docmau.setPitch((float) 0.9);
        }

    }
}
