package net.eddiew.grademe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraPreviewActivity extends Activity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;

    public static Bitmap Capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        mPicture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                Capture = BitmapFactory.decodeByteArray(data, 0, data.length);
                // Switch to preprocess activity
                Intent intent = new Intent(CameraPreviewActivity.this, PreprocessActivity.class);
                startActivity(intent);

//                MainActivity.Tess.setImage(image);
//                String text = MainActivity.Tess.getUTF8Text();
//                String hocrText = MainActivity.Tess.getHOCRText(0);
//                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
//                if (pictureFile == null){
//                    Log.d("yolo", "Error creating media file, check storage permissions: ");
//                    return;
//                }

//                try {
//                    FileOutputStream fos = new FileOutputStream(pictureFile);
//                    fos.write(data);
//                    fos.close();
//
//                    Intent i = new Intent(Intent.ACTION_SEND);
//                    i.setType("message/rfc822");
//                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ewang1997@gmail.com"});
//                    i.putExtra(Intent.EXTRA_SUBJECT, "herp");
//                    i.putExtra(Intent.EXTRA_TEXT   , "derp");
//                    Uri uri = Uri.fromFile(pictureFile);
//                    i.putExtra(Intent.EXTRA_STREAM, uri);
//                    try {
//                        startActivity(Intent.createChooser(i, "Send mail..."));
//                    } catch (android.content.ActivityNotFoundException ex) {
//                    }
//
//                } catch (FileNotFoundException e) {
//                    Log.d("yolo", "File not found: " + e.getMessage());
//                } catch (IOException e) {
//                    Log.d("yolo", "Error accessing file: " + e.getMessage());
//                }
            }
        };

        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );
    }

    public static Camera getCameraInstance() throws Exception {
        Camera c = Camera.open(); // attempt to get a Camera instance
        if (c == null) throw new Exception();
        Camera.Parameters params = c.getParameters();
        try {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            c.setParameters(params);
        }
        catch (Exception e) {}
        try {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            c.setParameters(params);
        }
        catch (Exception e) {}
        try {
            params.setColorEffect(Camera.Parameters.EFFECT_MONO);
            c.setParameters(params);
        }
        catch (Exception e) {}
        return c; // returns null if camera is unavailable
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Creates a Camera instance
        try {
            mCamera = getCameraInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        mCamera.setDisplayOrientation(90);
        mPreview.setCamera(mCamera);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mCamera.reconnect();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        mCamera.startPreview();
    }

    @Override
    protected void onPause() {
        mCamera.stopPreview();
        mCamera.unlock();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mCamera.release();
        super.onStop();
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera_preview, menu);
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
