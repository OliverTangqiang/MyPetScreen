package com.example.mypetscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //initialize variable
    EditText editText;
    Button btAdd, btReset;
    RecyclerView recyclerView;

    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    MainAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign variables
        editText = findViewById(R.id.pet_name);
        btAdd = findViewById(R.id.bt_add);
        btReset = findViewById(R.id.bt_reset);
        recyclerView = findViewById(R.id.recycler_view);

        //Initialise database
        database = RoomDB.getInstance(this);

        //Store database value in data list
        dataList = database.mainDao().getAll();

        //Initialise linear layout
        linearLayoutManager = new LinearLayoutManager(this);

        //Set layout manager
        recyclerView.setLayoutManager(linearLayoutManager);

        //Initialise adapter
        adapter = new MainAdapter(MainActivity.this,dataList);

        //set adapter
        recyclerView.setAdapter(adapter);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get string from edit text
                String sText = editText.getText().toString().trim();
                //Check condition
                if(!sText.equals("")){
                    //when text is not empty
                    //Initialise main data
                    MainData data = new MainData();

                    //Set text on main data
                    data.setText(sText);

                    //Insert text in database
                    database.mainDao().insert(data);

                    //Clear edit text
                    editText.setText("");

                    //Notify when data is inserted
                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    adapter.notifyDataSetChanged();
                }

            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Delete all data from database
                database.mainDao().reset(dataList);
                //Notify when all data deleted
                dataList.clear();
                dataList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
            }
        });
    }
}