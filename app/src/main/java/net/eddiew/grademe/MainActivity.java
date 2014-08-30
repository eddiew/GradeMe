package net.eddiew.grademe;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class MainActivity extends Activity implements NewTestDFragment.NewTestDListener{
    ArrayAdapter<String> adapter;
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/GradeMe/";

    ArrayList<String> menuItems = new ArrayList<String>();

    private AssetManager assMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuItems.add("Camera Control Demo");
        assMan = getAssets();

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

        // Tesseract Initialization
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

        // OCR test TODO: remove
        Bitmap testBitmap;
        try {
            InputStream bis = assMan.open("test-images/test2.png");
            //Drawable d = Drawable.createFromStream(bis, null);
            //Canvas canvas = new Canvas(testBitmap);
            testBitmap = BitmapFactory.decodeStream(bis);
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("OCR Test", "IOException");
            return;
        }
        tess.setImage(testBitmap);
        String text = tess.getUTF8Text();
        String[] lines = text.split("\n");
        for (String line : lines) {
            Log.i("OCR Output", line);
        }
        tess.end();
    }

    private void tessSetup() throws IOException{
        new File(DATA_PATH + "tessdata/").mkdirs();
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

    private void createNewTest()
    {
        new NewTestDFragment().show(getFragmentManager(), "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        switch (item.getItemId()) {
            case R.id.action_new:
                createNewTest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override // Info from NewTestDialog received here. Actually make new item here
    public void onDialogPositiveClick(DialogFragment dialog) {
        String title = ((NewTestDFragment) dialog).name;
        adapter.add(title);
    }
}
