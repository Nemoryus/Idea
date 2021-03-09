package com.example.idea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registration extends AppCompatActivity {
    private EditText userName,password,email;
    private Button registrationButton;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

//        Toolbar registrationToolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(registrationToolbar);
//        getSupportActionBar().setTitle("Registration");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registrationButton = findViewById(R.id.registrationButton);

        auth = FirebaseAuth.getInstance();

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername = userName.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();

                if(TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtPassword) || TextUtils.isEmpty(txtEmail)){

                    Toast.makeText(Registration.this, "All files are required", Toast.LENGTH_SHORT).show();

                }else if(txtPassword.length() < 6){

                    Toast.makeText(Registration.this, "Password must be at lest 6 characters",Toast.LENGTH_SHORT).show();

                }else {

                    registration(txtUsername,txtEmail,txtPassword);
                    userName.getText().clear();
                    email.getText().clear();
                    password.getText().clear();

                }


            }
        });
    }

    private void registration(final String userName, String email, String password ){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userId = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                    HashMap<String, String> userHashMap = new HashMap<String, String>();
                    userHashMap.put("id", userId);
                    userHashMap.put("username", userName);
                    userHashMap.put("imgUrl", "default");

                    reference.setValue(userHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                Intent intent = new Intent(Registration.this, User.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                    Toast.makeText(Registration.this, "Registration was successful",Toast.LENGTH_SHORT).show();

                }else{

                    Toast.makeText(Registration.this, "You can't registration whit this email or password", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
