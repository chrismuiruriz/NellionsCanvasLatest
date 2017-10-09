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
 * Created by Chris Muiruri on 1/10/2016.
 */
public class MoveSurveyAdapter extends BaseAdapter implements Filterable {

    public List<AppModel> appModelList;
    List<AppModel> appModelList2;
    Filter clientFilter;
    private LayoutInflater inflater;
    Context context;

    public MoveSurveyAdapter(Context context, List<AppModel> appModelList) {
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

    public int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        MyViewHolder myViewHolder;

        if (convertView == null) {
            view = inflater.inflate(R.layout.moves_survey_layout, parent, false);
            myViewHolder = new MyViewHolder();
            myViewHolder.clientName = (TextView) view.findViewById(R.id.clientName);
            myViewHolder.moveDescription = (TextView) view.findViewById(R.id.survey_description);
            myViewHolder.moveDate = (TextView) view.findViewById(R.id.survey_date);
            myViewHolder.moveRep = (TextView) view.findViewById(R.id.move_rep);
            view.setTag(myViewHolder);
        } else {
            view = convertView;
            myViewHolder = (MyViewHolder) view.getTag();
        }

        final AppModel appModel = appModelList2.get(position);
        myViewHolder.clientName.setText(appModel.getM_clientName());
        myViewHolder.moveDescription.setText(appModel.getM_description());
        myViewHolder.moveDate.setText(appModel.getM_surveyDate());
        myViewHolder.moveRep.setText(appModel.getM_moveRep());

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

        TextView clientName;
        TextView moveDescription;
        TextView moveDate;
        TextView moveRep;

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
                    if (appModel.getM_clientName().toUpperCase().contains(charSequence.toString().toUpperCase())) {
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
