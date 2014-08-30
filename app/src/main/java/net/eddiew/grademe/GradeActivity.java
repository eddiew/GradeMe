package net.eddiew.grademe;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.googlecode.tesseract.android.TessBaseAPI;

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
        tess.init(MainActivity.DATA_PATH, "eng");
        tess.setImage(processedImage);
        String test = tess.getUTF8Text();
        String text = tess.getHOCRText(0);
    }
}
