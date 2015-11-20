package com.guuwork.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private EditText editTextField;
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        editTextField = (EditText)findViewById(R.id.editText);
        editTextField.setText(getIntent().getStringExtra("item"));
        //places cursor at the end
        editTextField.setSelection(editTextField.length());
        itemPosition = getIntent().getIntExtra("position", -1);
    }


    public void onEdit(View view) {
        EditText editResult = (EditText)findViewById(R.id.editText);

        Intent data = new Intent();
        data.putExtra("item", editResult.getText().toString());
        data.putExtra("position", itemPosition);
        data.putExtra("code", 200);

        setResult(RESULT_OK, data);

        finish();
    }

}
