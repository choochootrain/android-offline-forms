package com.choochootrain.offlineform.app.forms.builder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.choochootrain.offlineform.app.forms.queue.FormQueue;
import com.choochootrain.offlineform.app.forms.data.FormConfig;
import com.choochootrain.offlineform.app.forms.data.FormData;
import com.choochootrain.offlineform.app.network.HttpPostTask;
import com.google.gson.Gson;

//TODO refactor
public class FormBuilder {
    private static final String TAG = "FormBuilder";
    private static final String FORM_PREFERENCES = "form_preferences";
    private static final String TARGET_URL_KEY = "target_url";
    private static final String DEBUG_URL = "http://10.20.74.54:5000/form";

    private Context context;
    private Gson gson;
    private ConnectivityManager connectivityManager;
    private FormConfig formConfig;
    private LinearLayout layout;
    private Button submitButton;

    public FormQueue formQueue;

    public FormBuilder(Context context, LinearLayout layout, String formData) {
        this.context = context;
        this.layout = layout;
        this.gson = new Gson();
        load(formData);
        this.connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.formQueue = new FormQueue(context);

        submitButton = new Button(context);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormBuilder.this.processData();
            }
        });
    }

    public void load(String formData) {
        this.formConfig = gson.fromJson(formData, FormConfig.class);
    }

    public void populate() {
        for (int i = 0; i < formConfig.elements.length; i++) {
            formConfig.elements[i].inflate(context, layout);
        }

        submitButton.setText("Submit to " + getTarget());
        layout.addView(submitButton);
    }

    public void repopulate() {
        layout.removeAllViews();
        this.load(FormConfig.load(context));
        this.populate();
    }

    public FormData processData() {
        FormData data = new FormData(formConfig.elements.length);
        data.title = formConfig.title;
        data.timestamp = System.currentTimeMillis();

        for (int i = 0; i < formConfig.elements.length; i++) {
            data.elements[i] = formConfig.elements[i].getData(layout);
        }

        this.submitData(data, formQueue);

        return data;
    }

    //TODO use json and store offline
    public void submitData(FormData data, FormQueue queue) {
        data.target = getTarget();
        String jsonData = gson.toJson(data);
        Toast.makeText(context, jsonData, Toast.LENGTH_SHORT).show();
        clearForm();

        if (isOnline()) {
            new HttpPostTask(context, data, queue).execute();
        } else {
            queue.add(data);
        }
    }

    private boolean isOnline() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void clearForm() {
        for (int i = 0; i < formConfig.elements.length; i++) {
            formConfig.elements[i].clear(layout);
        }
    }

    public void setTarget(String value) {
        context.getSharedPreferences(FORM_PREFERENCES, context.MODE_PRIVATE).edit().putString(TARGET_URL_KEY, value).commit();
    }

    public String getTarget() {
        return context.getSharedPreferences(FORM_PREFERENCES, context.MODE_PRIVATE).getString(TARGET_URL_KEY, DEBUG_URL);
    }
}
