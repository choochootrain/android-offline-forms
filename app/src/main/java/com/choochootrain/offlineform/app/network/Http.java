package com.choochootrain.offlineform.app.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.choochootrain.offlineform.app.forms.data.FormData;
import com.choochootrain.offlineform.app.forms.data.FormElementData;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Http {
    public static final String TAG = "Http";

    //TODO async task this
    public static boolean post(Context context, FormData data) {
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
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }
}
