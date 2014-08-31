package net.eddiew.grademe;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        processedImage = PreprocessActivity.Result;
        testView = (ImageView)findViewById(R.id.test_view);
        testView.setImageBitmap(processedImage);
        TessBaseAPI tess = new TessBaseAPI();
        // Init variables
        tess.setVariable("load_system_dawg", "F");
        tess.setVariable("load_freq_dawg", "F");
        //tess.setVariable("load_number_dawg", "F");
        // tess.setVariable("textord_xheight_error_margin", "1.0");
        tess.init(MainActivity.DATA_PATH, "eng");
        // Post-init variables
        tess.setVariable("tessedit_ocr_engine_mode", "2");
        tess.setVariable("chop_vertical_creep", "T");
        tess.setVariable("tessedit_minimal_rejection", "T");
        tess.setVariable("tessedit_char_whitelist", "1234567890+=");
        tess.setImage(processedImage);

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

        tess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT);
        String test = tess.getUTF8Text();
        Log.e("Tesseract output", test);
        Toast.makeText(this, "Recognized text: " + test, Toast.LENGTH_SHORT).show();

        String text = tess.getHOCRText(0);


        // Grade hocr text

    }
}
