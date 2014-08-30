package net.eddiew.grademe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import net.eddiew.grademe.bitmapFilters.BitmapFilters;

import java.util.HashMap;

/**
 * Created by eddiew on 8/30/14.
 */
public class PreprocessActivity extends Activity implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener{
    private Bitmap raw;
    private HashMap<String, Integer> settings = new HashMap<String, Integer>();
    private Spinner settingSpinner;
    private SeekBar settingSeekBar;
    private TextView settingValue;
    private Bitmap[] processed = new Bitmap[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        raw = CameraPreviewActivity.Capture;
        setContentView(R.layout.view_preprocess);
        ImageView preview = (ImageView) findViewById(R.id.preview);
        for (String setting : getResources().getStringArray(R.array.preprocess_configurables)) {
            settings.put(setting, 0);
        }
        settingSpinner = (Spinner)findViewById(R.id.setting_spinner);
        settingSpinner.setOnItemSelectedListener(this);
        settingSeekBar = (SeekBar)findViewById(R.id.setting_seekbar);
        settingSeekBar.setOnSeekBarChangeListener(this);
        settingValue = (TextView)findViewById(R.id.setting_value);
        processed[0] = raw;
        processed[1] = raw;
        processed[2] = raw;
        preview.setImageBitmap(processed[2]);
    }

    private String getCurrentSetting() {
        return (String) settingSpinner.getSelectedItem();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String setting = (String) parent.getItemAtPosition(pos);
        int val = settings.get(setting);
        settingSeekBar.setProgress(val);
        settingValue.setText("" + val);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
        if (!isFromUser) return;
        String setting = getCurrentSetting();
        int val = progress - 100;
        settings.put(setting, val);
        settingValue.setText("" + val);
        switch (getCurrentSetting()) {
            case "Sharpness":
                processed[0] = BitmapFilters.sharpen(raw, val);
                processed[1] = BitmapFilters.brighten(processed[0], settings.get("Brightness"));
                processed[2] = BitmapFilters.contrast(processed[1], settings.get("Contrast"));
                break;
            case "Brightness":
                processed[1] = BitmapFilters.brighten(processed[0], val);
                processed[2] = BitmapFilters.contrast(processed[1], settings.get("Contrast"));
                break;
            case "Contrast":
                processed[2] = BitmapFilters.contrast(processed[1], val);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Do nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Do nothing
    }
}
