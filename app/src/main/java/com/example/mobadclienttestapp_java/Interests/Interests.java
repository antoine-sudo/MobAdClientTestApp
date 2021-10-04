package com.example.mobadclienttestapp_java.Interests;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobadclienttestapp_java.R;
import com.imagineworks.mobad_sdk.MobAd;
import com.imagineworks.mobad_sdk.models.Category;
import com.imagineworks.mobad_sdk.models.Subcategory;

import java.util.ArrayList;
import java.util.List;

public class Interests extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressBar;
    Button save_interests;
    RecyclerView recyclerView;
    List<Category> users_categories;
    List<Subcategory> users_interests;

    Categories_Adapter adapter;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interests);

        //Initialize the toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle(R.string.interests);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        ActionBar ab = getSupportActionBar();
        assert ab != null;

        //Initialize the progressbar
        progressBar =findViewById(R.id.progressBar_interests);
        progressBar.setVisibility(View.VISIBLE);

        //Initialize the MobAdSDK
        MobAd mobAd = new MobAd(this);
        boolean is_initialized= mobAd.isUserInitialized();

        //Call the getCategories to retrieve user's interests
        if(is_initialized){
            mobAd.getCategories(new MobAd.GetCategoriesCompletionListener() {
                @Override
                public void onSuccess(@NonNull List<Category> categories) {
                    recyclerView = findViewById(R.id.categoriesRecyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Interests.this));
                    adapter=new Categories_Adapter(categories,Interests.this, (category, view) -> {
                        TextView category_text=view.findViewById(R.id.categorySectionText);
                        RecyclerView recyclerView=view.findViewById(R.id.subcategoriesRecyclerView);
                        if(recyclerView.getVisibility()==View.VISIBLE){
                            recyclerView.setVisibility(View.GONE);
                            category_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_less, 0);
                        }
                        else{
                            recyclerView.setVisibility(View.VISIBLE);
                            category_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                    users_categories=categories;
                    Log.i("MobAd", "Interests Retrieved");
                }

                @Override
                public void onError(int code, @Nullable String message) {
                    Toast.makeText(getApplicationContext(),"Failed to retrieve interests.", Toast.LENGTH_SHORT).show();
                    Log.i("MobAd", "Failed to retrieve interests");
                }
            });
        }

        //Save button to save interests and call the updateInterests function
        save_interests = findViewById(R.id.saveSubcategoriesButton);
        save_interests.setOnClickListener(view -> {

            if(adapter != null){

                progressBar.setVisibility(View.VISIBLE);

                users_categories=adapter.get_categories();

                users_interests=new ArrayList<>();

                for(int i=0;i<users_categories.size();i++){
                    for(Subcategory subcategory:users_categories.get(i).getSubcategories())
                        if(subcategory.isUserInterested())
                            users_interests.add(subcategory);
                }

                if(users_interests.isEmpty()){
                    AlertDialog.Builder alert = new AlertDialog.Builder(Interests.this);
                    alert.setTitle("Oops");
                    alert.setMessage("You should at least choose one interest.");
                    alert.setPositiveButton("OK!",null);
                    AlertDialog a = alert.create();
                    a.show();
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(new Categories_Adapter(users_categories,Interests.this, (category, holder_view) -> {
                        TextView category_text=holder_view.findViewById(R.id.categorySectionText);
                        RecyclerView recyclerView=holder_view.findViewById(R.id.subcategoriesRecyclerView);
                        if(recyclerView.getVisibility()==View.VISIBLE){
                            recyclerView.setVisibility(View.GONE);
                            category_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_less, 0);
                        }
                        else{
                            recyclerView.setVisibility(View.VISIBLE);
                            category_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        }
                        adapter.notifyDataSetChanged();
                    }));
                }
                else{
                    mobAd.updateInterests(users_interests, new MobAd.UpdateInterestsCompletionListener() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Your interests have been updated.", Toast.LENGTH_SHORT).show();
                            Log.i("MobAd", "Interests Updated");
                        }
                        @Override
                        public void onError(int code, @Nullable String message) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Failed to update interests.", Toast.LENGTH_SHORT).show();
                            Log.i("MobAd", "Failed to update interests");
                        }
                    });
                }
            }
        });
        ////////////////////////////////////////////
    }

    //Inflate option menu and assign for each item it's behavior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.interests_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //RecyclerView subcategory_recycle=Interests_Adapter.get_subcategories_recycle();
        switch (item.getItemId()) {
            case R.id.actionSelectAll:
                if(adapter != null){
                    for(Category category:users_categories){
                        for(Subcategory subcategory:category.getSubcategories()){
                            subcategory.setUserInterested(true);
                        }
                    }
                    recyclerView.setAdapter(new Categories_Adapter(users_categories,Interests.this, (category, view) -> {
                        TextView category_text=view.findViewById(R.id.categorySectionText);
                        RecyclerView recyclerView=view.findViewById(R.id.subcategoriesRecyclerView);
                        if(recyclerView.getVisibility()==View.VISIBLE){
                            recyclerView.setVisibility(View.GONE);
                            category_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_less, 0);
                        }
                        else{
                            recyclerView.setVisibility(View.VISIBLE);
                            category_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        }
                    }));
                }
                return true;
            case R.id.actionUnselectAll:
                if(adapter != null){
                    for(Category category:users_categories){
                        for(Subcategory subcategory:category.getSubcategories()){
                            subcategory.setUserInterested(false);
                        }
                    }
                    recyclerView.setAdapter(new Categories_Adapter(users_categories,Interests.this, (category, view) -> {
                        TextView category_text=view.findViewById(R.id.categorySectionText);
                        RecyclerView recyclerView=view.findViewById(R.id.subcategoriesRecyclerView);
                        if(recyclerView.getVisibility()==View.VISIBLE){
                            recyclerView.setVisibility(View.GONE);
                            category_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_less, 0);
                        }
                        else{
                            recyclerView.setVisibility(View.VISIBLE);
                            category_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        }
                    }));
                }
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    ///////////////////////////////
}