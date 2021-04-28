package com.example.wifiadvisor.main_tabs.results;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {


    public SectionsPagerAdapter( FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        return ResultsFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}