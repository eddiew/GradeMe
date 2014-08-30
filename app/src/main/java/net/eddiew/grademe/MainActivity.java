package net.eddiew.grademe;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.googlecode.tesseract.android.TessBaseAPI;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        String[] assetNames;
        try {
            assetNames = assetManager.list("");
        }
        catch (Exception e) {
            assetNames = new String[]{"NONE FOUND"};
        }
        for (String asset : assetNames) {
            Log.e("assets", asset);
        }
        int stuff;
//        TessBaseAPI tess = new TessBaseAPI();
//        tess.init("assets/tessdata/eng.traineddata", "eng");
//        tess.setImage("assets/test2.png");
        boolean more_stuff;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
