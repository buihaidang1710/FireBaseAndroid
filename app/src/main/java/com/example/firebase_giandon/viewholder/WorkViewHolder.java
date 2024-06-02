package com.example.firebase_giandon.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase_giandon.R;
import com.example.firebase_giandon.model.ItemClickListener;

public class WorkViewHolder extends RecyclerView.ViewHolder
implements View.OnClickListener{
    public TextView txtName,txtDesc,txtDate;
    public ImageView img;
    public ItemClickListener listener;
    public WorkViewHolder(@NonNull View view) {
        super(view);
        txtName=view.findViewById(R.id.txtName);
        txtDesc=view.findViewById(R.id.txtDesc);
        txtDate=view.findViewById(R.id.txtDate);
        img=view.findViewById(R.id.img);
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }
}
