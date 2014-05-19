package com.choochootrain.offlineform.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.choochootrain.offlineform.app.forms.data.FormConfig;

public class EditFormActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";

    private EditText jsonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_form);

        jsonText = (EditText) findViewById(R.id.form_json);
        jsonText.setText(FormConfig.load(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.load_json:
                jsonText.setText(FormConfig.load(this));
                Toast.makeText(this, "Loaded from file", Toast.LENGTH_SHORT).show();
                break;
            case R.id.save_json:
                FormConfig.save(this, jsonText.getText().toString());
                Toast.makeText(this, "Saved to file", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
