package com.example.wifiadvisor.adapter_recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.moshi.models.DevicesTest;
import com.example.wifiadvisor.moshi.models.testsTimestamp.DevicesTimestamp;

import java.util.ArrayList;

public class ResultDevicesAdapterRecyclerView extends RecyclerView.Adapter<ResultDevicesAdapterRecyclerView.ResultViewHolder> {

    private ArrayList<DevicesTimestamp> dataset;
    private Context context;

    public ResultDevicesAdapterRecyclerView(Context context, ArrayList<DevicesTimestamp> dataset) {
        this.dataset = dataset;
        this.context = context;
    }


    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(context).inflate(R.layout.card_devices_results, parent, false);
        return new ResultDevicesAdapterRecyclerView.ResultViewHolder(adapterLayout, this);    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        DevicesTimestamp data = this.dataset.get(position);
        if (data !=null){
            holder.date.setText(data.getTimestamp().toString());
            holder.title.setText("Dispositivos encontrados");
            String manuf;
            for (DevicesTest device : data.getTest()) {
                TextView textView = new TextView(context);
                manuf = device.getManuf() != null? device.getManuf():"";
                System.out.println(device.getPrivateIp());
                System.out.println(device.getMac());
                System.out.println(manuf);
                textView.setText(device.getPrivateIp()+" "+ manuf+ " ("+ device.getMac()+")");
                int drawableDevice = device.isRouter()? R.drawable.ic_baseline_router_24_black : R.drawable.ic_home_black_24dp;
                textView.setCompoundDrawablesWithIntrinsicBounds(drawableDevice, 0, 0 ,0 );
                holder.linearLayout.addView(textView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        ResultDevicesAdapterRecyclerView resultAdapterRecyclerView;
        LinearLayout linearLayout;
        TextView date;
        TextView title;
        public ResultViewHolder(@NonNull View itemView, ResultDevicesAdapterRecyclerView resultAdapterRecyclerView) {
            super(itemView);
            this.linearLayout = itemView.findViewById(R.id.devices_data);
            this.date = itemView.findViewById(R.id.date);
            this.title = itemView.findViewById(R.id.title_card);
            this.resultAdapterRecyclerView = resultAdapterRecyclerView;
        }
    }

}