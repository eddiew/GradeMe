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


public class MainActivity extends Activity implements NewTestDFragment.NewTestDListener, SelectTestDFragment.SelectTestDListener {
    ArrayAdapter<String> adapter;
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/GradeMe/";

    ArrayList<String> menuItems = new ArrayList<String>();

    private AssetManager assMan;
//    public static TessBaseAPI Tess;

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
                SelectTestDFragment st = new SelectTestDFragment();
                st.setSelect(position);
                st.show(getFragmentManager(), "");
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);

        // Tesseract Initialization
        try {
            tessSetup(true);
        }
        catch (IOException e) {
            Log.e("Tesseract Setup", "FAILED: IO Exception");
            e.printStackTrace();
            return;
        }

//        // OCR test TODO: remove
//        Bitmap testBitmap;
//        try {
//            InputStream bis = assMan.open("test-images/TEST_1.JPG");
//            //Drawable d = Drawable.createFromStream(bis, null);
//            //Canvas canvas = new Canvas(testBitmap);
//            testBitmap = BitmapFactory.decodeStream(bis);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            Log.e("OCR Test", "IOException");
//            return;
//        }
//        Tess.setImage(testBitmap);
//        String hocrText = Tess.getHOCRText(0);
//        String boxText = Tess.getBoxText(0);
//        Tess.end();
    }

    private void tessSetup() throws IOException{
        tessSetup(false);
    }

    private void tessSetup(boolean force) throws IOException{
        new File(DATA_PATH + "tessdata/configs/").mkdirs();
        for (String fileName : assMan.list("tessdata")) {
            InputStream in = assMan.open("tessdata/" + fileName);
            File file = new File(DATA_PATH + "tessdata/" + fileName);
            if (!force && file.exists()) continue;
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            Log.v("Tesseract Setup", "Copied: " + fileName);
        }
        Log.d("Tesseract Setup", "All tessdata files copied.");
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
                new NewTestDFragment().show(getFragmentManager(), "");
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

    @Override
    public void onSDialogPositiveClick(DialogFragment dialog) {
        int test = ((SelectTestDFragment) dialog).select;

        Intent intent = new Intent(this, CameraPreviewActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSDialogNegativeClick(DialogFragment dialog) {
        int test = ((SelectTestDFragment) dialog).select;
    }
}
