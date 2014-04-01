package com.choochootrain.offlineform.app.forms;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class FormBuilder {
    private static final String TAG = "FormBuilder";
    private Context context;
    private Gson gson;

    public FormBuilder(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public void populateForm(LinearLayout layout, String formDataString) {
        FormConfig formConfig = gson.fromJson(formDataString, FormConfig.class);

        Toast.makeText(context, formConfig.title, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < formConfig.elements.length; i++) {
            addFormElement(layout, formConfig.elements[i]);
        }
    }

    private void addFormElement(LinearLayout layout, FormElement element) {
        //TODO implement other types
        if (element.type.equals("text")) {
            TextView t = new TextView(context);
            t.setText("text " + element.name);
            layout.addView(t);
        } else if (element.type.equals("numeric")) {
            TextView t = new TextView(context);
            t.setText("numeric " + element.name);
            layout.addView(t);
        }
    }

    private class FormConfig {
        private String title;
        private FormElement[] elements;
    }

    private class FormElement {
        private String name;
        private String type;
        private String id;
    }
}
