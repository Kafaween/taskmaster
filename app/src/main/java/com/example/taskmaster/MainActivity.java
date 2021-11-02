package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Task> tasks = new ArrayList<Task>();

        tasks.add(new Task("Coding","think then code","new"));
        tasks.add(new Task("playing","safety first","assigned"));
        tasks.add(new Task("sleeping","sleep well","in progress"));
        tasks.add(new Task("games","Dota forever","complete"));

        RecyclerView allTasksRecyclerView = findViewById(R.id.recyclerview);
        allTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allTasksRecyclerView.setAdapter(new TaskAdapter(tasks));

        Button navToAddTask = MainActivity.this.findViewById(R.id.buttonMain_addTask);
        navToAddTask.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTask.class);
            startActivity(intent);
        });

        Button navToAllTasks = MainActivity.this.findViewById(R.id.buttonMain_allTask);
        navToAllTasks.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AllTasks.class);
            startActivity(intent);
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Button button1= findViewById(R.id.Task1);
        findViewById(R.id.Task1).setOnClickListener(view -> {

            String Task1 =button1.getText().toString();
            editor.putString("TaskName",Task1);
            editor.apply();
            Intent gotToStd = new Intent(MainActivity.this,TaskDetailPage.class);
            startActivity(gotToStd);
        });
        Button button2= findViewById(R.id.Task2);
        findViewById(R.id.Task2).setOnClickListener(view -> {

            String Task2 =button2.getText().toString();
            editor.putString("TaskName",Task2);
            editor.apply();
            Intent gotToInst = new Intent(MainActivity.this,TaskDetailPage.class);
            startActivity(gotToInst);
        });
        Button button3= findViewById(R.id.Task3);
        findViewById(R.id.Task3).setOnClickListener(view -> {

            String Task3 =button3.getText().toString();
            editor.putString("TaskName",Task3);
            editor.apply();
            Intent gotToInst = new Intent(MainActivity.this,TaskDetailPage.class);
            startActivity(gotToInst);
        });

        findViewById(R.id.Setting).setOnClickListener(view -> {
            Intent gotToStd = new Intent(MainActivity.this,SettingsPage.class);
            startActivity(gotToStd);
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String instName = sharedPreferences.getString("UserName","user");
        System.out.println(instName);
        TextView welcome = findViewById(R.id.user);
        welcome.setText( instName+"’s Tasks");
    }
}