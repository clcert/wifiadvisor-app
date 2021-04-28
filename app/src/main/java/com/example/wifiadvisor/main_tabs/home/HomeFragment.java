package com.example.wifiadvisor.main_tabs.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.wifiadvisor.activities.BasicMeasurementActivity;
import com.example.wifiadvisor.activities.BlockingTestActivity;
import com.example.wifiadvisor.activities.DevicesActivity;
import com.example.wifiadvisor.R;
import com.example.wifiadvisor.activities.SpeedMeasurementActivity;

public class HomeFragment extends Fragment {

    Context context;
    CardView card_basic_measurement;
    CardView card_speed_measurement;
    CardView card_others_devices;
    CardView card_blocking_test;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context=getContext();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        card_basic_measurement = (CardView) view.findViewById(R.id.card_basic_measurement);
        card_speed_measurement = (CardView) view.findViewById(R.id.card_speed_measurement);
        card_others_devices = (CardView) view.findViewById(R.id.card_others_devices);
        card_blocking_test = (CardView) view.findViewById(R.id.card_blocking_test);
        card_basic_measurement.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this.getActivity(), BasicMeasurementActivity.class);
                    startActivity(intent);
                }
        );

        card_speed_measurement.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this.getActivity(), SpeedMeasurementActivity.class);
                    startActivity(intent);
                }
        );

        card_others_devices.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this.getActivity(), DevicesActivity.class);
                    startActivity(intent);
                }
        );

        card_blocking_test.setOnClickListener(
                v -> {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireActivity());
                    final EditText input = new EditText(context);
                    input.setSingleLine();
                    input.setHint(R.string.clcert_page);
                    alertDialog.setTitle(R.string.title_dialog);
                    alertDialog.setView(input);
                    alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    alertDialog.setPositiveButton(R.string.run_test, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getActivity(), BlockingTestActivity.class);
                            intent.putExtra("url", input.getText().toString());
                            startActivity(intent);
                        }
                    });

                    alertDialog.show();
                });
        return view;
    }
}