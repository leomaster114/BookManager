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

public class LoginActivity extends AppCompatActivity {
    EditText edt_email, edt_pass;
    Button btn_login;
    TextView tv_register;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pw);
        btn_login = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tv_resgister);
        final MyDatabase database = new MyDatabase(this);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edt_email.getText().toString().equals("") && !edt_pass.getText().toString().equals("")) {
                    username = edt_email.getText().toString();
                    password = edt_pass.getText().toString();
                    // find user in database
                    User user = database.findUserByName(username);
                    if(user.getUsername().equals("")){
                        Toast.makeText(LoginActivity.this, "UserName not existed!", Toast.LENGTH_SHORT).show();
                    }else if(!user.getPassword().equals(password)){
                        Toast.makeText(LoginActivity.this, "password incorect!", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        String name = username;
                        intent.putExtra("username", name);
                        intent.putExtra("password",password);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "fill email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}
