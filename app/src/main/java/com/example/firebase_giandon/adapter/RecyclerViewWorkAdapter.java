package com.example.firebase_giandon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase_giandon.EditDeleteActivity;
import com.example.firebase_giandon.R;
import com.example.firebase_giandon.model.Work;
import com.example.firebase_giandon.viewholder.WorkViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewWorkAdapter extends RecyclerView.Adapter<WorkViewHolder> {
    private Context mContext;
    private List<Work> mList;

    public RecyclerViewWorkAdapter(Context mContext,List<Work> mList) {
        this.mContext = mContext;
        this.mList=mList;
    }

    public void setmList(List<Work> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item,parent,false);
        return new WorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkViewHolder holder, int position) {
        Work model=mList.get(position);
        holder.txtName.setText(model.getName());
        holder.txtDesc.setText(model.getDesc());
        holder.txtDate.setText(model.getDate());
        Picasso.get().load(model.getImage()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, EditDeleteActivity.class);
                intent.putExtra("key",model.getKey());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
