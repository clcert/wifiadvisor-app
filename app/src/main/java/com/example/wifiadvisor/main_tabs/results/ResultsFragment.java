package com.example.wifiadvisor.main_tabs.results;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.adapter_recycler_view.ResultBasicMeasurementAdapterRecyclerView;
import com.example.wifiadvisor.adapter_recycler_view.ResultDevicesAdapterRecyclerView;
import com.example.wifiadvisor.adapter_recycler_view.ResultNdtOoniAdapterRecyclerView;
import com.example.wifiadvisor.adapter_recycler_view.ResultWebOoniAdapterRecyclerView;
import com.example.wifiadvisor.enums.TestsNamesEnum;
import com.example.wifiadvisor.moshi.models.Results;

import java.io.IOException;

import okhttp3.Request;

public class ResultsFragment extends Fragment implements ResultsHistory.GetResultsListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    Results results;
    private int index;
    private TestsNamesEnum testsNames;
    private boolean error = false;

    public static ResultsFragment newInstance(int index) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ViewModel pageViewModel;

    private ResultsHistory resultsHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResultsHistory resultsHistory = new ResultsHistory(getContext());
        resultsHistory.setResultsListener(this);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (index) {
                case 0:
                    testsNames = TestsNamesEnum.basic_tests;
                    break;
                case 1:
                    testsNames = TestsNamesEnum.ndt_tests_ooni;
                    break;
                case 2:
                    testsNames = TestsNamesEnum.devices_tests;
                    break;
                case 3:
                    testsNames = TestsNamesEnum.web_tests_ooni;
                    break;
            }
            resultsHistory.loadData(testsNames);
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_results, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.GONE);
        if (error) {
            errorView(root);
            return root;
        }
        showData(root);
        return root;
    }


    public void showData(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        if (results != null) {
            if (root.findViewById(R.id.progressBar2) != null) {
                ProgressBar p = root.findViewById(R.id.progressBar2);
                requireActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        p.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }
            RecyclerView.Adapter adapter = null;
            switch (index) {
                case 0:
                    if(results.getBasicMesuarement() != null){
                        adapter = new ResultBasicMeasurementAdapterRecyclerView(getContext(), results.getBasicMesuarement());
                    }
                    break;
                case 1:
                    if (results.getNdtTestsOoni() != null){
                        adapter = new ResultNdtOoniAdapterRecyclerView(getContext(), results.getNdtTestsOoni());
                    }
                    break;
                case 2:
                    if (results.getDevicesTest() != null)
                        adapter = new ResultDevicesAdapterRecyclerView(getContext(), results.getDevicesTest());
                    break;

                 case 3:
                     if (results.getWebTestsOoni() != null)
                        adapter = new ResultWebOoniAdapterRecyclerView(getContext(), results.getWebTestsOoni());
                     break;
            }
            if (adapter != null) {
                RecyclerView.Adapter finalAdapter = adapter;
                requireActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        recyclerView.setAdapter(finalAdapter);
                    }
                });
            }
        }
    }

    public void errorView(View view) {
        if (view != null) {
            if (view.findViewById(R.id.progressBar2) != null && view.findViewById(R.id.section_label) != null) {
                ProgressBar p = view.findViewById(R.id.progressBar2);
                TextView textView = view.findViewById(R.id.section_label);
                requireActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        p.setVisibility(View.GONE);
                        textView.setText(R.string.error_server);
                    }
                });
            }
        } else {
            error = true;
        }
    }

    @Override
    public void onFailure(Request request, IOException e) {
        errorView(getView());
        System.out.println(e);
        error = true;
    }

    @Override
    public void onResponse(Results response) {
        results = response;
        if (getView() != null) {
            showData(getView());
        }
        error = false;
    }

}