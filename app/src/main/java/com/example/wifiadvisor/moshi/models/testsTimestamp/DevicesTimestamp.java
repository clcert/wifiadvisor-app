package com.example.wifiadvisor.moshi.models.testsTimestamp;

import android.util.Pair;

import com.example.wifiadvisor.moshi.models.DevicesTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DevicesTimestamp implements TestTimestampI{
    List<DevicesTest> test;
    Date timestamp;

    @Override
    public String toString() {
        return "DevicesTimestamp{" +
                "tests=" + test +
                ", timestamp=" + timestamp +
                '}';
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<DevicesTest> getTest() {
        return test;
    }
}
