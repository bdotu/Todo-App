package com.dotu.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // ArrayList holds the items on the list
    ArrayList<String> items;
    // The adapter allows us to easily display the contents of the ArrayList within a ListView
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    // This serves as a tag (key) for the entry being edited.
    public final static String EDITED_ITEM = "com.dotu.simpletodo.ITEM";
    // REQUEST_CODE is an arbitrary value used to determine the Intent result type.
    private final int REQUEST_CODE = 20;
    // This holds the index of the entry being edited.
    // The index is later used to replace the old entry with the new one.
    private int indexOfEditItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        launchEditActivity();
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                });
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e){
            items = new ArrayList<String>();
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void launchEditActivity(){
        final Intent intent = new Intent(this, EditItemActivity.class);
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View item, int pos, long id) {
                        String textToEdit = parent.getItemAtPosition(pos).toString();
//                        Log.d("myTag", textToEdit);
                        indexOfEditItem = items.indexOf(textToEdit);
                        intent.putExtra(EDITED_ITEM, textToEdit);
//                        startActivity(intent);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
        );
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            String editedText = data.getExtras().getString("editedText");
//            Toast.makeText(this, editedText, Toast.LENGTH_SHORT).show();
            items.set(indexOfEditItem, editedText);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }
}
