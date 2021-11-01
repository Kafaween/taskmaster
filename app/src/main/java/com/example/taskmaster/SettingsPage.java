package com.example.taskmaster;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SettingsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        findViewById(R.id.save_user).setOnClickListener(view -> {
            TextView text = findViewById(R.id.Username);

            String name =text.getText().toString();
            editor.putString("UserName",name);
            editor.apply();
        });
    }

}