package com.info.rehberim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText editTextName,editTextPhone,editTextEmail;
    Bitmap selectedImage;
    Button buttonSave,buttonExit;
    Database database;
    List<Person>people;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.imageView);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSave = findViewById(R.id.buttonSave);
        buttonExit = findViewById(R.id.buttonExit);

        database = new Database(this);

        Bitmap selectImage = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.round_add_icon);
        imageView.setImageBitmap(selectImage);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    save(view);
            }
        });


        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailActivity.this,MainActivity.class));
                finish();
            }
        });


    }
    public void selectImage(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            Uri imageData = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedImage);
                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }
    public void save(View view){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (selectedImage != null) {
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }else{
             selectedImage = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.round_add_icon);
        }
        final byte[] byteArray = stream.toByteArray();
        new DbHelper().addPerson(database,editTextName.getText().toString(),editTextPhone.getText().toString(),editTextEmail.getText().toString(),
                byteArray);
        startActivity(new Intent(DetailActivity.this,MainActivity.class));
        finish();

    }
    public Bitmap makeSmallerImage(Bitmap image,int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height ;

        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int)(width/bitmapRatio);
        } else {
            height  = maxSize;
            width = (int)(height / bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image,width,height,true);

}
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}