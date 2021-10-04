package com.example.mobadclienttestapp_java.Interests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobadclienttestapp_java.R;
import com.imagineworks.mobad_sdk.models.Subcategory;

import java.util.List;

public class Subcategories_Adapter extends RecyclerView.Adapter<Subcategories_Adapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Subcategory subcategory, View view);
    }

    private final List<Subcategory> subcategories;
    private final OnItemClickListener listener;

    //Data is passed into the constructor
    public Subcategories_Adapter(List<Subcategory> subcategories, OnItemClickListener listener) {
        this.subcategories = subcategories;
        this.listener = listener;
    }

    //Inflates the row layout from xml when needed
    @NonNull
    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selectable_item, parent, false);
        return new ViewHolder(v);
    }

    //Binds the data to the TextView in each row
    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(subcategories.get(position), listener);
    }

    //Total number of rows
    @Override public int getItemCount() {
        return subcategories.size();
    }

    //Stores and recycles views as they are scrolled off screen
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView subcategory_name;
        private final TextView subcategory_check;

        public ViewHolder(View itemView) {
            super(itemView);
            subcategory_name = itemView.findViewById(R.id.itemCheckBox);
            subcategory_check = itemView.findViewById(R.id.checkmarkText);
        }

        public void bind(final Subcategory subcategory, final OnItemClickListener listener) {
            subcategory_name.setText(subcategory.getName());
            if(!subcategory.isUserInterested()){
                subcategory_check.setText("");
                itemView.setBackgroundResource(R.color.white);
            }
            else{
                itemView.setBackgroundResource(R.color.material_grey_200);
            }
            itemView.setOnClickListener(v -> listener.onItemClick(subcategory,v));
        }
    }
}