package com.education.voicetranslator.mic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.education.voicetranslator.callback.DownloadImageCallBack;

public class DownloadImage extends AsyncTask<Void, Integer, File> {
	private String strUrl = "";
	private File filesave;
	private Bitmap imgBitmap = null;
	private DownloadImageCallBack mDownloadImageCallBack;
	public DownloadImage(String strUrl, File ifile , DownloadImageCallBack mCallBack) {
		super();
		this.strUrl = strUrl;
		//this.imageview = img;
		this.filesave = ifile;
		this.mDownloadImageCallBack = mCallBack;
		Log.d("Filesave AsyscTask", filesave.getPath());
		
	}

	public File getCreateFileImage(Bitmap inBitmap){
		if(inBitmap != null){
			if(!strUrl.equalsIgnoreCase("")){
				try {
					FileOutputStream fo = new FileOutputStream(filesave);
                    inBitmap.compress(Bitmap.CompressFormat.PNG, 80, fo);
					Log.d("File size :", "" + filesave.length());
					Log.d("resizeimg",
							"Save File Image: " + filesave.getAbsolutePath());
                    inBitmap.recycle();
					fo.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return filesave;
		}else{
			return null;
		}
		
	}
	
		
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		Log.d("Chuan Bi Download :", strUrl);
		super.onPreExecute();
	}

    @Override
    protected File doInBackground(Void... voids) {

        return downloadFile(strUrl);
    }


    @Override
    protected void onPostExecute(File file) {
        mDownloadImageCallBack.getDownloadImageCallBack(file);
	    super.onPostExecute(file);
    }

    private File downloadFile(String fileUrl){
	      //URL myFileUrl =null;
	      Bitmap bmImg = null;
	      
	      try {
               URL myFileUrl= new URL(fileUrl);
	           HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
	           conn.setDoInput(true);
	           conn.setConnectTimeout(5000);
	           conn.setReadTimeout(60000);
	           conn.connect();
	           InputStream is = conn.getInputStream();
	           bmImg = BitmapFactory.decodeStream(is);
	           
	      } catch (IOException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	      }
	      return getCreateFileImage(bmImg);
	 }
	public Bitmap getfileBitmap(){
		return this.imgBitmap;
	}
	/*public static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}*/
}
