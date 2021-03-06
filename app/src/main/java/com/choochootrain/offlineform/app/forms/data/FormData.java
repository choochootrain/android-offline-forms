package com.choochootrain.offlineform.app.forms.data;

public class FormData {
    public String title;
    public long timestamp;
    public FormElementData[] elements;
    public String target;

    public FormData() {
        this(0);
    }

    public FormData(int numElements) {
        this.elements = new FormElementData[numElements];
    }
}
