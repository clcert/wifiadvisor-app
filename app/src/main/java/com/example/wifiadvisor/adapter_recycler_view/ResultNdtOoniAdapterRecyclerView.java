package com.example.wifiadvisor.adapter_recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.moshi.models.testsTimestamp.NdtTestOoniTimestamp;
import com.example.wifiadvisor.moshi.models.testsTimestamp.TestTimestampI;

import java.util.ArrayList;

public class ResultNdtOoniAdapterRecyclerView extends RecyclerView.Adapter<ResultNdtOoniAdapterRecyclerView.ResultViewHolder> {

    private ArrayList<NdtTestOoniTimestamp> dataset;
    private Context context;

    public ResultNdtOoniAdapterRecyclerView(Context context, ArrayList<NdtTestOoniTimestamp> dataset) {
        this.dataset = dataset;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(context).inflate(R.layout.card_ndt_ooni_results, parent, false);
        return new ResultNdtOoniAdapterRecyclerView.ResultViewHolder(adapterLayout, this);    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        NdtTestOoniTimestamp data = this.dataset.get(position);
        if (data !=null){
            holder.date.setText(data.getTimestamp().toString());
            holder.title.setText(R.string.title_ndt_result);
            holder.download.setText(data.checkDownload(context).second);
            holder.upload.setText(data.checkUpload(context).second);
            holder.download_rtt.setText(data.checkRtt(context).second);
            holder.download.setCompoundDrawablesWithIntrinsicBounds(TestTimestampI.getDrawable(data.checkDownload(context).first), 0, 0, 0);
            holder.upload.setCompoundDrawablesWithIntrinsicBounds(TestTimestampI.getDrawable(data.checkUpload(context).first), 0, 0, 0);
            holder.download_rtt.setCompoundDrawablesWithIntrinsicBounds(TestTimestampI.getDrawable(data.checkRtt(context).first), 0, 0, 0);

        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        ResultNdtOoniAdapterRecyclerView resultAdapterRecyclerView;
        TextView date;
        TextView title;
        TextView upload;
        TextView download;
        TextView download_rtt;

        public ResultViewHolder(@NonNull View itemView, ResultNdtOoniAdapterRecyclerView resultAdapterRecyclerView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.date);
            this.title = itemView.findViewById(R.id.title_card);
            this.upload = itemView.findViewById(R.id.upload);
            this.download = itemView.findViewById(R.id.download);
            this.download_rtt = itemView.findViewById(R.id.download_rtt);
            this.resultAdapterRecyclerView = resultAdapterRecyclerView;
        }
    }

}