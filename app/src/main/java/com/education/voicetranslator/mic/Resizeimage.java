package com.education.voicetranslator.mic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.education.voicetranslator.callback.ResizeImageCallBack;
import com.education.voicetranslator.common.Common;


@SuppressWarnings("deprecation")
public class Resizeimage extends AsyncTask<Void, Long, File> {

	private File infile,outfile;
	private String filepath;
	private Bitmap imgbitmap;
	private ProgressDialog progressDialog;
	private ResizeImageCallBack mCallBack;
	public Resizeimage(String filepath, ResizeImageCallBack mCallBack) {
		super();
		this.filepath = filepath;
		this.infile = new File(filepath);
		this.outfile = infile;
		this.mCallBack = mCallBack;
		setimgbit(decodeFile(infile, Common.IMAGE_MAX_SIZE));
	}
	
	@Override
	protected File doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Log.d("Resizeimage", "doInBackground");
		outfile = resizeimg(filepath);
		return outfile;
	}

    @Override
    protected void onPostExecute(File file) {
	    mCallBack.getResizeImageCallBack(file,getimgbit());
        super.onPostExecute(file);
    }

    private File resizeimg(String filepath) {

		
		File rawfile = new File(filepath);
		long freespace = rawfile.getFreeSpace();
		boolean nonewfile = false;
		if(freespace<10000000) nonewfile = true;
		String folderparent = rawfile.getParent();
		String appfolder = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/VoiceTranslator";
		boolean samefolder = folderparent.equalsIgnoreCase(appfolder);
		if (rawfile.length() <= 1000000 && samefolder) {
			Log.d("Image file:", " without resize");
			return rawfile;
		}
		
		File filesave = rawfile;
		if (!samefolder) {
			if(!nonewfile){
				Log.d("Resize Img", "Create new image file");
				int sorandomocr = randInt(0, 99999);
				filesave = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath()
						+ "/VoiceTranslator"
						+ "/ocr_resize_" + sorandomocr + ".png");
				Log.d("Resize Img", "File: "+filesave.getAbsolutePath());
			}/*else{
				filesave = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath()
						+ "/VoiceTranslator"
						+ "/ocr_resize.png");
			}*/
		}
		
		Bitmap out = getimgbit();
		
		if(out!=null){
				Log.d("Width out :", "" + out.getWidth());
				Log.d("Height out :", "" + out.getHeight());

				// return savefileintoAPP(rawfile, 90);

				ExifInterface ei;
				try {
					ei = new ExifInterface(filepath);
					int orientation = ei.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_UNDEFINED);
					Log.d("orientation ", "" + orientation);
					switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						Log.d("rotate image", "rotate 90");
						out = rotateImage(out, 90);
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						Log.d("rotate image", "rotate 180");
						out = rotateImage(out, 180);
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						Log.d("rotate image", "rotate -90");
						out = rotateImage(out, -90);
						break;
					// etc.
					}
					
					FileOutputStream fo = new FileOutputStream(filesave);
					out.compress(Bitmap.CompressFormat.PNG, 90, fo);
					Log.d("File size :", "" + filesave.length());
					Log.d("resizeimg",
							"Save File Image: " + filesave.getAbsolutePath());
					//out.recycle();
					fo.flush();
					setimgbit(out);
					return filesave;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return filesave;
				}
				
			}else{
				return null;
			}
			
	}
	
	public Bitmap getimgbit(){
		return this.imgbitmap;
	}
	
	private void setimgbit(Bitmap bitmap){
		this.imgbitmap = bitmap;
	}
	public File getInfile(){
		return this.infile;
	}
	public static Bitmap rotateImage(Bitmap source, float angle) {
		Bitmap retVal;
	
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, true);

		return retVal;
	}
	/*// Get Bitmap from ImageLoader API
	public Bitmap setimgbit_imageloader(File infile){
		//Uri uriFile = Uri.fromFile(infile);
		ImageLoader imageLoader = ImageLoader.getInstance();
		final Bitmap[] finalTrave = {null};
		imageLoader.loadImage( Uri.fromFile(infile).toString(), new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// Do whatever you want with Bitmap
				finalTrave[0] = loadedImage;
			}
		});
		return finalTrave[0];
	}*/
	public static Bitmap decodeFile(File f,int WIDTH){
		 try {
		     //Decode image size
		     BitmapFactory.Options o = new BitmapFactory.Options();
		     o.inJustDecodeBounds = true;
		     BitmapFactory.decodeStream(new FileInputStream(f),null,o);

		     //The new size we want to scale to
		     final int REQUIRED_WIDTH=WIDTH;
		     int width = o.outWidth;
		     int height = o.outHeight;
		     int sosanh = width;
		     if(height > width) sosanh = height;
		     //Find the correct scale value. It should be the power of 2.
		     int scale=1;
		     
		     while(sosanh/scale/2>=REQUIRED_WIDTH)
		         scale*=2;

		     //Decode with inSampleSize
		     BitmapFactory.Options o2 = new BitmapFactory.Options();
		     o2.inSampleSize=scale;
		     Log.d("Resize Scale:", "" + scale);
		     return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		 } catch (FileNotFoundException e) {}
		 return null;
		}
	
	public static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
	public void showProgressDialog(Context context, String noidung) {
		if (this.progressDialog != null) {
			this.progressDialog.dismiss();
			this.progressDialog = null;
		}
		this.progressDialog = ProgressDialog.show(context, "", noidung);
	}

	public void hideProgressDialog() {
		if (this.progressDialog != null) {
			this.progressDialog.dismiss();
			this.progressDialog = null;
		}
	}
}
