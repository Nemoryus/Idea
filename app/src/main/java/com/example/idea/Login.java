package com.example.idea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class  Login extends AppCompatActivity {
    private EditText password,email;
    private Button loginButton, adminButton,userButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        loginButton = findViewById(R.id.loginButton);
//        adminButton = findViewById(R.id.adminButton);
//        userButton = findViewById(R.id.userButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();

                if(TextUtils.isEmpty(txtEmail)|| TextUtils.isEmpty(txtPassword)){
                    Toast.makeText(Login.this,"All fileds are required",Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(txtEmail,txtPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            boolean yesNo = task.isSuccessful();
                             if(task.isSuccessful() && txtEmail.equals("errorsvk@gmail.com")){
                                Intent mainActivity = new Intent(Login.this, Admin.class);
                                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(mainActivity);
                                finish();
                             }else if (task.isSuccessful()){
                                 Intent mainActivity = new Intent(Login.this, User.class);
                                 mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                 startActivity(mainActivity);
                                 finish();
                             }else{
                                 Toast.makeText(Login.this,"Authentication failed!",Toast.LENGTH_SHORT).show();
                             }
                        }
                    });


                }

            }
        });

//        userButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent adminActivity = new Intent(Login.this, User.class);
//                startActivity(adminActivity);
//            }
//        });
//
//
//        adminButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent adminActivity = new Intent(Login.this, Admin.class);
//                startActivity(adminActivity);
//            }
//        });

    }


}
