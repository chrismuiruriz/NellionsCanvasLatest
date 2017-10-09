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
 * Created by Chris Muiruri on 2/18/2016.
 */
public class PhotoViewAdapter extends RecyclerView.Adapter<PhotoViewAdapter.MyViewHolder> {
    public List<AppModel> appModelList;

    public PhotoViewAdapter(List<AppModel> appModelList) {
        this.appModelList = appModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageView imageView = holder.imageView;
        TextView textView = holder.textView;

        imageView.setImageBitmap(appModelList.get(position).getPhotoImage());
        textView.setText(appModelList.get(position).getPhotoName());
    }

    @Override
    public int getItemCount() {
        return appModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.photo);
            this.textView = (TextView) itemView.findViewById(R.id.photoText);
        }
    }
}
