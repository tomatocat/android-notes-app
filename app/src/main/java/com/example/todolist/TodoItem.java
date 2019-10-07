package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class TodoItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item);

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        final int num = getIntent().getIntExtra("num", 0);

        Button btn = findViewById(R.id.add);
        final EditText name = findViewById(R.id.title);
        final EditText text = findViewById(R.id.text);

        name.setText(title);
        text.setText(content);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent data = new Intent();
            data.putExtra("title", name.getText().toString());
            data.putExtra("content", text.getText().toString());
            data.putExtra("num", num);
            setResult(RESULT_OK, data);
            finish();
            }
        });

    }

}
