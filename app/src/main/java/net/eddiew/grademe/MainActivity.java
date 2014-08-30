package net.eddiew.grademe;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends Activity {
    ArrayAdapter<String> adapter;
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/GradeMe/";
    String[] menuItems = {"Camera Control Demo", "Blank1", "Blank2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sets items in ListView
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menuItems);
        ListView listView = (ListView) findViewById(R.id.testListView);
        listView.setAdapter(adapter);

        // Click listener
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                switchActivity(position);
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
        try {
            tessSetup();
        }
        catch (IOException e) {
            Log.e("Tesseract Setup", "FAILED: IO Exception");
            e.printStackTrace();
            return;
        }
        TessBaseAPI tess = new TessBaseAPI();
        tess.init(DATA_PATH, "eng");
    }

    private void tessSetup() throws IOException{
        new File(DATA_PATH + "tessdata/").mkdirs();
        AssetManager assMan = getAssets();
        for (String fileName : assMan.list("tessdata")) {
            InputStream in = assMan.open("tessdata/" + fileName);
            File file = new File(DATA_PATH + "tessdata/" + fileName);
            if (file.exists()) continue;
            OutputStream out = new FileOutputStream(DATA_PATH + "tessdata/" + fileName);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            Log.v("Tesseract Setup", "Copied: " + fileName);
        }
        Log.v("Tesseract Setup", "All tessdata files copied.");
    }

    private void switchActivity(int id)
    {
        switch (id) {
            case 0:
                Intent intent = new Intent(this, CameraPreviewActivity.class);
                startActivity(intent);
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
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
