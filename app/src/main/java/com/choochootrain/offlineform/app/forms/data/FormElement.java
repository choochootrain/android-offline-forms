package com.choochootrain.offlineform.app.forms.data;

import android.content.Context;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class FormElement {
    public String name;
    public String type;
    public String id;
    public String[] choices;

    public void inflate(Context context, LinearLayout layout) {
        //TODO implement other types
        //TODO insert elements in specified order
        if (this.type.equals("text")) {
            EditText t = new EditText(context);
            t.setInputType(InputType.TYPE_CLASS_TEXT);
            t.setText(this.name);
            t.setId(this.id.hashCode());
            layout.addView(t);
        } else if (this.type.equals("numeric")) {
            EditText t = new EditText(context);
            t.setInputType(InputType.TYPE_CLASS_NUMBER);
            t.setText(this.name);
            t.setId(this.id.hashCode());
            layout.addView(t);
        } else if (this.type.equals("select")) {
            Spinner t = new Spinner(context);
            t.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, this.choices));
            t.setId(this.id.hashCode());
            layout.addView(t);
        }
    }

    public FormElementData getData(LinearLayout layout) {
        FormElementData data = new FormElementData();
        data.id = this.id;

        //TODO implement other types
        if (this.type.equals("text")) {
            EditText t = (EditText) layout.findViewById(this.id.hashCode());
            data.value = t.getText().toString();
        } else if (this.type.equals("numeric")) {
            EditText t = (EditText) layout.findViewById(this.id.hashCode());
            data.value = t.getText().toString();
        } else if (this.type.equals("select")) {
           Spinner t = (Spinner)  layout.findViewById(this.id.hashCode());
            data.value = this.choices[t.getSelectedItemPosition()];
        }

        return data;
    }

    public void clear(LinearLayout layout) {
        //TODO implement other types
        if (this.type.equals("text")) {
            EditText t = (EditText) layout.findViewById(this.id.hashCode());
            t.setText("");
        } else if (this.type.equals("numeric")) {
            EditText t = (EditText) layout.findViewById(this.id.hashCode());
            t.setText("");
        } else if (this.type.equals("select")) {
            Spinner t = (Spinner) layout.findViewById(this.id.hashCode());
            t.setSelected(false);
        }
    }
}