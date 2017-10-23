package com.nadoo.tacademy.eunbyul_nanu;



import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;

public class CameraActivity extends AppCompatActivity implements
        SurfaceHolder.Callback, Camera.ShutterCallback,Camera.PictureCallback,View.OnClickListener{
    private SurfaceView mCameraView;
    private Camera mCamera;
    private ImageButton mStart;
    private TextView saveImage;

    private ImageView imagePreView;
    private SurfaceHolder surfaceHolder;
    private final static int PERMISSIONS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        mCameraView = (SurfaceView)findViewById(R.id.camera_view);
        imagePreView = (ImageView)findViewById(R.id.imagepreview);

        surfaceHolder = mCameraView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //사진찍기
        mStart = (ImageButton)findViewById(R.id.btn);
        mStart.setOnClickListener(this);
        //사진 등록
        saveImage = (TextView)findViewById(R.id.save_img);
        saveImage.setOnClickListener(this);


    }
    @Override
    public void onStart(){
        super.onStart();
        int APIVersion = Build.VERSION.SDK_INT;
        if(APIVersion >= Build.VERSION_CODES.M){
            if(checkCAMERAPermission()){
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PERMISSIONS_REQUEST_CODE);
            }
        }
    }
    public void onClick(View v) {
        switch( v.getId() ) {
            case R.id.btn:
                mCamera.takePicture(this, null, this);
                break;
            case R.id.save_img:
                Intent intent = new Intent(CameraActivity.this, RegisterItem.class);
                intent.putExtra("imageDir",filePath);
                Log.e("imageDir",filePath);
               setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if (!isSDCardAvailable()) {
            Toast.makeText(this, "SD 카드가 없어 종료 합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }
    public boolean isSDCardAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
    private boolean checkCAMERAPermission(){
        int result = ContextCompat.checkSelfPermission(MyApplication.getmContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        switch (requestCode){
            case PERMISSIONS_REQUEST_CODE :
                if(grantResults.length>0){
                    boolean camearaAccepted = (grantResults[0]==PackageManager.PERMISSION_GRANTED);
                    if(camearaAccepted){
                        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    }
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(CAMERA)) {

                            new AlertDialog.Builder(this)
                                    .setTitle("Camera Permission Needed")
                                    .setMessage("This app needs the Camera permission, please accept to use camera functionality")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ActivityCompat.requestPermissions(CameraActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                                                    Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CODE);
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                    Toast.makeText(this, "Request storage permission denied", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(width, height);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
    public void onShutter(){}

    String filePath;
    String rotate_filePath;
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i("TAG", "셔터 누름");
        if (data != null) {
            Log.i("TAG", "JPEG 사진 찍었음");

            String imgFileName = "upload_" + String.valueOf(System.currentTimeMillis() / 1000) + ".jpg";
            File file =  CameraActivity.this.getCacheDir();
            File cacheFile = new File(file, imgFileName);

            BitmapFactory.Options options = new BitmapFactory.Options();

            try {

                FileOutputStream fos = new FileOutputStream(cacheFile);
                fos.write(data);
                fos.flush();
                fos.close();

                filePath = cacheFile.getAbsoluteFile().toString();

            } catch (IOException e) {
                Log.e("tag", "File Write Error", e);
                return;
            }

            Toast.makeText(getApplicationContext(),"카메라로 찍은 사진을 앨범에 저장했습니다.",Toast.LENGTH_LONG).show();

            options.inSampleSize = 8;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
            Bitmap bitmap = rotate(bmp);
            imagePreView.setImageBitmap(bitmap);
            imagePreView.setImageBitmap(bitmap);
            mCamera.startPreview();
        }
    }

    public Bitmap rotate(Bitmap bitmap){
        if(bitmap!=null){
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            try{
                Bitmap converted = Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(),bitmap.getHeight(),matrix, false);
                if(bitmap!=converted){
                    bitmap = converted;
                }
            }catch (OutOfMemoryError error){
            }
        }
        return bitmap;
    }
}
