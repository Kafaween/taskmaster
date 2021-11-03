package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTask extends AppCompatActivity {

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
           String StateB = (Body.getText().toString());

           Task student = new Task(TitleName, BodyB,StateB);
           Long addedStudentID = AppDatabase.getInstance(getApplicationContext()).studentDao().insertStudent(student);

           System.out.println(
                   "++++++++++++++++++++++++++++++++++++++++++++++++++" +
                           " Student ID : " + addedStudentID
                           +
                           "++++++++++++++++++++++++++++++++++++++++++++++++++"
           );


           Intent intent = new Intent(AddTask.this, MainActivity.class);
           startActivity(intent);
       });

    }


}