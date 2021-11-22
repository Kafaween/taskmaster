package com.example.taskmaster;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;

import com.amplifyframework.datastore.generated.model.Task;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


public class AddTask extends AppCompatActivity {
    String img="";
    private static final String TAG = "AddTask";
    private String uploadedFileNames;
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
//        recordEvent();
        image();
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            onChooseFile(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });



        findViewById(R.id.upload).setOnClickListener(view -> {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("*/*");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            someActivityResultLauncher.launch(chooseFile);
        });



        Button addTaskButton = AddTask.this.findViewById(R.id.button_addTask);
        addTaskButton.setOnClickListener(view -> {
            EditText studentTitle = findViewById(R.id.edit_myTask);
            String TitleName = studentTitle.getText().toString();
            EditText Body = findViewById(R.id.edit_doSomething);
            String BodyB = (Body.getText().toString());
            EditText State = findViewById(R.id.stateinput);
            String StateB = (State.getText().toString());
            RadioButton b1=findViewById(R.id.radioButton1);
            RadioButton b2=findViewById(R.id.radioButton2);
            RadioButton b3=findViewById(R.id.radioButton3);


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

            dataStore(TitleName, BodyB, StateB,id);

            System.out.println(
                    "++++++++++++++++++++++++++++++++++++++++++++++++++" +
                            " Title Name: " + TitleName
                            +
                            "++++++++++++++++++++++++++++++++++++++++++++++++++"
            );


            Intent intent = new Intent(AddTask.this, MainActivity.class);
            startActivity(intent);
        });

    }

    private void dataStore(String title, String body, String state,String id) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String lat=sharedPreferences.getString("lat","");
        String lon=sharedPreferences.getString("lon","");
        System.out.println("waaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+lat+lon);
        String fileNameIfThere = uploadedFileNames == null ? "" : uploadedFileNames;
        Task task = Task.builder().teamId(id).title(title).body(body).state(state).fileName(fileNameIfThere).lat(lat).lon(lon).build();


        Amplify.API.mutate(ModelMutation.create(task),succuess-> {
            Log.i(TAG, "Saved to DYNAMODB");
        }, error -> {
            Log.i(TAG, "error saving to DYNAMODB");
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void onChooseFile(ActivityResult activityResult) throws IOException {

        Uri uri = null;
        if (activityResult.getData() != null) {
            uri = activityResult.getData().getData();
        }
        assert uri != null;
        String uploadedFileName = new Date().toString() + "." + getMimeType(getApplicationContext(), uri);

        File uploadFile = new File(getApplicationContext().getFilesDir(), "uploadFile");

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileUtils.copy(inputStream, new FileOutputStream(uploadFile));
        } catch (Exception exception) {
            Log.e("onChooseFile", "onActivityResult: file upload failed" + exception.toString());
        }

        Amplify.Storage.uploadFile(
                uploadedFileName,
                uploadFile,
                success -> Log.i("onChooseFile", "uploadFileToS3: succeeded " + success.getKey()),
                error -> Log.e("onChooseFile", "uploadFileToS3: failed " + error.toString())
        );
        uploadedFileNames= uploadedFileName;
    }
    public static String getMimeType(Context context, Uri uri) {
        String extension;

        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    private boolean permissionGranted(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }
//    private void recordEvent(){
//        AnalyticsEvent event = AnalyticsEvent.builder()
//                .name("Launch Add Task Activity")
//                .addProperty("Channel", "SMS")
//                .addProperty("Successful", true)
//                .addProperty("ProcessDuration", 792)
//                .addProperty("UserAge", 120.3)
//                .build();
//
//        Amplify.Analytics.recordEvent(event);
//    }

    public  void onClickGetlocation(View view){
        Intent intent = new Intent(AddTask.this, Maps.class);
        startActivity(intent);
    }
    public void image(){
    Intent intent = getIntent();
    String action = intent.getAction();
    String type = intent.getType();
    ImageView image = findViewById(R.id.imgeViewId);
        if (Intent.ACTION_SEND.equals(action) && type != null) {
        if (type.startsWith("image/")) {
            Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (imageUri != null) {
                image.setImageURI(imageUri);

            }}}}}