package com.example.wifiadvisor.moshi.models.testsTimestamp;

import com.example.wifiadvisor.R;

public interface TestTimestampI {
    public static int getDrawable(Boolean value){
        int drawable;
        if (value == null){
            drawable= R.drawable.ic_baseline_warning_24;
        }else if (value){
            drawable=R.drawable.ic_baseline_check_circle_24;
        }else {
            drawable=R.drawable.ic_baseline_cancel_24;
        }
        return drawable;
    }
}
