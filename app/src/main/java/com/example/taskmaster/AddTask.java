package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;

public class AddTask extends AppCompatActivity {

    private static final String TAG = "AddTask";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Button addTaskButton = AddTask.this.findViewById(R.id.button_addTask);
        addTaskButton.setOnClickListener(view -> {
            EditText studentTitle = findViewById(R.id.edit_myTask);
            String TitleName = studentTitle.getText().toString();

            EditText Body = findViewById(R.id.edit_doSomething);
            String BodyB = (Body.getText().toString());

            EditText State = findViewById(R.id.stateinput);
            String StateB = (State.getText().toString());

            dataStore(TitleName, BodyB, StateB);

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

    private void dataStore(String title, String body, String state) {
        Task task = Task.builder().title(title).state(state).body(body).build();

        // save with the datastore
        Amplify.DataStore.save(task, result -> {
            Log.i(TAG, "Task Saved");
        }, error -> {
            Log.i(TAG, "Task Not Saved");
        });

    }
}