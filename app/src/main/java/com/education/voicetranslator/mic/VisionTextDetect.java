package com.education.voicetranslator.mic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import java.io.File;


public class VisionTextDetect {
    private TextRecognizer detector;
    private boolean errorOCRText = false;

    public VisionTextDetect(Context context) {
        this.detector = new TextRecognizer.Builder(context).build();

    }

    public boolean isErrorOCRText() {
        return errorOCRText;
    }

    public void setErrorOCRText(boolean errorOCRText) {
        this.errorOCRText = errorOCRText;
    }

    public String getTextFromFile(File imagefile){

        Bitmap mbitmap = BitmapFactory.decodeFile(imagefile.getPath());
        String trave = null;
        try {
            if (detector.isOperational() && mbitmap != null) {
                Frame frame = new Frame.Builder().setBitmap(mbitmap).build();
                SparseArray<TextBlock> textBlocks = detector.detect(frame);
                String blocks = "";
                String lines = "";
                String words = "";
                for (int index = 0; index < textBlocks.size(); index++) {
                    //extract scanned text blocks here
                    TextBlock tBlock = textBlocks.valueAt(index);
                    blocks = blocks + tBlock.getValue() + "\n" + "\n";
                    for (Text line : tBlock.getComponents()) {
                        //extract scanned text lines here
                        lines = lines + line.getValue() + "\n";
                        for (Text element : line.getComponents()) {
                            //extract scanned text words here
                            words = words + element.getValue() + ", ";
                        }
                    }
                }
                if (textBlocks.size() > 0) {
                    trave = ""+lines;
                }
                errorOCRText = false;
            }
        } catch (Exception e) {
            errorOCRText = true;
            Log.e("TEXT API", e.toString());
        }
        detector.release();
        return trave;
    }
}
