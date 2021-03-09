package com.example.idea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;

public class Admin extends AppCompatActivity {
    private EditText nameEditText, categoryEditText, authorEditText, bodyEditText;
    private Button saveButton;
    private static final String CHANNEL_ID = "categoryNotification";
    private static final String CHANNEL_NAME = "Notification";
    private static final String CHANNEL_DESC = "Notification desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        nameEditText = findViewById(R.id.idEditTextName);
        categoryEditText = findViewById(R.id.idEditTextCategory);
        authorEditText = findViewById(R.id.idEditTextAuthor);
        bodyEditText = findViewById(R.id.idEditTextBody);
        saveButton = findViewById(R.id.saveButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String category = categoryEditText.getText().toString().trim();
                String author = authorEditText.getText().toString().trim();
                String body = bodyEditText.getText().toString().trim();

                ArrayList<String> listOFCategories = new ArrayList<String>( Arrays.asList("Gastronomia", "E-shop", "Sluzby","Manualn praca","Stavebnictvo") );

                if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(category) || !TextUtils.isEmpty(author)){
                    Database database = new Database( Admin.this);
                    Boolean existCategory = false;
                    for (String categoryFromList : listOFCategories) {
                        if (categoryFromList.equals(category)) existCategory = true;
                    }

                    if (existCategory){
                        long result = database.addIdeaIntoDatabase(name,category,author,body);

                        authorEditText.setText("");
                        categoryEditText.setText("");
                        nameEditText.setText("");
                        bodyEditText.setText("");

                        if (result == -1){
                            Toast.makeText(Admin.this,"Failed: "+result,Toast.LENGTH_SHORT).show();
                        }else{
                            notification();
                            Toast.makeText(Admin.this,"Saved",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Admin.this,"The specified category does not exist.",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(Admin.this,"Fill everything!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void notification(){
        String message = "Pribudol ďalší nový nápad";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.rocket_small)
                .setContentTitle("Naštartuj to!!!")
                .setContentText(message )
                .setPriority(PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,notificationBuilder.build());


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(Admin.this, Login.class));
        finish();
    }
}