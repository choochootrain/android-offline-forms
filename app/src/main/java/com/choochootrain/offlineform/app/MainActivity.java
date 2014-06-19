package com.choochootrain.offlineform.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.choochootrain.offlineform.app.forms.builder.FormBuilder;
import com.choochootrain.offlineform.app.forms.data.FormConfig;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";

    private FormBuilder formBuilder;
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        //clear and reload from FormConfig data
        formBuilder.repopulate();
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
                formBuilder.formQueue.flush();
                break;
            case R.id.change_target:
                final EditText input = new EditText(MainActivity.this);
                input.setText(formBuilder.getTarget());
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(getString(R.string.change_target));
                alert.setMessage(getString(R.string.change_target_description));
                alert.setView(input);
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        formBuilder.setTarget(value);
                        formBuilder.repopulate();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
