package com.example.wifiadvisor.moshi.models.testsTimestamp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Pair;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.moshi.models.Mlab;
import com.example.wifiadvisor.moshi.models.NdtTestOoni;

import java.util.Date;

public class NdtTestOoniTimestamp implements TestTimestampI {
    NdtTestOoni test;
    Date timestamp;
    Mlab mlab;

    @Override
    public String toString() {
        return "NdtTestOoniTimestamp{" +
                "test=" + test +
                ", timestamp=" + timestamp +
                ", timestamp=" + mlab +
                '}';
    }

    public Mlab getMlab() {
        return mlab;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public NdtTestOoni getTest() {
        return test;
    }


    public Pair<Boolean, String> checkDownload(Context context) {
        int measurement = getMlab().getP_download();
        if (measurement >= 50) {
            return new Pair<>(true, context.getString(R.string.download_speed_better, measurement));
        } else {
            return new Pair<>(false, context.getString(R.string.download_speed_worse, 100 - measurement));
        }
    }

    public Pair<Boolean, String> checkUpload(Context context) {
        int measurement = getMlab().getP_upload();
        if (measurement >= 50) {
            return new Pair<>(true, context.getString(R.string.upload_speed_better, measurement));
        } else {
            return new Pair<>(false, context.getString(R.string.upload_speed_worse, 100 - measurement));
        }
    }

    public Pair<Boolean, String> checkRtt(Context context) {
        int measurement = getMlab().getP_rtt();
        if (measurement >= 50) {
            return new Pair<>(true, context.getString(R.string.ping_better,measurement));
        } else {
            return new Pair<>(false, context.getString(R.string.ping_worse, 100 - measurement));
        }
    }

}
