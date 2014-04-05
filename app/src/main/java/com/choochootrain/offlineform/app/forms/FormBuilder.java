package com.choochootrain.offlineform.app.forms;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

public class FormBuilder {
    private static final String TAG = "FormBuilder";
    private Context context;
    private Gson gson;
    private FormConfig formConfig;
    private LinearLayout layout;

    public FormBuilder(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public void populateForm(final LinearLayout layout, String formDataString) {
        this.layout = layout;
        formConfig = gson.fromJson(formDataString, FormConfig.class);

        Toast.makeText(context, formConfig.title, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < formConfig.elements.length; i++) {
            addFormElement(layout, formConfig.elements[i]);
        }

        Button submitButton = new Button(context);
        submitButton.setText("submit");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormData[] data = processData();
            }
        });
        layout.addView(submitButton);
    }

    private FormData[] processData() {
        FormData[] data = new FormData[formConfig.elements.length];
        for (int i = 0; i < formConfig.elements.length; i++) {
            FormElement element = formConfig.elements[i];
            //TODO implement other types
            if (element.type.equals("text")) {
                EditText t = (EditText) layout.findViewById(element.id.hashCode());
                data[i] = new FormData();
                data[i].id = element.id;
                data[i].value = t.getText().toString();
            } else if (element.type.equals("numeric")) {
                EditText t = (EditText) layout.findViewById(element.id.hashCode());
                data[i] = new FormData();
                data[i].id = element.id;
                data[i].value = t.getText().toString();
            }
        }

        return data;
    }

    //TODO use json and store offline
    private void submitData(FormData[] data) {
        String s = "";
        for (int i = 0; i < data.length; i++) {
           s += data[i].id + ": " + data[i].value + ", ";
        }

        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private void addFormElement(LinearLayout layout, FormElement element) {
        //TODO implement other types
        if (element.type.equals("text")) {
            EditText t = new EditText(context);
            t.setInputType(InputType.TYPE_CLASS_TEXT);
            t.setText(element.name);
            t.setId(element.id.hashCode());
            layout.addView(t);
        } else if (element.type.equals("numeric")) {
            EditText t = new EditText(context);
            t.setInputType(InputType.TYPE_CLASS_NUMBER);
            t.setText(element.name);
            t.setId(element.id.hashCode());
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

    private class FormData {
        private String id;
        private String value;
    }
}
