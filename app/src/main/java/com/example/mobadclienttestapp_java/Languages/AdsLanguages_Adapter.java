package com.example.mobadclienttestapp_java.Languages;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobadclienttestapp_java.R;
import com.example.mobadclienttestapp_java.Utils.Save_Languages;
import com.imagineworks.mobad_sdk.models.Language;

import java.util.List;

public class AdsLanguages_Adapter extends RecyclerView.Adapter<AdsLanguages_Adapter.ViewHolder> {

    private final List<Language> languages;

    private final LayoutInflater mInflater;

    private ItemClickListener mClickListener;

    //Data is passed into the constructor
    AdsLanguages_Adapter(Context context, List<Language> languages) {
        this.mInflater = LayoutInflater.from(context);
        for(int i=0;i<languages.size();i++){
            boolean is_selected = Save_Languages.get_language(context,languages.get(i).getNativeName());
            if(!is_selected){
                languages.get(i).setSelected(false);
            }
        }
        this.languages = languages;
    }

    //Inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.selectable_item, parent, false);
        return new ViewHolder(view);
    }

    //Binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Language language = languages.get(position);
        holder.language_textview.setText(language.getNativeName());
        if(!language.isSelected()){
            holder.is_checked_textview.setText("");
            holder.itemView.setBackgroundResource(R.color.white);
        }
        else{
            holder.itemView.setBackgroundResource(R.color.material_grey_200);
        }
    }

    //Total number of rows
    @Override
    public int getItemCount() {
        if(languages!=null) return languages.size();
        return 0;
    }



    //Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView language_textview;
        TextView is_checked_textview;

        ViewHolder(View itemView) {
            super(itemView);
            language_textview = itemView.findViewById(R.id.itemCheckBox);
            is_checked_textview = itemView.findViewById(R.id.checkmarkText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    //Convenience method for getting data at click position
    Language getItem(int id) {
        return languages.get(id);
    }

    List<Language> get_list() {
        return languages;
    }

    //Allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    //Parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}