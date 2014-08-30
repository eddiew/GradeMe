package net.eddiew.grademe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by eddiew on 8/30/14.
 */
public class PreprocessActivity extends Activity {
    private Bitmap raw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        raw = CameraPreviewActivity.Capture;
        setContentView(R.layout.view_preprocess);
        ImageView preview = (ImageView) findViewById(R.id.preview);
        preview.setImageBitmap(raw);
    }
}
