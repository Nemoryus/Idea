package com.example.idea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        TextView notificationTextView = findViewById(R.id.idNotification);

        String message = getIntent().getStringExtra("message");
        notificationTextView.setText(message);
    }
}