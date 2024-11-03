package com.onurerdem.yapilacaklarlistesi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onurerdem.yapilacaklarlistesi.Adapter.ToDoAdapter;
import com.onurerdem.yapilacaklarlistesi.Model.ToDoModel;
import com.onurerdem.yapilacaklarlistesi.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView tasksRecyclerview;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;

    private List<ToDoModel> taskList;
    private DatabaseHandler db;

    SharedPreferences preferences2;
    SharedPreferences.Editor editor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences2 = this.getSharedPreferences("dil", Context.MODE_PRIVATE);
        Locale locale = new Locale(preferences2.getString("dil",""));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskList = new ArrayList<>();

        tasksRecyclerview = findViewById(R.id.tasksRecyclerview);
        tasksRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(this);
        tasksAdapter = new ToDoAdapter(db, this);
        tasksRecyclerview.setAdapter(tasksAdapter);

        fab = findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);
        fab2 = findViewById(R.id.fab2);
        fab2.setColorFilter(Color.WHITE);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerview);

        /*ToDoModel task = new ToDoModel();
        task.setTask((String) getText(R.string.test_task));
        task.setStatus(0);
        task.setId(1);

        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);

        tasksAdapter.setTasks(taskList);*/

        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences2 = getApplicationContext().getSharedPreferences("dil", Context.MODE_PRIVATE);
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.language_settings);
                Button turkishButton = (Button) dialog.findViewById(R.id.turkishButton);
                Button englishButton = (Button) dialog.findViewById(R.id.englishButton);
                Button cancelLanguageButton = (Button) dialog.findViewById(R.id.cancelLanguageButton);
                TextView languageSettings = (TextView) dialog.findViewById(R.id.language_settings);
                languageSettings.setText(R.string.languages);
                turkishButton.setText(R.string.turkish);
                englishButton.setText(R.string.english);
                cancelLanguageButton.setText(R.string.cancel);
                turkishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor2 = preferences2.edit();
                        editor2.putString("dil","");
                        editor2.commit();
                        Locale locale = new Locale(preferences2.getString("dil",""));
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                });
                englishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor2 = preferences2.edit();
                        editor2.putString("dil","en");
                        editor2.commit();
                        Locale locale = new Locale(preferences2.getString("dil",""));
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                });
                cancelLanguageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.delete_task);
                builder.setMessage(R.string.delete_task_ask);
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor2 = preferences2.edit();
                        editor2.putString("dil","");
                        editor2.commit();
                        Locale locale = new Locale(preferences2.getString("dil",""));
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor2 = preferences2.edit();
                        editor2.putString("dil","en");
                        editor2.commit();
                        Locale locale = new Locale(preferences2.getString("dil",""));
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                //dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(adapter.getContext(), R.color.colorPrimaryDark));*/
            }
        });
    }

    @Override
    public void onBackPressed() {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("ÇIKIŞ");
        builder.setMessage("Çıkmak istediğinize emin misiniz?");
        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent cikis = new Intent(Intent.ACTION_MAIN);
                cikis.addCategory(Intent.CATEGORY_HOME);
                cikis.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(cikis);
            }
        });
        builder.show();*/
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.warning);
        Button yesButton = (Button) dialog.findViewById(R.id.yesButton);
        Button noButton = (Button) dialog.findViewById(R.id.noButton);
        TextView textWarning = (TextView) dialog.findViewById(R.id.textWarning);
        textWarning.setText(R.string.are_you_sure_you_want_to_get_out);
        yesButton.setText(R.string.yes);
        noButton.setText(R.string.no);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exit = new Intent(Intent.ACTION_MAIN);
                exit.addCategory(Intent.CATEGORY_HOME);
                exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(exit);
                dialog.dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}