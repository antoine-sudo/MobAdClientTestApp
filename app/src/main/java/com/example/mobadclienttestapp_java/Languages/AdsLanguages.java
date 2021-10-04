package com.example.mobadclienttestapp_java.Languages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.example.mobadclienttestapp_java.Utils.Save_Languages;
import com.imagineworks.mobad_sdk.MobAd;
import com.imagineworks.mobad_sdk.models.Language;

import java.util.List;

public class AdsLanguages extends AppCompatActivity implements AdsLanguages_Adapter.ItemClickListener {

    Toolbar toolbar;
    private MobAd mobAd;
    ProgressBar progressBar;
    Button save_languages;
    RecyclerView recyclerView;
    AdsLanguages_Adapter adapter;
    List<Language> users_languages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_languages);

        //Initialize the toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle(R.string.ad_languages);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        ActionBar ab = getSupportActionBar();
        assert ab != null;

        //Initialize the ProgressBar
        progressBar =findViewById(R.id.progressBar_languages);
        progressBar.setVisibility(View.VISIBLE);

        //Initialize Mob Ad and check if initialized
        mobAd= new MobAd(this);
        boolean is_initialized=mobAd.isUserInitialized();

        //Call the getAdLanguages to retrieve user's languages
        if(is_initialized){
            mobAd.getAdLanguages(new MobAd.GetAdLanguagesCompletionListener() {
            @Override
            public void onSuccess(@NonNull List<Language> languages) {
                recyclerView = findViewById(R.id.languagesRecyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdsLanguages.this));
                adapter = new AdsLanguages_Adapter(AdsLanguages.this, languages);
                adapter.setClickListener(AdsLanguages.this);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                users_languages=adapter.get_list();
                Log.i("MobAd", "Languages Retrieved");
            }

            @Override
            public void onError(int code, @Nullable String message) {
                Toast.makeText(getApplicationContext(),"Failed to retrieve languages.", Toast.LENGTH_SHORT).show();
                Log.i("MobAd", "Failed to retrieve languages");
            }
            });
        }

        //Save button to save languages and call the updateInterests function and write in shared preferences
        save_languages = findViewById(R.id.saveLanguagesButton);
        save_languages.setOnClickListener(view -> {
            if(adapter != null){
                progressBar.setVisibility(View.VISIBLE);

                boolean at_least_one_selected=false;

                for(int i=0;i<users_languages.size();i++){
                    Save_Languages.set_language(this,users_languages.get(i).getNativeName(),users_languages.get(i).isSelected());
                    if(users_languages.get(i).isSelected())
                        at_least_one_selected=true;
                }
                if(at_least_one_selected){
                    mobAd.updateAdLanguages(users_languages, new MobAd.UpdateAdLanguagesCompletionListener() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Your languages have been updated.",Toast.LENGTH_SHORT).show();
                            Log.i("MobAd", "Languages Updated");
                        }

                        @Override
                        public void onError(int code, @Nullable String message) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Failed to update languages.",Toast.LENGTH_SHORT).show();
                            Log.i("MobAd", "Failed to update languages");
                        }
                    });
                }
                else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(AdsLanguages.this);
                    alert.setTitle("Oops");
                    alert.setMessage("You should at least choose one language.");
                    alert.setPositiveButton("OK!",null);
                    AlertDialog a = alert.create();
                    a.show();
                }
            }
        });
    }

    //To assign for each row it's behavior
    @Override
    public void onItemClick(View view, int position) {
        if(adapter.getItem(position).isSelected()){
            adapter.getItem(position).setSelected(false);
            users_languages.get(position).setSelected(false);
            view.setBackgroundResource(R.color.white);
            RecyclerView.ViewHolder holder =recyclerView.findViewHolderForAdapterPosition(position);
            assert holder != null;
            TextView txt =holder.itemView.findViewById(R.id.checkmarkText);
            txt.setText("");
        }
        else{
            adapter.getItem(position).setSelected(true);
            users_languages.get(position).setSelected(true);
            view.setBackgroundResource(R.color.material_grey_200);
            RecyclerView.ViewHolder holder =recyclerView.findViewHolderForAdapterPosition(position);
            assert holder != null;
            TextView txt =holder.itemView.findViewById(R.id.checkmarkText);
            txt.setText(R.string.checkmark);
        }
    }


    //Inflate option menu and assign for each item it's behavior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.languages_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSelectAll:
                if(adapter != null){
                    for(int i=0;i<users_languages.size();i++){
                        users_languages.get(i).setSelected(true);
                        RecyclerView.ViewHolder holder =recyclerView.findViewHolderForAdapterPosition(i);
                        assert holder != null;
                        RelativeLayout rel=holder.itemView.findViewById(R.id.selectableItemContainer);
                        rel.setBackgroundResource(R.color.material_grey_200);
                        TextView txt =holder.itemView.findViewById(R.id.checkmarkText);
                        txt.setText(R.string.checkmark);
                    }
                }
                return true;
            case R.id.actionUnselectAll:
                if(adapter != null){
                    for(int i=0;i<users_languages.size();i++){
                        users_languages.get(i).setSelected(false);
                        RecyclerView.ViewHolder holder =recyclerView.findViewHolderForAdapterPosition(i);
                        assert holder != null;
                        RelativeLayout rel=holder.itemView.findViewById(R.id.selectableItemContainer);
                        rel.setBackgroundResource(R.color.white);
                        TextView txt =holder.itemView.findViewById(R.id.checkmarkText);
                        txt.setText("");
                    }
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