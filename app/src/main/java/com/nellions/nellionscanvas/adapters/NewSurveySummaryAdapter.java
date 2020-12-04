package com.nellions.nellionscanvas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nellions.nellionscanvas.R;
import com.nellions.nellionscanvas.model.AppModel;

import java.util.List;

/**
 * Created by Chris Muiruri on 3/10/2016.
 */
public class NewSurveySummaryAdapter extends BaseAdapter {

    public List<AppModel> appModelList;
    List<AppModel> appModelList2;
    Filter clientFilter;
    private final LayoutInflater inflater;
    Context context;

    public NewSurveySummaryAdapter(Context context, List<AppModel> appModelList) {
        inflater = LayoutInflater.from(context);
        this.appModelList = appModelList;
        this.appModelList2 = appModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return appModelList2.size();
    }

    @Override
    public AppModel getItem(int position) {
        return appModelList2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup parent) {
        View view;
        MyViewHolder myViewHolder;

        if (contentView == null) {

            view = inflater.inflate(R.layout.summary_item_layout, parent, false);
            myViewHolder = new MyViewHolder();

            myViewHolder.summaryItemCategory = view.findViewById(R.id.summary_item_category);
            myViewHolder.summaryItemName = view.findViewById(R.id.summary_item_name);
            myViewHolder.summaryItemTotal = view.findViewById(R.id.summary_item_total);
            myViewHolder.summaryItemVolume = view.findViewById(R.id.summary_item_volume);
            myViewHolder.summaryItemQty = view.findViewById(R.id.summary_item_number);
            myViewHolder.syncStatus = view.findViewById(R.id.sync_status);
            view.setTag(myViewHolder);
        } else {
            view = contentView;
            myViewHolder = (MyViewHolder) view.getTag();
        }

        final AppModel appModel = appModelList2.get(position);
        if (appModel.getS_sync() != null) {
            if (appModel.getS_sync().equals("1")) {
                myViewHolder.syncStatus.setImageResource(android.R.drawable.checkbox_on_background);
            }
        }
        myViewHolder.summaryItemCategory.setText(appModel.getS_categoryName());
        myViewHolder.summaryItemName.setText(appModel.getS_itemName());
        myViewHolder.summaryItemTotal.setText(appModel.getS_itemTotal());
        myViewHolder.summaryItemQty.setText(appModel.getS_itemQuantity());
        myViewHolder.summaryItemVolume.setText(appModel.getS_itemVolume());

        return view;
    }

    public static class MyViewHolder {

        TextView summaryItemCategory;
        TextView summaryItemName;
        TextView summaryItemTotal;
        TextView summaryItemVolume;
        TextView summaryItemQty;
        ImageView syncStatus;

    }
}
