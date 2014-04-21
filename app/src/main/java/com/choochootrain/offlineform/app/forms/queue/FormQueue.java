package com.choochootrain.offlineform.app.forms.queue;

import android.content.Context;
import android.util.Log;

import com.choochootrain.offlineform.app.forms.data.FormData;
import com.choochootrain.offlineform.app.network.Http;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FormQueue {
    private static final String TAG = "FormQueue";
    private static final String QUEUE_FILE = "formdata.json";

    private Context context;
    private String queueLocation;
    private Gson gson;
    private QueueData queue;

    public FormQueue(Context context) {
        this(context, QUEUE_FILE);
    }

    public FormQueue(Context context, String location) {
        this.context = context;
        this.queueLocation = location;
        this.gson = new Gson();
        this.read();
    }

    private void write() {
        String data = gson.toJson(queue, QueueData.class);

        try {
            FileOutputStream fos = context.openFileOutput(queueLocation, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "queue file not found");
        } catch (IOException e) {
            Log.e(TAG, "error writing to file");
        }
    }

    private void read() {
        String contents = "";

        try {
            FileInputStream fis = context.openFileInput(queueLocation);
            StringBuffer stringBuffer = new StringBuffer("");
            byte[] buffer = new byte[1024];

            while (fis.read(buffer) != -1) {
                stringBuffer.append(new String(buffer));
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "queue file not found");
        } catch (IOException e) {
            Log.e(TAG, "error reading from file");
        }

        if (contents.length() == 0)
            queue = new QueueData();
        else
            queue = gson.fromJson(contents, QueueData.class);
    }

    public void add(FormData data) {
        FormData[] oldSubmissions = queue.submissions;
        queue.submissions = new FormData[oldSubmissions.length + 1];
        for (int i = 0; i < oldSubmissions.length; i++) {
            queue.submissions[i] = oldSubmissions[i];
        }

        queue.submissions[queue.submissions.length - 1] = data;

        write();
    }

    public boolean flush() {
        if (queue.submissions.length == 0)
            return false;

        for (int i = 0; i < queue.submissions.length; i++) {
            Http.post(context, queue.submissions[i]);
        }

        queue.submissions = new FormData[0];
        write();
        return true;
    }
}
