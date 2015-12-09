package org.iitb.moodi.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.iitb.moodi.R;
import org.iitb.moodi.ui.widget.CameraPreview;

import java.util.List;


public class QRActivity extends BaseActivity {


    QRCodeReader reader;
    int[] pixels ;

    private Camera mCamera;
    private Camera.Size mSize;
    private CameraPreview mPreview;
    private String TAG="QRActivity";
    private int pass=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        mCamera = getCameraInstance();

        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();

        for(Camera.Size size :sizes){
            mSize=size;

            Log.d("CameraSize" , size.width+"x"+size.height);

            if(size.width<800)
                break;
        }

        if(mSize==null){
            mSize=sizes.get(sizes.size()-1);
        }

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.setPreviewSize(mSize.width,mSize.height);
        parameters.setPreviewFormat(ImageFormat.YV12);
        pixels=new int[mSize.width*mSize.height];

        mCamera.setParameters(parameters);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera, new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, final Camera camera) {
                if(pass<50) {
                    pass++;
                    return;
                } else {
                    pass = 0;
                }

                try{
                    BinaryBitmap binImg = new BinaryBitmap(new HybridBinarizer(
                            new PlanarYUVLuminanceSource(
                                    data,
                                    mSize.width,mSize.height,
                                    0,0,
                                    mSize.width,mSize.height,
                                    false
                                    )));

                    Result decoded = reader.decode(binImg);
                    camera.stopPreview();
                    new AlertDialog.Builder(QRActivity.this)
                            .setMessage(decoded.getText())
                            .setTitle("Message")
                            .setNeutralButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton){
                                            camera.startPreview();
                                        }
                                    })
                            .show();

                } catch (NotFoundException e) {
                    Log.e(TAG, "QR Code not found!");
                } catch (ChecksumException e) {
                    Log.e(TAG, "QR Code invalid!");
                } catch (FormatException e) {
                    Log.e(TAG, "Wrong format!");
                } catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
        });
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        reader = new QRCodeReader();

        /*mPicture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, final Camera camera) {
                BinaryBitmap binImg = new BinaryBitmap(new HybridBinarizer(
                        new PlanarYUVLuminanceSource(
                                data,
                                mSize.width,mSize.height,
                                0,0,
                                mSize.width,mSize.height,
                                false
                        )));

                try {
                    Result decoded = reader.decode(binImg);

                    new AlertDialog.Builder(QRActivity.this)
                            .setMessage(decoded.getText())
                            .setTitle("Decoded Message")
                            .setNeutralButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton){
                                            camera.startPreview();
                                        }
                                    })
                            .show();

                } catch (NotFoundException e) {
                    Toast.makeText(QRActivity.this, "QR Code not found!",Toast.LENGTH_LONG).show();
                    camera.startPreview();
                } catch (ChecksumException e) {
                    Toast.makeText(QRActivity.this, "QR Code invalid! Please scan properly.",Toast.LENGTH_LONG).show();
                    camera.startPreview();
                } catch (FormatException e) {
                    Toast.makeText(QRActivity.this, "Wrong format",Toast.LENGTH_LONG).show();
                    camera.startPreview();
                }
            }
        };*/

        mCamera.setDisplayOrientation(90);
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
           e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    /*public void scan(View v){
        mCamera.takePicture(null,null,mPicture);
    }*/

    @Override
    public void onDestroy(){
        mCamera.stopPreview();
        mCamera.setPreviewCallback(null);
        mCamera.release();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mToolbar.setTitle("QR Scanner");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
