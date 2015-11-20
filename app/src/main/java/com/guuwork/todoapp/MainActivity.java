package com.guuwork.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    private EditText etWords;
    private ListView lvItems;
    private final int CODE_EDIT = 200;//edit text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etWords = (EditText)findViewById(R.id.etWords);
        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<>();

        readItems();

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewClickListener();
//        setupListViewClickListener();
    }

    public void onSubmit(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etWords);
        String itemText = etNewItem.getText().toString();
        if (!itemText.isEmpty()) {
            itemsAdapter.add(itemText);
            etNewItem.setText("");
            writeItems();
        } else {
            Toast.makeText(MainActivity.this, "Enter Any Text", Toast.LENGTH_SHORT).show();
        }

    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupListViewClickListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("item", items.get(position));
                i.putExtra("position", position);
                startActivityForResult(i, CODE_EDIT);
            }
        });
    }


    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            items = new ArrayList(FileUtils.readLines(todoFile));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CODE_EDIT) {
            String result = data.getStringExtra("item");
            if (!result.isEmpty()) {

                items.set(data.getIntExtra("position", -1), result);
                itemsAdapter.notifyDataSetChanged();

                writeItems();
            } else {
                Toast.makeText(MainActivity.this, "Error retrieving edited text", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
