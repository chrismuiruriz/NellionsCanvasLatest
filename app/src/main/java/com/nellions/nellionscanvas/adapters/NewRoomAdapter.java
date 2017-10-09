package com.nellions.nellionscanvas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nellions.nellionscanvas.R;
import com.nellions.nellionscanvas.model.AppModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris Muiruri on 3/10/2016.
 */
public class NewRoomAdapter extends BaseAdapter implements Filterable {

    public List<AppModel> appModelList;
    List<AppModel> appModelList2;
    Filter clientFilter;
    private LayoutInflater inflater;
    Context context;

    public NewRoomAdapter(Context context, List<AppModel> appModelList) {
        inflater = LayoutInflater.from(context);
        this.appModelList = appModelList;
        this.appModelList2 = appModelList;
        this.context = context;
    }

    public void resetData() {
        appModelList2 = appModelList;
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
            view = inflater.inflate(R.layout.moves_category_layout, parent, false);
            myViewHolder = new MyViewHolder();
            myViewHolder.categoryName = (TextView) view.findViewById(R.id.categoryName);
            view.setTag(myViewHolder);
        } else {
            view = contentView;
            myViewHolder = (MyViewHolder) view.getTag();
        }

        final AppModel appModel = appModelList2.get(position);
        myViewHolder.categoryName.setText(appModel.getC_name().toUpperCase());
        return view;
    }

    @Override
    public Filter getFilter() {
        if (clientFilter == null) {
            clientFilter = new ClientFilter();
        }
        return clientFilter;
    }

    public static class MyViewHolder {
        TextView categoryName;
    }

    public class ClientFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                results.values = appModelList;
                results.count = appModelList.size();
            } else {
                List<AppModel> appModelList1 = new ArrayList<AppModel>();

                for (AppModel appModel : appModelList2) {
                    if (appModel.getC_name().toUpperCase().contains(charSequence.toString().toUpperCase())) {
                        appModelList1.add(appModel);
                        notifyDataSetChanged();
                    }
                }
                results.values = appModelList1;
                results.count = appModelList1.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
            if (filterResults.count == 0) {
                notifyDataSetChanged();
            } else {
                appModelList2 = (List<AppModel>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }
}
