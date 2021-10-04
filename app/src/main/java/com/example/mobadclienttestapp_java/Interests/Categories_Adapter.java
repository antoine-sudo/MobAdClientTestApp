package com.example.mobadclienttestapp_java.Interests;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobadclienttestapp_java.R;
import com.imagineworks.mobad_sdk.models.Category;
import com.imagineworks.mobad_sdk.models.Subcategory;

import java.util.List;


public class Categories_Adapter extends RecyclerView.Adapter<Categories_Adapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Category category,View view);
    }

    public static List<Category> categories=null;
    private final OnItemClickListener listener;
    public final Context context;

    //Data is passed into the constructor
    public Categories_Adapter(List<Category> categories,Context context ,OnItemClickListener listener) {
        Categories_Adapter.categories = categories;
        this.listener = listener;
        this.context=context;
    }

    //Inflates the row layout from xml when needed
    @NonNull
    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(v);
    }

    //Binds the data to the TextView in each row
    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(categories.get(position), listener,context);
    }

    //Total number of rows
    @Override public int getItemCount() {
        return categories.size();
    }

    //Returns the Categories List
    public List<Category> get_categories(){
        return categories;
    }

    //Stores and recycles views as they are scrolled off screen
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView category_name;
        private final RecyclerView subcategories_recyclerview;

        public ViewHolder(View itemView) {
            super(itemView);
            category_name = itemView.findViewById(R.id.categorySectionText);
            subcategories_recyclerview = itemView.findViewById(R.id.subcategoriesRecyclerView);
        }

        public void bind(final Category category, final OnItemClickListener listener,Context context) {
            category_name.setText(category.getName());
            subcategories_recyclerview.setLayoutManager(new LinearLayoutManager(context));
            subcategories_recyclerview.setAdapter(new Subcategories_Adapter(category.getSubcategories(), (subcategory, view) -> {
                TextView txt=view.findViewById(R.id.checkmarkText);
                if(subcategory.isUserInterested()){
                    subcategory.setUserInterested(false);
                    for(Category category_to_change:categories){
                        if(category_to_change.getId()==subcategory.getCategoryId()){
                            for(Subcategory subcategory_to_change:category_to_change.getSubcategories()){
                                if(subcategory_to_change.getId()==subcategory.getId())
                                    subcategory_to_change.setUserInterested(false);
                            }
                        }
                    }
                    txt.setText("");
                    view.setBackgroundResource(R.color.white);
                }
                else{
                    subcategory.setUserInterested(true);
                    for(Category category_to_change:categories){
                        if(category_to_change.getId()==subcategory.getCategoryId()){
                            for(Subcategory subcategory_to_change:category_to_change.getSubcategories()){
                                if(subcategory_to_change.getId()==subcategory.getId())
                                    subcategory_to_change.setUserInterested(true);
                            }
                        }
                    }
                    txt.setText(R.string.checkmark);
                    view.setBackgroundResource(R.color.material_grey_200);
                }
            }));
            subcategories_recyclerview.setVisibility(View.GONE);
            itemView.setOnClickListener(v -> listener.onItemClick(category,v));
        }
    }
}