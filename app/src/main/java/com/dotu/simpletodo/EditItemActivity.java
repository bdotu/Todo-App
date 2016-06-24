package com.dotu.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String editedItem = getIntent().getStringExtra(MainActivity.EDITED_ITEM);
        EditText etTextField = (EditText) findViewById(R.id.etTextField);
        assert etTextField != null;
        etTextField.setText(editedItem);
        onSave();
    }

    public void onSave(){
        Button btnSaveEdit = (Button) findViewById(R.id.btnSaveEdit);
        assert btnSaveEdit != null;
        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etTextField = (EditText) findViewById(R.id.etTextField);
                Intent data = new Intent();
                assert etTextField != null;
                data.putExtra("editedText", etTextField.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}
