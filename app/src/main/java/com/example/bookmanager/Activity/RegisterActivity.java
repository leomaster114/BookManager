package com.example.bookmanager.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookmanager.Database.MyDatabase;
import com.example.bookmanager.Model.User;
import com.example.bookmanager.R;

public class RegisterActivity extends AppCompatActivity {
    EditText edt_email, edt_pass;
    TextView tv_signIn;
    Button btn_SignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       tv_signIn = findViewById(R.id.tv_signIn);
        btn_SignUp = findViewById(R.id.btn_signUp);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pw);
        final MyDatabase database = new MyDatabase(this);
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edt_email.getText().toString();
                String password = edt_pass.getText().toString();
                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Điền đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                }else if(password.length()<6){
                    Toast.makeText(RegisterActivity.this,"Mật khẩu phải nhiều hơn 6 kí tự",Toast.LENGTH_SHORT).show();
                }else{
                    User newUser = new User(username,password);
                    database.addUser(newUser);
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        tv_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
