/*
Code base in MeasurementKit Android app https://github.com/measurement-kit/android-example
 */
package com.example.wifiadvisor.usertests;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wifiadvisor.moshi.models.TestBase;

import org.json.JSONException;
import org.json.JSONObject;

import oonimkall.Task;

import static java.lang.String.format;

public class OoniTest implements Runnable {
    Activity activity;
    String TAG = "OONI";
    String settings;
    String result;
    TextView logtext;
    TestBase test_base;
    OoniTest(String settings, TextView logText, Activity activity) {
        this.settings = settings;
        this.logtext = logText;
        this.activity = activity;
        this.test_base = new TestBase(activity);
    }

    @Override
    public void run() {
        Task task = null;
        JSONObject event;
        try {
            task = oonimkall.Oonimkall.startTask(settings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert task != null;
        while (!task.isDone()) {
            final String serialization = task.waitForNextEvent();
            if (serialization == null) {
                Log.w(TAG, "Cannot serialize event");
                break;
            }
            try {
                event = new JSONObject(serialization);
            } catch (JSONException exc) {
                Log.w(TAG, "Cannot marshal event: " + exc.toString());
                break;
            }
            String key = event.optString("key");
            if (key.compareTo("status.progress") == 0) {
                JSONObject value = event.optJSONObject("value");
                final double percentage = value.optDouble("percentage", 0.0);
                final String message = value.optString("message");
                activity.runOnUiThread(() -> logtext.setText(String.format("Corriendo test en ooni %s%% ", format("%.2f", percentage * 100.0))));
                Log.w(TAG, percentage * 100.0 + "% " + message + "\n");
            } else if (key.compareTo("measurement") == 0) {
                System.out.println(event.optJSONObject("value"));
                result = event.optJSONObject("value").optString("json_str");
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        logtext.setText("Recibiendo resultados");
                    }
                });
            } else {
                Log.i(TAG, "Unhandled event: " + serialization);
            }
        }
    }

    public String getResult() {
        return result;
    }
}
