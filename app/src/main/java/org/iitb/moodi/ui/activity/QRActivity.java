package org.iitb.moodi.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
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
    private Camera.PictureCallback mPicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

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

        parameters.setPictureSize(mSize.width,mSize.height);
        pixels=new int[mSize.width*mSize.height];

        mCamera.setParameters(parameters);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        reader = new QRCodeReader();

        mPicture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, final Camera camera) {
                Bitmap img = BitmapFactory.decodeByteArray(data,0,data.length);
                /*int[] pixelData = new int[img.getWidth()*img.getHeight()];
                img.getPixels(pixelData,0,img.getWidth(),0,0,img.getWidth(),img.getHeight());*/

                data = null;

                img.getPixels(pixels,0,mSize.width,0,0,mSize.width,mSize.height);

                LuminanceSource luminImg = new RGBLuminanceSource( img.getWidth(), img.getHeight(),pixels);
                BinaryBitmap binImg = new BinaryBitmap(new HybridBinarizer(luminImg));

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
        };

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

    public void scan(View v){
        mCamera.takePicture(null,null,mPicture);
    }

    @Override
    public void onDestroy(){
        mCamera.release();
        super.onDestroy();
    }
}
