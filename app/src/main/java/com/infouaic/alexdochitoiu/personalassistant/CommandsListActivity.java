package com.infouaic.alexdochitoiu.personalassistant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.infouaic.alexdochitoiu.personalassistant.Utils.MAX_COMMANDS;
import static com.infouaic.alexdochitoiu.personalassistant.Utils.commands;

public class CommandsListActivity extends AppCompatActivity {

    ListView lv;
    public static CustomAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_commands_list);

        loadCommands(getApplication());

        lv = findViewById(R.id.commands_lv);
        adapter = new CustomAdapter(commands);

        lv.setAdapter(adapter);
    }

    public static void saveCommands(Context ctx) {
        SharedPreferences sharedPreferences =  ctx.getSharedPreferences("COMMANDS", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(commands);
        editor.putString("COMMANDS_LIST", json);
        editor.apply();
    }

    public static void loadCommands(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("COMMANDS", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("COMMANDS_LIST", null);
        Type type = new TypeToken<ArrayList<Command>>(){}.getType();
        commands = gson.fromJson(json, type);
        if(commands == null) commands = new ArrayList<>(MAX_COMMANDS);
    }
}