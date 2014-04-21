package com.choochootrain.offlineform.app.forms;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.text.format.Time;

import com.choochootrain.offlineform.app.forms.data.FormConfig;
import com.choochootrain.offlineform.app.forms.data.FormData;
import com.choochootrain.offlineform.app.forms.data.FormElement;
import com.choochootrain.offlineform.app.forms.data.FormElementData;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO refactor
public class FormBuilder {
    private static final String TAG = "FormBuilder";
    private static final String CACHE_FILE = "formdata.json";
    private static final String DEBUG_URL = "http://127.0.0.1:5000/form";

    private Context context;
    private Gson gson;
    private ConnectivityManager connectivityManager;
    private FormConfig formConfig;
    private LinearLayout layout;
    private int cacheSize;

    public FormBuilder(Context context) {
        this.context = context;
        this.gson = new Gson();
        this.connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void populateForm(final LinearLayout layout, String formDataString) {
        this.layout = layout;
        this.formConfig = gson.fromJson(formDataString, FormConfig.class);

        Toast.makeText(context, formConfig.title, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < formConfig.elements.length; i++) {
            formConfig.elements[i].inflate(context, layout);
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
        data.timestamp = new Time().toMillis(false);

        for (int i = 0; i < formConfig.elements.length; i++) {
            data.elements[i] = formConfig.elements[i].getData(layout);
        }

        return data;
    }

    //TODO use json and store offline
    private void submitData(FormData data) {
        data.target = DEBUG_URL;
        String jsonData = gson.toJson(data);
        Toast.makeText(context, jsonData, Toast.LENGTH_SHORT).show();
        clearForm();

        if (isOnline()) {
            flushCache();
            sendPostRequest(data);
        } else
            cachePostRequest(data);
    }

    private boolean isOnline() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo.isConnected();
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void flushCache() {
        String contents = readCacheFile();

        if (contents.length() != 0) {
            CacheData cacheData = gson.fromJson(contents, CacheData.class);

            for (int i = 0; i < cacheData.submissions.length; i++) {
                FormData submission = cacheData.submissions[i];
                sendPostRequest(submission);
            }
        }

        writeCacheFile("");
    }

    private void sendPostRequest(FormData data) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(data.target);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(data.elements.length);
        for (int i = 0; i < data.elements.length; i++) {
            FormElementData element = data.elements[i];
            nameValuePairs.add(i, new BasicNameValuePair(element.id, element.value));
        }

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            Toast.makeText(context, "Recieved response: " + response.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void cachePostRequest(FormData data) {
        //TODO refactor this mess
        String contents = readCacheFile();
        CacheData cache;

        if (contents.length() == 0)
            cache = new CacheData();
        else
            cache = gson.fromJson(contents, CacheData.class);

        FormData[] oldSubmissions = cache.submissions;
        cache.submissions = new FormData[oldSubmissions.length + 1];
        for (int i = 0; i < oldSubmissions.length; i++) {
            cache.submissions[i] = oldSubmissions[i];
        }

        cache.submissions[cache.submissions.length - 1] = data;
        String cacheData = gson.toJson(cache);

        cacheSize = cache.submissions.length;

        writeCacheFile(cacheData);
    }

    private void writeCacheFile(String data) {
        try {
            FileOutputStream fos = context.openFileOutput(CACHE_FILE, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "cache file not found");
        } catch (IOException e) {
            Log.e(TAG, "error writing to file");
        }
    }

    private String readCacheFile() {
        String contents = "";

        try {
            FileInputStream fis = context.openFileInput(CACHE_FILE);
            StringBuffer stringBuffer = new StringBuffer("");
            byte[] buffer = new byte[1024];

            while (fis.read(buffer) != -1) {
                stringBuffer.append(new String(buffer));
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "cache file not found");
        } catch (IOException e) {
            Log.e(TAG, "error reading from file");
        }

        return contents;
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
            } else if (element.type.equals("select")) {
                Spinner t = (Spinner) layout.findViewById(element.id.hashCode());
                t.setSelected(false);
            }
        }
    }
}
