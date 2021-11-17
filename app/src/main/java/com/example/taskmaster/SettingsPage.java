package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;

public class SettingsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        recordEvent();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        findViewById(R.id.save_user).setOnClickListener(view -> {
            TextView text = findViewById(R.id.Username);
            RadioButton b1=findViewById(R.id.radioButton1s);
            RadioButton b2=findViewById(R.id.radioButton2s);
            RadioButton b3=findViewById(R.id.radioButton3s);

            String id = null;
            if(b1.isChecked()){
                id="1";
            }
            else if(b2.isChecked()){
                id="2";
            }
            else if(b3.isChecked()){
                id="3";
            }

            String name =text.getText().toString();
            editor.putString("UserName",name);
            editor.putString("Team",id);
            editor.apply();
            Intent gotToInst = new Intent(SettingsPage.this,MainActivity.class);
            startActivity(gotToInst);
        });
    }

    private void recordEvent(){
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("Launch Setting activity")
                .addProperty("Channel", "SMS")
                .addProperty("Successful", true)
                .addProperty("ProcessDuration", 792)
                .addProperty("UserAge", 120.3)
                .build();

        Amplify.Analytics.recordEvent(event);
    }

}