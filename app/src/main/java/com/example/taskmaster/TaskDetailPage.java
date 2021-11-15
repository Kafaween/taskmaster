package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.squareup.picasso.Picasso;

public class TaskDetailPage extends AppCompatActivity {
    private String fileURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_page);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String instName = sharedPreferences.getString("TaskName","Boos");
        String filename = sharedPreferences.getString("Filename","");

        TextView welcome = findViewById(R.id.title_detail);

        welcome.setText("Welcome " + instName);


        TextView fileLinkDetail = findViewById(R.id.fileLinkDetail);
        fileLinkDetail.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(fileURL));
            startActivity(i);
        });
        System.out.println(filename);



        if (filename!= null) {

            Amplify.Storage.getUrl(
                    filename,
                    result -> {
                        Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
                        runOnUiThread(() -> {
                            if (filename.endsWith("png")
                                    || filename.endsWith("jpg")
                                    || filename.endsWith("jpeg")
                                    || filename.endsWith("gif")) {
                                ImageView taskImageDetail = findViewById(R.id.taskImageDetail);
                                System.out.println(result.getUrl());
                                Picasso.get().load(String.valueOf(result.getUrl())).into(taskImageDetail);

                                taskImageDetail.setVisibility(View.VISIBLE);
                            }else{
                                fileURL = String.valueOf(result.getUrl());
//                                String link = "<a href=\""+ result.getUrl() + "\">Download the linked file</a>";
                                fileLinkDetail.setVisibility(View.VISIBLE);
                            }
                        });
                    },
                    error -> Log.e("MyAmplifyApp", "URL generation failure", error)
            );
        }

    }
}