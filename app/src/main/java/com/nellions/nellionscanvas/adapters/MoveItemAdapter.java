package com.nellions.nellionscanvas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.nellions.nellionscanvas.R;
import com.nellions.nellionscanvas.database.DatabaseHelper;
import com.nellions.nellionscanvas.model.AppModel;

import java.util.List;

/**
 * Created by Chris Muiruri on 2/5/2016.
 */
public class MoveItemAdapter extends RecyclerView.Adapter<MoveItemAdapter.MyViewHolder> {
    public List<AppModel> appModelList;
    DatabaseHelper databaseHelper;

    public MoveItemAdapter(List<AppModel> appModelList, Context context) {
        this.appModelList = appModelList;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moves_item_layout, parent, false);
        return new MyViewHolder(view, new MyCustomEditTextListener(), new MyCustomEditTextlistenerTwo());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TextView itemName = holder.itemName;
        EditText itemVolume = holder.itemVolume;
        EditText itemQuantity = holder.itemQuantity;
        TextView itemTotal = holder.itemTotal;

        itemName.setText(appModelList.get(position).getI_name());
        //itemVolume.setText(appModelList.get(position).getI_vol());
        //itemQuantity.setText(appModelList.get(position).getI_quantity());
        holder.myCustomEditTextListener.updatePosition(position);
        holder.myCustomEditTextlistenerTwo.updatePosition(position);
        holder.itemTotal.setText(appModelList.get(position).getI_quantity());
        holder.itemVolume.setText(appModelList.get(position).getI_vol());
        holder.itemQuantity.setText(appModelList.get(position).getI_quantity());

    }

    @Override
    public int getItemCount() {
        return appModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyCustomEditTextListener myCustomEditTextListener;
        public MyCustomEditTextlistenerTwo myCustomEditTextlistenerTwo;
        TextView itemName;
        EditText itemVolume;
        EditText itemQuantity;
        TextView itemTotal;

        public MyViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener, MyCustomEditTextlistenerTwo myCustomEditTextlistenerTwo) {
            super(itemView);
            this.itemName = (TextView) itemView.findViewById(R.id.item_name);
            this.itemVolume = (EditText) itemView.findViewById(R.id.item_volume);
            this.itemQuantity = (EditText) itemView.findViewById(R.id.item_quantity);

            this.myCustomEditTextListener = myCustomEditTextListener;
            this.myCustomEditTextlistenerTwo = myCustomEditTextlistenerTwo;
            itemQuantity.addTextChangedListener(myCustomEditTextListener);
            itemVolume.addTextChangedListener(myCustomEditTextlistenerTwo);
            this.itemTotal = (TextView) itemView.findViewById(R.id.item_total_volume);
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            appModelList.get(position).setI_quantity(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    //
    private class MyCustomEditTextlistenerTwo implements TextWatcher {

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            appModelList.get(position).setI_vol(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}

