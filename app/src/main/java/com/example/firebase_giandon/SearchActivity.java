package com.example.firebase_giandon;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase_giandon.adapter.RecyclerViewWorkAdapter;
import com.example.firebase_giandon.model.Work;
import com.example.firebase_giandon.viewholder.WorkViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private List<Work> mList;
    private RecyclerViewWorkAdapter adapter;
    //private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        searchView=findViewById(R.id.search);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        mList=new ArrayList<>();
        adapter=new RecyclerViewWorkAdapter(this,mList);
        recyclerView.setAdapter(adapter);
    }
    private List<Work> search(String s){
        List<Work> list=new ArrayList<>();
        for(Work i:mList){
            if(i.getName().toLowerCase().contains(s.toLowerCase())){
                list.add(i);
            }
        }
        return list;
    }


}