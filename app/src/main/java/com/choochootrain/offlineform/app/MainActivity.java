package com.choochootrain.offlineform.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.choochootrain.offlineform.app.forms.FormBuilder;
import com.choochootrain.offlineform.app.forms.data.FormData;
import com.choochootrain.offlineform.app.forms.queue.FormQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";

    private FormQueue formQueue;
    private FormBuilder formBuilder;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_form);

        LinearLayout formLayout = (LinearLayout)findViewById(R.id.form_layout);

        formBuilder = new FormBuilder(this);

        formBuilder.populateForm(formLayout, readRawResource(R.raw.form));

        submitButton = new Button(this);
        submitButton.setText("submit");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormData data = formBuilder.processData();
                if (!formBuilder.submitData(data))
                    formQueue.add(data);
            }
        });
        formLayout.addView(submitButton);

        formQueue = new FormQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                //TODO implement
                break;
            case R.id.flush_cache:
                formQueue.flush();
        }

        return super.onOptionsItemSelected(item);
    }

    private String readRawResource(int id) {
        InputStream is = getResources().openRawResource(id);
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
