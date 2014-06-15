package com.choochootrain.offlineform.app.forms.builder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.text.format.Time;

import com.choochootrain.offlineform.app.forms.queue.FormQueue;
import com.choochootrain.offlineform.app.forms.data.FormConfig;
import com.choochootrain.offlineform.app.forms.data.FormData;
import com.choochootrain.offlineform.app.network.HttpPostTask;
import com.google.gson.Gson;

//TODO refactor
public class FormBuilder {
    private static final String TAG = "FormBuilder";
    private static final String DEBUG_URL = "http://192.168.1.65:5000/form";

    private Context context;
    private Gson gson;
    private ConnectivityManager connectivityManager;
    private FormConfig formConfig;
    private LinearLayout layout;

    public FormBuilder(Context context, LinearLayout layout, String formData) {
        this.context = context;
        this.layout = layout;
        this.gson = new Gson();
        this.formConfig = gson.fromJson(formData, FormConfig.class);
        this.connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void populate() {
        for (int i = 0; i < formConfig.elements.length; i++) {
            formConfig.elements[i].inflate(context, layout);
        }
    }

    public FormData processData() {
        FormData data = new FormData(formConfig.elements.length);
        data.title = formConfig.title;
        data.timestamp = new Time().toMillis(false);

        for (int i = 0; i < formConfig.elements.length; i++) {
            data.elements[i] = formConfig.elements[i].getData(layout);
        }

        return data;
    }

    //TODO use json and store offline
    public void submitData(FormData data, FormQueue queue) {
        data.target = DEBUG_URL;
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
        return networkInfo.isConnected();
    }

    private void clearForm() {
        for (int i = 0; i < formConfig.elements.length; i++) {
            formConfig.elements[i].clear(layout);
        }
    }
}
