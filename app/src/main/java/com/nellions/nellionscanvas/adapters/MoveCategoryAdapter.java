package com.nellions.nellionscanvas.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nellions.nellionscanvas.R;
import com.nellions.nellionscanvas.model.AppModel;

import java.util.List;

/**
 * Created by Chris Muiruri on 2/4/2016.
 */
public class MoveCategoryAdapter extends RecyclerView.Adapter<MoveCategoryAdapter.MyViewHolder> {
    public List<AppModel> appModelList;

    public MoveCategoryAdapter(List<AppModel> appModelList) {
        this.appModelList = appModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moves_category_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView categoryName = holder.categoryName;
        categoryName.setText(appModelList.get(position).getC_name());
    }

    @Override
    public int getItemCount() {
        return appModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}
