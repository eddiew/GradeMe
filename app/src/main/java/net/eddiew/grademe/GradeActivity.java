package net.eddiew.grademe;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by eddiew on 8/30/14.
 */
public class GradeActivity extends Activity {
    private Bitmap processedImage;
    private ImageView testView;
    private TextView nameView;
    private TextView gradeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        processedImage = PreprocessActivity.Result;
        testView = (ImageView)findViewById(R.id.test_view);
        nameView = (TextView)findViewById(R.id.test_name);
        gradeView = (TextView)findViewById(R.id.test_grade);
//        testView.setImageBitmap(processedImage);
//        TessBaseAPI tess = new TessBaseAPI();
//        // Init variables
//        tess.setVariable("load_system_dawg", "F");
//        tess.setVariable("load_freq_dawg", "F");
//        //tess.setVariable("load_number_dawg", "F");
//        // tess.setVariable("textord_xheight_error_margin", "1.0");
//        tess.init(MainActivity.DATA_PATH, "eng");
//        // Post-init variables
//        tess.setVariable("tessedit_ocr_engine_mode", "2");
//        tess.setVariable("chop_vertical_creep", "T");
//        tess.setVariable("tessedit_minimal_rejection", "T");
//        tess.setVariable("tessedit_char_whitelist", "1234567890+=");
//        tess.setImage(processedImage);

//        Bitmap testBitmap;
//        try {
//            InputStream bis = getAssets().open("test-images/test3.png");
//            //Drawable d = Drawable.createFromStream(bis, null);
//            //Canvas canvas = new Canvas(testBitmap);
//            testBitmap = BitmapFactory.decodeStream(bis);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            Log.e("OCR Test", "IOException");
//            return;
//        }
//        tess.setImage(testBitmap);

//        tess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT);
//        String test = tess.getUTF8Text();
//        Log.e("Tesseract output", test);
//        Toast.makeText(this, "Recognized text: " + test, Toast.LENGTH_SHORT).show();
//
//        String text = tess.getHOCRText(0);


        // Grade hocr text
        Paint wrongPaint = new Paint();
        wrongPaint.setColor(Color.RED);
        wrongPaint.setStrokeWidth(2f);
        wrongPaint.setStyle(Paint.Style.STROKE);
        RectF wrong1 = new RectF(20, 120, 110, 150);
        RectF wrong2 = new RectF(110, 80, 200, 110);
        Bitmap wrongBitmap = Bitmap.createBitmap(processedImage.getWidth(), processedImage.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas wrongCanvas = new Canvas(wrongBitmap);
        wrongCanvas.drawBitmap(processedImage, 0, 0, null);
        wrongCanvas.drawRect(wrong1, wrongPaint);
        wrongCanvas.drawRect(wrong2, wrongPaint);
        testView.setImageDrawable(new BitmapDrawable(getResources(), wrongBitmap));

        nameView.setText("Eddie");
        gradeView.setText("8/10");
    }
}
