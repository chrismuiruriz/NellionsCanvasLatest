package com.nellions.nellionscanvas.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nellions.nellionscanvas.model.AppModel;

import java.util.List;

/**
 * Created by Chris Muiruri on 2/17/2016.
 */
public class RoomSpinnerAdapter extends ArrayAdapter<AppModel> {

    public Context context;
    List<AppModel> appModelList;

    public RoomSpinnerAdapter(Context context, int textViewResourceId, List<AppModel> appModelList) {
        super(context, textViewResourceId, appModelList);
        this.context = context;
        this.appModelList = appModelList;
    }

    @Override
    public int getCount() {
        return appModelList.size();
    }

    @Override
    public AppModel getItem(int position) {
        return appModelList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(appModelList.get(position).getRoomName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setPadding(15, 15, 15, 15);
        label.setText(appModelList.get(position).getRoomName());
        return label;
    }
}
