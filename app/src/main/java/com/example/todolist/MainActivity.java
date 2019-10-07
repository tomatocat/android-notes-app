package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayList<String> notesContent;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        items = new ArrayList<>();
        ArrayAdapter<String> noteAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        Button btn = findViewById(R.id.addlist);
        ListView itemList = findViewById(R.id.list_view);
        notesContent = new ArrayList<>();
        //file names: name1, content1
        //while file exists, load file
        String title = "name" + num;
        while (fileExists(title)) {
            //load content
            items.add(num, open(title));
            notesContent.add(num, open("content" + num));
            num += 1;
            title = "name" + num;
        }
        itemList.setAdapter(noteAdapter);

        //new note
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,
                        TodoItem.class);
                i.putExtra("title", "note" + num);
                i.putExtra("content", "");
                i.putExtra("num", num);
                startActivityForResult(i, 1);
            }
        });

        //existing note
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent i = new Intent(MainActivity.this,
                        TodoItem.class);
                //number of existing files
                i.putExtra("title", items.get(position));
                i.putExtra("content", notesContent.get(position));
                i.putExtra("num", position);
                startActivityForResult(i, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //get title, content, write to files. save which file it is
        if (resultCode == RESULT_OK) {
            // Extract name value from result extras
            String name = data.getExtras().getString("title");
            String internals = data.getExtras().getString("content");
            int number = data.getExtras().getInt("num");
            save("name" + number, name);
            save("content" + number, internals);
            if (requestCode == 2) {
                items.set(number, name);
                notesContent.set(number, internals);
            } else {
                items.add(number, name);
                notesContent.add(number, internals);
            }
        }
    }

    private void save(String fileName, String textToSave) {
        try {
            OutputStreamWriter out =
                    new OutputStreamWriter(openFileOutput(fileName, 0));
            out.write(textToSave);
            out.close();
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        } catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /** Checks if the file denoted by fileName exists. **/
    private boolean fileExists(String fileName) {
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }

    /** Opens the file denoted by fileName and returns the contents of the file. **/
    private String open(String fileName) {
        String content = "";
        if (fileExists(fileName)) {
            try {
                InputStream in = openFileInput(fileName);
                if ( in != null) {
                    InputStreamReader tmp = new InputStreamReader( in );
                    BufferedReader reader = new BufferedReader(tmp);
                    String str;
                    StringBuilder buf = new StringBuilder();
                    while ((str = reader.readLine()) != null) {
                        buf.append(str + "\n");
                    } in .close();
                    content = buf.toString();
                }
            } catch (java.io.FileNotFoundException e) {} catch (Throwable t) {
                Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return content;
    }


}
