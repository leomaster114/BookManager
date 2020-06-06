package com.example.bookmanager.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookmanager.Database.MyDatabase;
import com.example.bookmanager.Model.Book;
import com.example.bookmanager.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddBook extends AppCompatActivity {
    EditText edt_type,edt_name,edt_author,edt_des;
    ImageButton btn_choose;
    Button btn_addbook,btn_back;
    ImageView imageBook;
    final int REQUEST_CODE_GALLERY = 111;
    MyDatabase database = new MyDatabase(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        //
        btn_addbook =findViewById(R.id.btn_add_book);
        edt_type = findViewById(R.id.edt_type_book);
        edt_name = findViewById(R.id.edt_name_book);
        edt_author = findViewById(R.id.edt_author_book);
        edt_des = findViewById(R.id.edt_des_book);
        btn_choose = findViewById(R.id.btnFolder);
        imageBook = findViewById(R.id.imgv_book);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBook.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AddBook.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
            }
        });
        btn_addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Book book = new Book();
                    book.setBookName(edt_name.getText().toString());
                    book.setAuthor(edt_author.getText().toString());
                    book.setType(edt_type.getText().toString());
                    book.setDescription(edt_des.getText().toString());
                    book.setImageUri(imageViewToByte(imageBook));
                    Log.d("Addbook", "onClick: add book successfull");
                    database.addBook(book);

                    Toast.makeText(AddBook.this,"Add book successfully",Toast.LENGTH_SHORT).show();
                    edt_author.setText("");
                    edt_name.setText("");
                    edt_des.setText("");
                    edt_type.setText("");
//                    imageBook.setImageResource(R.drawable.folder_icon);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_GALLERY);
            }else{
                Toast.makeText(this,"not permission",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE_GALLERY&&resultCode == RESULT_OK&& data!=null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageBook.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
