package com.choochootrain.offlineform.app.forms;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FormBuilder {
    private Context context;

    public FormBuilder(Context context) {
        this.context = context;
    }

    public void populateForm(LinearLayout layout, String formData) {
        TextView test = new TextView(context);
        test.setId(123);
        test.setText("dynamically generated textview");

        Button test2 = new Button(context);
        test2.setId(124);
        test2.setText("dynamically generated button");

        layout.addView(test);
        layout.addView(test2);
    }
}
