package com.choochootrain.offlineform.app.forms;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.Gson;

//TODO refactor
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
        this.formConfig = gson.fromJson(formDataString, FormConfig.class);

        Toast.makeText(context, formConfig.title, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < formConfig.elements.length; i++) {
            addFormElement(layout, formConfig.elements[i]);
        }

        final Button submitButton = new Button(context);
        submitButton.setText("submit");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormData data = processData();
                submitData(data);
            }
        });
        layout.addView(submitButton);
    }

    private FormData processData() {
        FormData data = new FormData();
        data.title = formConfig.title;

        for (int i = 0; i < formConfig.elements.length; i++) {
            FormElement element = formConfig.elements[i];
            //TODO implement other types
            if (element.type.equals("text")) {
                EditText t = (EditText) layout.findViewById(element.id.hashCode());
                data.elements[i] = new FormElementData();
                data.elements[i].id = element.id;
                data.elements[i].value = t.getText().toString();
            } else if (element.type.equals("numeric")) {
                EditText t = (EditText) layout.findViewById(element.id.hashCode());
                data.elements[i] = new FormElementData();
                data.elements[i].id = element.id;
                data.elements[i].value = t.getText().toString();
            }
        }

        return data;
    }

    //TODO use json and store offline
    private void submitData(FormData data) {
        String jsonData = gson.toJson(data);
        Toast.makeText(context, jsonData, Toast.LENGTH_SHORT).show();
        clearForm();
    }

    private void clearForm() {
        for (int i = 0; i < formConfig.elements.length; i++) {
            FormElement element = formConfig.elements[i];
            //TODO implement other types
            if (element.type.equals("text")) {
                EditText t = (EditText) layout.findViewById(element.id.hashCode());
                t.setText("");
            } else if (element.type.equals("numeric")) {
                EditText t = (EditText) layout.findViewById(element.id.hashCode());
                t.setText("");
            }
        }
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
        private String title;
        private FormElementData[] elements;
    }

    private class FormElementData {
        private String id;
        private String value;
    }
}
