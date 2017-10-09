package com.nellions.nellionscanvas.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nellions.nellionscanvas.R;
import com.nellions.nellionscanvas.model.AppModel;

import java.util.List;

/**
 * Created by Chris Muiruri on 2/12/2016.
 */
public class SurveyItemAdapter extends RecyclerView.Adapter<SurveyItemAdapter.MyViewHolder> {
    public List<AppModel> appModelList;

    public SurveyItemAdapter(List<AppModel> appModelList) {
        this.appModelList = appModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView summaryItemCategory = holder.summaryItemCategory;
        TextView summaryItemName = holder.summaryItemName;
        TextView summaryItemTotal = holder.summaryItemTotal;
        TextView summaryItemVolume = holder.summaryItemVolume;
        TextView summaryItemQty = holder.summaryItemQty;
        ImageView syncStatus = holder.syncStatus;
        if (appModelList.get(position).getS_sync() != null) {
            if (appModelList.get(position).getS_sync().equals("1")) {
                syncStatus.setImageResource(android.R.drawable.checkbox_on_background);
            }
        }
        summaryItemCategory.setText(appModelList.get(position).getS_categoryName());
        summaryItemName.setText(appModelList.get(position).getS_itemName());
        summaryItemTotal.setText(appModelList.get(position).getS_itemTotal());
        summaryItemQty.setText(appModelList.get(position).getS_itemQuantity());
        summaryItemVolume.setText(appModelList.get(position).getS_itemVolume());
    }

    @Override
    public int getItemCount() {
        return appModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView summaryItemCategory;
        TextView summaryItemName;
        TextView summaryItemTotal;
        TextView summaryItemVolume;
        TextView summaryItemQty;
        ImageView syncStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.summaryItemCategory = (TextView) itemView.findViewById(R.id.summary_item_category);
            this.summaryItemName = (TextView) itemView.findViewById(R.id.summary_item_name);
            this.summaryItemTotal = (TextView) itemView.findViewById(R.id.summary_item_total);
            this.summaryItemVolume = (TextView) itemView.findViewById(R.id.summary_item_volume);
            this.summaryItemQty = (TextView) itemView.findViewById(R.id.summary_item_number);
            this.syncStatus = (ImageView) itemView.findViewById(R.id.sync_status);
        }
    }
}
