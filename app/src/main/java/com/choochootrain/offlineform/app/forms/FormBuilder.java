package com.choochootrain.offlineform.app.forms;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class FormBuilder {
    private static final String TAG = "FormBuilder";
    private Context context;

    public FormBuilder(Context context) {
        this.context = context;
    }

    public void populateForm(LinearLayout layout, String formDataString) {
        JSONObject formData;
        try {
            formData = new JSONObject(formDataString);
        } catch (JSONException e) {
           Log.e(TAG, "error parsing json");
           return;
        }

        Toast.makeText(context, formData.optString("title", "asdf"), Toast.LENGTH_SHORT).show();
    }
}
