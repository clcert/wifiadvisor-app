package com.example.wifiadvisor.main_tabs.results;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.wifiadvisor.R;
import com.google.android.material.tabs.TabLayout;

public class ResultsMainFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_result, container, false);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        ViewPager viewPager = root.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = root.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_baseline_wifi_24);
        tabs.getTabAt(1).setIcon(R.drawable.ic_baseline_speed_24);
        tabs.getTabAt(2).setIcon(R.drawable.ic_baseline_router_24);
        tabs.getTabAt(3).setIcon(R.drawable.ic_baseline_app_blocking_24);
        return root;
    }
}