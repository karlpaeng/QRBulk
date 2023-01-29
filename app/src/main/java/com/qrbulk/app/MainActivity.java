package com.qrbulk.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    ImageView imageView;
    Button genBtn, saveBtn;
    ConstraintLayout cl;
    TextView qrTxt;
asdasd
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);// no dark mode
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PackageManager.PERMISSION_GRANTED);

        editText = findViewById(R.id.textEt);
        imageView = findViewById(R.id.imageView);
        genBtn = findViewById(R.id.genBtn);
        saveBtn = findViewById(R.id.saveBtn);
        cl = findViewById(R.id.qrLayout);
        qrTxt = findViewById(R.id.qrPlainTxt);

        genBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString().trim();
                qrTxt.setText(text);
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 350, 350);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    imageView.setImageBitmap(bitmap);
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToGallery();
            }
        });

    }
    private void saveToGallery(){
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
//        Bitmap bitmap = bitmapDrawable.getBitmap();
        cl.setDrawingCacheEnabled(true);
        cl.buildDrawingCache();
        Bitmap bitmap = cl.getDrawingCache();

//        FileOutputStream outputStream = null;
//        File file = Environment.getExternalStorageDirectory();
//        File dir = new File(file.getAbsolutePath() + "/MyPics");
//        dir.mkdirs();
//
//        String filename = String.format("%d.png",System.currentTimeMillis());
//        File outFile = new File(dir,filename);
//        try{
//            outputStream = new FileOutputStream(outFile);
//        }catch (Exception e){
//            e.printStackTrace();
//            Log.d("asd1:", e.toString());
//        }
//        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);//
//        try{
//            outputStream.flush();
//        }catch (Exception e){
//            e.printStackTrace();
//            Log.d("asd2:", e.toString());
//        }
//        try{
//            outputStream.close();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            Log.d("asd3:", e.toString());
//        }
        String dateNow = new SimpleDateFormat("yyyyMMMdd-hhmmssa", Locale.getDefault()).format(new Date());

        File filePath = new File(getApplicationContext().getExternalFilesDir(null) + "/amsbcc-" + dateNow + "-QR-" + editText.getText().toString() + ".png");
        try {
            if(filePath.exists()) filePath.createNewFile();
            else filePath = new File(getApplicationContext().getExternalFilesDir(null) + "/amsbcc-" + dateNow + "-QR-" + editText.getText().toString() + ".png");
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);//
            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            Toast.makeText(getApplicationContext(), "File was created:" + filePath.toString(), Toast.LENGTH_SHORT).show();
            Log.d("asdqr:", filePath.toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "File failed to create", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.d("asd:", e.toString());
        }
    }
}