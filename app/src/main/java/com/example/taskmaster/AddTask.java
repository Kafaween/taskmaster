package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
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
        Task task = Task.builder().teamId(id).title(title).body(body).state(state).build();


        Amplify.API.mutate(ModelMutation.create(task),succuess-> {
            Log.i(TAG, "Saved to DYNAMODB");
        }, error -> {
            Log.i(TAG, "error saving to DYNAMODB");
        });

    }
}