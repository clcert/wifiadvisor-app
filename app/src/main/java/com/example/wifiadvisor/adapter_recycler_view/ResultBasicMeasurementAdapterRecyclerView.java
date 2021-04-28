package com.example.wifiadvisor.adapter_recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.moshi.models.TestBase;
import com.example.wifiadvisor.moshi.models.testsTimestamp.DnsTimestamp;
import com.example.wifiadvisor.moshi.models.testsTimestamp.ProtocolsTimestamp;
import com.example.wifiadvisor.moshi.models.testsTimestamp.TestTimestampI;

import java.util.ArrayList;

public class ResultBasicMeasurementAdapterRecyclerView extends RecyclerView.Adapter<ResultBasicMeasurementAdapterRecyclerView.ResultViewHolder> {

    private ArrayList<TestTimestampI> dataset;
    private Context context;
    public ResultBasicMeasurementAdapterRecyclerView(Context context, ArrayList<TestTimestampI> dataset ) {
        this.context = context;
        this.dataset =dataset;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(context).inflate(R.layout.card_basic_result, parent, false);
        return new ResultViewHolder(adapterLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        TestTimestampI data = this.dataset.get(position);
        if (data instanceof DnsTimestamp){
            DnsTimestamp dnsTimestamp = (DnsTimestamp) data;
            holder.date.setText(dnsTimestamp.getTimestamp().toString());
            holder.androidDns.setText(dnsTimestamp.checkAndroidDns(context).second);
            holder.dnssec.setText(dnsTimestamp.checkDnssec(context).second);
            holder.sourcePort.setText(dnsTimestamp.checkSourcePort(context).second);
            holder.idTransaction.setText(dnsTimestamp.checkTransactionId(context).second);
            holder.androidDns.setCompoundDrawablesWithIntrinsicBounds(TestTimestampI.getDrawable(dnsTimestamp.checkAndroidDns(context).first), 0, 0, 0);
            holder.dnssec.setCompoundDrawablesWithIntrinsicBounds(TestTimestampI.getDrawable(dnsTimestamp.checkDnssec(context).first), 0, 0, 0);
            holder.sourcePort.setCompoundDrawablesWithIntrinsicBounds(TestTimestampI.getDrawable(dnsTimestamp.checkSourcePort(context).first), 0, 0, 0);
            holder.idTransaction.setCompoundDrawablesWithIntrinsicBounds(TestTimestampI.getDrawable(dnsTimestamp.checkTransactionId(context).first), 0, 0, 0);
            holder.title.setText(R.string.dns_tests);
            holder.protocol.setVisibility(View.GONE);
        }
        if(data instanceof ProtocolsTimestamp){
            ProtocolsTimestamp protocolsTimestamp = (ProtocolsTimestamp) data;
            holder.date.setText(protocolsTimestamp.getTimestamp().toString());
            holder.protocol.setText(protocolsTimestamp.check().second);
            holder.protocol.setCompoundDrawablesWithIntrinsicBounds(TestTimestampI.getDrawable(protocolsTimestamp.check().first), 0, 0 ,0 );
            holder.title.setText(R.string.protocol_tests);
            holder.androidDns.setVisibility(View.GONE);
            holder.dnssec.setVisibility(View.GONE);
            holder.sourcePort.setVisibility(View.GONE);
            holder.idTransaction.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        TextView protocol;
        TextView androidDns;
        TextView dnssec;
        TextView sourcePort;
        TextView idTransaction;

        ResultBasicMeasurementAdapterRecyclerView resultAdapterRecyclerView;

        public ResultViewHolder(@NonNull View itemView, ResultBasicMeasurementAdapterRecyclerView resultAdapterRecyclerView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title_card);
            this.date = itemView.findViewById(R.id.date);
            this.protocol = itemView.findViewById(R.id.protocol);
            this.androidDns = itemView.findViewById(R.id.android_dns);
            this.dnssec = itemView.findViewById(R.id.dnssec);
            this.sourcePort = itemView.findViewById(R.id.source_port);
            this.idTransaction = itemView.findViewById(R.id.id_transaction);
            this.resultAdapterRecyclerView = resultAdapterRecyclerView;
        }
    }

}