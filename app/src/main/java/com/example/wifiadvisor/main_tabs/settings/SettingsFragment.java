/**
 * iterate radio button https://stackoverflow.com/questions/20931986/how-to-iterate-in-my-radiobuttons-to-find-a-string-on-javaandroid
 */
package com.example.wifiadvisor.main_tabs.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wifiadvisor.R;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        EditText mEditText = (EditText)root.findViewById(R.id.username);
        Button button = (Button) root.findViewById(R.id.save);
        RadioGroup radioGroup = (RadioGroup) root.findViewById(R.id.places);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String place=sharedPref.getString(getString(R.string.place_measurement), "");
        ((EditText) root.findViewById(R.id.username)).setText(sharedPref.getString(getString(R.string.username), ""));
        if (!place.equals("")){
            for (int i=0;i<radioGroup.getChildCount();i++) {
                View child = radioGroup.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton selectedAnswer =(RadioButton)child;
                    if(selectedAnswer.getText().equals(place)) {
                        ((RadioButton) child).setChecked(true);
                        break;
                    }
                }
            }
        }
        button.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            boolean change = false;
            if(radioGroup.getCheckedRadioButtonId()!=-1){
                String radiovalue = ((RadioButton)root.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                editor.putString(getString(R.string.place_measurement), radiovalue);
                editor.putInt("id_place", radioGroup.getCheckedRadioButtonId());
                change = true;
            }
            String username=mEditText.getText().toString().trim();
            if (!username.equals("")){
                editor.putString(getString(R.string.username), username);
                change = true;
            }
            editor.apply();
            if (change) {

                Snackbar.make(root, getString(R.string.save_data,sharedPref.getString(getString(R.string.username), ""), sharedPref.getString(getString(R.string.place_measurement), "")), Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
        return root;
    }
}