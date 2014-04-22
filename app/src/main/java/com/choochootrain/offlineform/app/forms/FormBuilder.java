package com.choochootrain.offlineform.app.forms;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.text.format.Time;

import com.choochootrain.offlineform.app.forms.queue.FormQueue;
import com.choochootrain.offlineform.app.forms.data.FormConfig;
import com.choochootrain.offlineform.app.forms.data.FormData;
import com.choochootrain.offlineform.app.network.Http;
import com.google.gson.Gson;

//TODO refactor
public class FormBuilder {
    private static final String TAG = "FormBuilder";
    private static final String DEBUG_URL = "http://127.0.0.1:5000/form";

    private Context context;
    private Gson gson;
    private ConnectivityManager connectivityManager;
    private FormQueue queue;
    private FormConfig formConfig;
    private LinearLayout layout;

    public FormBuilder(Context context) {
        this.context = context;
        this.gson = new Gson();
        this.connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.queue = new FormQueue(context);
    }

    public void populateForm(final LinearLayout layout, String formDataString) {
        this.layout = layout;
        this.formConfig = gson.fromJson(formDataString, FormConfig.class);

        Toast.makeText(context, formConfig.title, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < formConfig.elements.length; i++) {
            formConfig.elements[i].inflate(context, layout);
        }
    }

    public FormData processData() {
        FormData data = new FormData();
        data.title = formConfig.title;
        data.timestamp = new Time().toMillis(false);

        for (int i = 0; i < formConfig.elements.length; i++) {
            data.elements[i] = formConfig.elements[i].getData(layout);
        }

        return data;
    }

    //TODO use json and store offline
    public boolean submitData(FormData data) {
        data.target = DEBUG_URL;
        String jsonData = gson.toJson(data);
        Toast.makeText(context, jsonData, Toast.LENGTH_SHORT).show();
        clearForm();

        if (isOnline())
            return Http.post(context, data);
        else
            return false;
    }

    private boolean isOnline() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo.isConnected();
    }

    private void clearForm() {
        for (int i = 0; i < formConfig.elements.length; i++) {
            formConfig.elements[i].clear(layout);
        }
    }
}
