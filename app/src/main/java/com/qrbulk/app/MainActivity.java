package com.qrbulk.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button selFileBtn, genBtn, useFileBtn;
    ConstraintLayout cl;
    TextView qrTxt, filePathTV;
    String QrPlainTxt;
    //
    private static Workbook wb;
    private static Sheet sh;
    private static Row row;
    private static Cell cell;

    int w;

    Uri studListUri;
    String filePath;

    private static final int CODE = 1001;

    ArrayList<String> qrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);// no dark mode
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PackageManager.PERMISSION_GRANTED);

        filePath = "";

        imageView = findViewById(R.id.imageView);
        selFileBtn = findViewById(R.id.selFileBtn);
        genBtn = findViewById(R.id.genFileBtn);
        useFileBtn = findViewById(R.id.useFileBtn);

        cl = findViewById(R.id.qrLayout);
        qrTxt = findViewById(R.id.qrPlainTxt);
        filePathTV = findViewById(R.id.filePath);

        selFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                startActivityForResult(intent, CODE);

                //open file manager
//                if(editText.getText().toString().equals("")){
//                    Toast.makeText(MainActivity.this, "Please enter text first", Toast.LENGTH_SHORT).show();
//
//                }else {
//
//                    qrTxt.setText(editText.getText().toString());
//                    genQR(editText.getText().toString());
//                }

            }
        });
        useFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath.equals("")){
                    Toast.makeText(MainActivity.this, "Pls select an xlsx file first", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        saveToList(MainActivity.this, studListUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "FNF Exception", Toast.LENGTH_SHORT).show();
                        Log.d("asduri:", e.toString());
                    }
                }
            }
        });
        genBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath.equals("")){
                    Toast.makeText(MainActivity.this, "Pls select an xlsx file first", Toast.LENGTH_SHORT).show();
                }else{
                    //
                    //Toast.makeText(SelectFile.this, "selected", Toast.LENGTH_SHORT).show();
                    iterateList();
                }
//                if(qrTxt.getText().toString().equals("")){
//                    Toast.makeText(MainActivity.this, "Please enter text first", Toast.LENGTH_SHORT).show();
//
//                }else {
//                    saveToGallery();
//                }
            }
        });

    }
    private void genQR(String text){
        qrTxt.setText(QrPlainTxt);
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            imageView.setImageBitmap(bitmap);
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //manager.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);

        }catch (Exception e){
            e.printStackTrace();
        }
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
        //String dateNow = new SimpleDateFormat("-yyyyMMMdd-hhmmssa", Locale.getDefault()).format(new Date());

        File filePath = new File(getApplicationContext().getExternalFilesDir(null) + "/qrbulk" + "-QR-" + qrTxt.getText().toString() + ".png");
        try {
            if(filePath.exists()) filePath.createNewFile();
            else filePath = new File(getApplicationContext().getExternalFilesDir(null) + "/qrbulk" + "-QR-" + qrTxt.getText().toString() + ".png");
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);//
            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            //Toast.makeText(getApplicationContext(), "File was created:" + filePath.toString(), Toast.LENGTH_SHORT).show();
            Log.d("asdqr:", filePath.toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "File failed to create", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.d("asd:", e.toString());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //
        studListUri = data.getData();

        File tempFile = new File(studListUri.getPath());
        final String[] split = tempFile.getPath().split(":");//split the path.
        filePath = split[1];
        //filePath = tempFile.getAbsolutePath();

        //filePath = data.getDataString();
        filePathTV.setText(filePath);



        super.onActivityResult(requestCode, resultCode, data);
    }
    private void saveToList(Context context, Uri uri) throws FileNotFoundException {
        InputStream inStream;
        inStream = context.getContentResolver().openInputStream(uri);
        try {
            wb = new XSSFWorkbook(inStream);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "IOException", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        sh = wb.getSheetAt(0);
        int rowNum = sh.getLastRowNum();
        qrList = new ArrayList<String>();
        for (int q = 1 ; q <= rowNum ; q++) {
            qrList.add("" + (int) Double.parseDouble(sh.getRow(q).getCell(0).toString()));
        }
        Toast.makeText(MainActivity.this, "File selected with " + rowNum + " rows of data", Toast.LENGTH_SHORT).show();
        QrPlainTxt = qrList.get(0);
        genQR(qrList.get(0));
        saveToGallery();
    }
    private void iterateList(){

        for (int q = 1 ; q < qrList.size() ; q++) {
            QrPlainTxt = qrList.get(q);
            w=q;
            qrTxt.setText(QrPlainTxt);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    QrPlainTxt = qrList.get(w);
                    qrTxt.setText(QrPlainTxt);
                }
            }, 1000);
            genQR(QrPlainTxt);

            saveToGallery();
        }

        Toast.makeText(MainActivity.this, "Successfully generated " + qrList.size() + " QR code images", Toast.LENGTH_SHORT).show();


    }
}