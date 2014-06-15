package com.choochootrain.offlineform.app.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.choochootrain.offlineform.app.forms.data.FormData;
import com.choochootrain.offlineform.app.forms.data.FormElementData;
import com.choochootrain.offlineform.app.forms.queue.FormQueue;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class HttpPostTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "HttpPostTask";
    private Context context;
    private FormData formData;
    private FormQueue formQueue;

    public HttpPostTask(Context context, FormData formData, FormQueue formQueue) {
        this.context = context;
        this.formData = formData;
        this.formQueue = formQueue;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(formData.target);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(formData.elements.length);
        for (int i = 0; i < formData.elements.length; i++) {
            FormElementData element = formData.elements[i];
            nameValuePairs.add(i, new BasicNameValuePair(element.id, element.value));
        }

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            Log.d(TAG, "Recieved response: " + response.toString());
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            formQueue.add(formData);
            return false;
        }
    }
}
