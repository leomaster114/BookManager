package com.example.bookmanager.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.bookmanager.Adapter.Adapter;
import com.example.bookmanager.Database.MyDatabase;
import com.example.bookmanager.Model.Book;
import com.example.bookmanager.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    FloatingActionButton floatingActionButton;
    ArrayList<Book> books,BookInType;
    ListView listType;
    Set<String> BookType;
    final int REQUEST_CODE_GALLERY = 222;
    GridView gridView;
    MyDatabase database;
    Adapter myAdapter;
    ArrayAdapter<String> arrayAdapter;
    TextView tvSignOut, tvType, tvUserName, tvPassword;
    EditText edtSearch;
    ImageView btnSearch;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout = findViewById(R.id.activity_main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_draw_open, R.string.nav_draw_close);
        drawerLayout.addDrawerListener(drawerToggle);
        gridView = findViewById(R.id.gridview);
        floatingActionButton = findViewById(R.id.fab);
        tvSignOut = findViewById(R.id.tv_signOut);
        tvType = findViewById(R.id.tv_typeBook);
        tvUserName = findViewById(R.id.activity_main_tv_user_name);
        tvPassword = findViewById(R.id.activity_main_tv_pass);
        edtSearch = findViewById(R.id.edt_search);
        btnSearch = findViewById(R.id.btn_search);
        listType = findViewById(R.id.listType);
        // get User from login
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String pass = intent.getStringExtra("password");
        tvUserName.setText("user: "+username);
        tvPassword.setText("pass:"+pass);
        // signOut
        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //display book to gridview
        books = new ArrayList<>();
        books.clear();
        database = new MyDatabase(this);
        books = database.getAllBook();
        myAdapter = new Adapter(this, R.layout.item_list, books);
        gridView.setAdapter(myAdapter);
        // long click in item to edit or delete book
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {

                final Book book = books.get(position);
                CharSequence[] items = {"Sửa", "Xoá"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //sửa
//                            Toast.makeText(MainActivity.this, "Sửa sách", Toast.LENGTH_SHORT).show();
                            showDialogUpdate(MainActivity.this, book);
                        } else {
                            //xoá
                            showDialogDelete(book.getId());
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddBook.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Add Book", Toast.LENGTH_SHORT).show();
            }
        });
//display all type of book
        BookType = new HashSet<>();
        for (Book b : books) {
            BookType.add(b.getType());
        }
        final ArrayList<String> bookType = new ArrayList<>();
        for (String str : BookType) {
            bookType.add(str);
        }
        arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,bookType);
        listType.setAdapter(arrayAdapter);
//search
        BookInType = new ArrayList<>();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = edtSearch.getText().toString().trim();
//                Log.d("Main", "onClick: "+type);
                if (type.isEmpty())
                    Toast.makeText(MainActivity.this, "Nhập sách cần tìm", Toast.LENGTH_SHORT).show();
                else {
                    BookInType = database.getBookByType(type);
                    Book bookFind = database.getBookByName(type);
                    if (BookInType.size() > 0) {
                        myAdapter = new Adapter(MainActivity.this, R.layout.item_list, BookInType);
                        gridView.setAdapter(myAdapter);
                    } else if (BookInType.size() == 0 && bookFind != null) {
                        BookInType.add(bookFind);
                        myAdapter = new Adapter(MainActivity.this, R.layout.item_list, BookInType);
                        gridView.setAdapter(myAdapter);
                    }else{
                        Log.d("Main", "onClick: khong co sach");
                        Toast.makeText(MainActivity.this, "Không có sách cần tìm", Toast.LENGTH_SHORT).show();
//                        myAdapter = new Adapter(MainActivity.this, R.layout.item_list, books);
//                        gridView.setAdapter(myAdapter);
                    }
                }
            }
        });

    }

    ImageView imgv;

    private void showDialogUpdate(Activity activity, final Book book1) {
        final Book book = book1;
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_book);
        imgv = dialog.findViewById(R.id.imgv_edit_book);
        final EditText editName = dialog.findViewById(R.id.edt_edit_name_book);
        final EditText editType = dialog.findViewById(R.id.edt_edit_type_book);
        final EditText editDes = dialog.findViewById(R.id.edt_edit_des_book);
        final EditText editAuthor = dialog.findViewById(R.id.edt_edit_author_book);
        ImageButton btnchoose = dialog.findViewById(R.id.btn_edit_Folder);
//        Button btnBack = dialog.findViewById(R.id.btn_edit_back);
        Button btnSave = dialog.findViewById(R.id.btn_save_book);
        //hiển thị thông tin trước đó
        editName.setText(book.getBookName());
        editType.setText(book.getType());
        editAuthor.setText(book.getAuthor());
        editDes.setText(book.getDescription());
        byte[] img = book.getImageUri();
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        imgv.setImageBitmap(bitmap);
        // set kích thước dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.9);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
// chọn ảnh
        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });
        // click cập nhật
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    book.setBookName(editName.getText().toString());
                    book.setAuthor(editAuthor.getText().toString());
                    book.setType(editType.getText().toString());
                    book.setDescription(editDes.getText().toString());
                    book.setImageUri(imageViewToByte(imgv));
                    Log.d("Main", "onClick: edit book successfull");
                    database.updateBook(book);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                refreshList();
            }
        });
    }

    private void showDialogDelete(final int id) {
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this);

        dialogDelete.setTitle("Chú ý!!");
        dialogDelete.setMessage("Bạn có muốn xoá cuốn sách này?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    database.deleteBook(id);
                    Toast.makeText(getApplicationContext(), "Xoá xong!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
                refreshList();
            }
        });

        dialogDelete.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(this, "not permission", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void refreshList() {
//        books.clear();
        books = database.getAllBook();
        myAdapter.notifyDataSetChanged();
    }



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        myAdapter = new Adapter(MainActivity.this, R.layout.item_list, books);
        gridView.setAdapter(myAdapter);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
