package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String instName = sharedPreferences.getString("UserName","user");
        String Team = sharedPreferences.getString("Team","noTeam");
        System.out.println(instName);
        System.out.println("-------------------------------------------------------------------");
        System.out.println(Team);
        TextView welcome = findViewById(R.id.user);
        welcome.setText( instName+"’s Tasks");

//            configureAmplify();
            creatTeams();


        RecyclerView allTasksRecyclerView = findViewById(R.id.recyclerview);
        List<Task> tasks= new ArrayList<>();
        if(Team.equals("noTeam")){
             tasks = GetData(allTasksRecyclerView);
        }
        else{
             tasks = GetData2(allTasksRecyclerView);
        }
        Log.i("kafawen",tasks.toString());
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


        SharedPreferences.Editor editor = sharedPreferences.edit();


        findViewById(R.id.Setting).setOnClickListener(view -> {
            Intent gotToStd = new Intent(MainActivity.this, SettingsPage.class);
            startActivity(gotToStd);
        });

    }


    public void onClickSignOut(View view){
        Amplify.Auth.signOut(
                () -> Log.i("AuthQuickstart", "Signed out successfully"),
                error -> Log.e("AuthQuickstart", error.toString())
        );
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String instName = sharedPreferences.getString("UserName","user");
        String Team = sharedPreferences.getString("Team","noTeam");
        System.out.println(instName);
        TextView welcome = findViewById(R.id.user);
        welcome.setText( instName+"’s Tasks");
    }

    private void configureAmplify() {
        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify", error);
        }}

    private  List<Task> GetData( RecyclerView allTaskDataRecyclerView ){
        Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                allTaskDataRecyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }
        });
            List<Task> foundTask=new ArrayList<>();
            Amplify.API.query(
                    ModelQuery.list(Task.class),
                    response -> {
                        for (Task todo : response.getData()) {
                            foundTask.add(todo);
                            foundTask.toString();
                            Log.i("MyAmplifyApp", foundTask.toString());
                            Log.i("MyAmplifyApp", "Successful query, found posts.");
                        }
                        handler.sendEmptyMessage(1);
                    },
                    error -> Log.e("MyAmplifyApp", "Query failure", error)
            );

            return  foundTask;
        }
    private  List<Task> GetData2( RecyclerView allTaskDataRecyclerView ){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Team = sharedPreferences.getString("Team","noTeam");
        System.out.println("-------------------------------------------------------------------");
        System.out.println(Team);
        Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                allTaskDataRecyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }
        });
        List<Task> foundTask=new ArrayList<>();
        Amplify.API.query(
                ModelQuery.list(Task.class,Task.TEAM_ID.contains(Team)),
                response -> {
                    for (Task todo : response.getData()) {
                        foundTask.add(todo);
                        foundTask.toString();
                        Log.i("MyAmplifyApp", foundTask.toString());
                        Log.i("MyAmplifyApp", "Successful query, found posts.");
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );

        return  foundTask;
    }
        private void creatTeams(){
        AtomicBoolean x= new AtomicBoolean(false);
            Amplify.API.query(
                    ModelQuery.list(Team.class),
                    response -> {
                        if(response.getData().getRequestForNextResult()==null){
                            System.out.println("alooooolllllllllllllllllllllll");
                            System.out.println(response.getData().getRequestForNextResult());
                            x.set(true);
                            Log.i("Teams", "Successful query, found teams.");
                        }
                    },
                    error -> Log.e("MyAmplifyApp", "Query failure", error)
            );
            if(x.equals(false)){
            Team todo1 = Team.builder()
                    .name("Team 1").id("1")
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(todo1),
                    response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                    error -> Log.e("MyAmplifyApp", "Create failed", error)
            );
            Team todo2 = Team.builder()
                    .name("Team 2").id("2")
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(todo2),
                    response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                    error -> Log.e("MyAmplifyApp", "Create failed", error)
            );
            Team todo3 = Team.builder()
                    .name("Team 3").id("3")
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(todo3),
                    response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                    error -> Log.e("MyAmplifyApp", "Create failed", error)
            );
        } }
}