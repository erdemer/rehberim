package com.info.rehberim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class EditActivity extends AppCompatActivity {
    private ImageView editImage;
    private EditText editName,editPhone,editEmail;
    private Button buttonCancel_E,buttonEdit;
    private Database database;
    private Bitmap selectEditImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editImage = findViewById(R.id.editImage);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        buttonCancel_E = findViewById(R.id.buttonCancel_E);
        buttonEdit = findViewById(R.id.buttonEdit);

        database = new Database(this);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("edit_name");
        final String phone = intent.getStringExtra("edit_telNumber");
        final String email = intent.getStringExtra("edit_email");
        final byte[] imageByte = intent.getByteArrayExtra("edit_image");
        final int person_id = intent.getIntExtra("person_id",0);

        final Bitmap bitmapImage = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

        editName.setText(name);
        editPhone.setText(phone);
        editEmail.setText(email);
        editImage.setImageBitmap(bitmapImage);

        buttonCancel_E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditActivity.this,MainActivity.class));
            }
        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                if (selectEditImage == null) {
                    selectEditImage = bitmapImage;
                }
                selectEditImage.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
                byte[] byteArrayEdit =  outputStream.toByteArray();
                new DbHelper().updatePerson(database,person_id,editName.getText().toString(),
                        editPhone.getText().toString(),editEmail.getText().toString(),byteArrayEdit);
                startActivity(new Intent(EditActivity.this,MainActivity.class));
            }
        });


    }
    public void selectEditImage(View view){
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
                    selectEditImage = ImageDecoder.decodeBitmap(source);
                    editImage.setImageBitmap(selectEditImage);
                }else{
                    selectEditImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}