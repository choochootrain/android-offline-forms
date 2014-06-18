package com.choochootrain.offlineform.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.choochootrain.offlineform.app.forms.builder.FormBuilder;
import com.choochootrain.offlineform.app.forms.data.FormConfig;
import com.choochootrain.offlineform.app.forms.data.FormData;
import com.choochootrain.offlineform.app.forms.queue.FormQueue;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";

    private FormQueue formQueue;
    private FormBuilder formBuilder;
    private Button submitButton;
    private LinearLayout formLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_form);

        formLayout = (LinearLayout)findViewById(R.id.form_layout);

        FormConfig.init(this);

        //TODO use external file for reading
        formBuilder = new FormBuilder(this, formLayout, FormConfig.load(this));
        formBuilder.populate();

        submitButton = new Button(this);
        submitButton.setText("submit");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormData data = formBuilder.processData();
                formBuilder.submitData(data, formQueue);
            }
        });
        formLayout.addView(submitButton);

        formQueue = new FormQueue(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //clear and reload from FormConfig data
        formLayout.removeAllViews();
        formBuilder.load(FormConfig.load(this));
        formBuilder.populate();
        formLayout.addView(submitButton);
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
            case R.id.edit_form:
                Intent intent = new Intent(this, EditFormActivity.class);
                startActivity(intent);
                break;
            case R.id.flush_cache:
                formQueue.flush();
        }

        return super.onOptionsItemSelected(item);
    }
}
