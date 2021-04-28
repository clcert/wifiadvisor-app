package com.example.wifiadvisor.adapter_recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.moshi.models.testsTimestamp.TestTimestampI;
import com.example.wifiadvisor.moshi.models.testsTimestamp.WebTestOoniTimestamp;

import java.util.ArrayList;

public class ResultWebOoniAdapterRecyclerView extends RecyclerView.Adapter<ResultWebOoniAdapterRecyclerView.ResultViewHolder> {

    private ArrayList<WebTestOoniTimestamp> dataset;
    private Context context;

    public ResultWebOoniAdapterRecyclerView(Context context, ArrayList<WebTestOoniTimestamp> dataset) {
        this.dataset = dataset;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(context).inflate(R.layout.card_ooni_web_results, parent, false);
        return new ResultViewHolder(adapterLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        WebTestOoniTimestamp data = this.dataset.get(position);
        if (data !=null){
            holder.date.setText(data.getTimestamp().toString());
            holder.url.setText(data.getTest().getUrl());
            holder.blocking_reason.setText(data.check().second);
            Boolean not_blocking = data.check().first;
            if (not_blocking == null) {
                holder.title.setText(R.string.measurement_not_performed);
            } else if (not_blocking){
                holder.title.setText(R.string.site_not_blocked);
            } else {
                holder.title.setText(R.string.site_blocking);
            }
            holder.blocking_reason.setCompoundDrawablesWithIntrinsicBounds(TestTimestampI.getDrawable(not_blocking), 0, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView title;
        TextView url;
        TextView blocking_reason;
        ResultWebOoniAdapterRecyclerView resultAdapterRecyclerView;

        public ResultViewHolder(@NonNull View itemView, ResultWebOoniAdapterRecyclerView resultAdapterRecyclerView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.date);
            this.title = itemView.findViewById(R.id.title_card);
            this.url = itemView.findViewById(R.id.url);
            this.blocking_reason = itemView.findViewById(R.id.blocking_reason);
            this.resultAdapterRecyclerView = resultAdapterRecyclerView;
        }
    }

}