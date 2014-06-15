package com.choochootrain.offlineform.app.forms.data;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class FormConfig {
    private static final String TAG = "FormConfig";
    private static final String FORM_JSON = "form.json";

    public String title;
    public FormElement[] elements;

    public static void init(Context context) {

    }

    public static String load(Context context) {
        return "{title: 'This is a test form'," +
                "elements: [" +
                    "{name: 'Name',type: 'text',id: 'name'}," +
                    "{name: 'Email address',type: 'text',id: 'email'}," +
                    "{name: 'Age',type: 'numeric',id: 'age'}" +
                "]}";
    }

    public static void save(Context context, String data) {

    }

    private String readRawResource(Context context, int id) {
        InputStream is = context.getResources().openRawResource(id);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.e(TAG, "error reading json file");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "could not close inputstream");
            }
        }

        return writer.toString();
    }

    private String readJson(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FORM_JSON);
            return readStream(fis);
        } catch (FileNotFoundException e) {
            return "";
        }
    }

    private boolean writeJson(Context context, String s) {
        try {
            FileOutputStream fos = context.openFileOutput(FORM_JSON, Context.MODE_PRIVATE);
            fos.write(s.getBytes());
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private String readStream(InputStream is) {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.e(TAG, "error reading json file");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "could not close inputstream");
            }
        }

        return writer.toString();
    }
}
