package com.choochootrain.offlineform.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class EditFormActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";
    private static final String FORM_JSON = "form.json";

    private EditText jsonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_form);

        jsonText = (EditText) findViewById(R.id.form_json);
        jsonText.setText(readJson());
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
            case R.id.save_json:
        }

        return super.onOptionsItemSelected(item);
    }

    private String readJson() {
        try {
            FileInputStream fis = openFileInput(FORM_JSON);
            return readStream(fis);
        } catch (FileNotFoundException e) {
            return "";
        }
    }

    private boolean writeJson(String s) {
        try {
            FileOutputStream fos = openFileOutput(FORM_JSON, Context.MODE_PRIVATE);
            fos.write(s.getBytes());
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private String readStream(InputStream is) {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.e(TAG, "error reading json file");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "could not close inputstream");
            }
        }

        return writer.toString();
    }
}
