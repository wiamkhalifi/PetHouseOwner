package com.example.pethouseowner.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pethouseowner.Common.Common;
import com.example.pethouseowner.Interface.ItemClickListener;
import com.example.pethouseowner.R;

public class ProViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener

{

     public TextView pro_name;
    public ImageView pro_image;
    private ItemClickListener itemClickListener;
    public ProViewHolder(@NonNull View itemView) {
        super(itemView);
          pro_name=(TextView)itemView.findViewById(R.id.pro_name);
        pro_image=(ImageView)itemView.findViewById(R.id.pro_image);
        itemView.setOnCreateContextMenuListener(this);

        itemView.setOnClickListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Sélectionnez une action  ");

        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(),Common.DELETE);
    }
}
