package com.example.newsapi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapi.R;
import com.example.newsapi.activity.BusinessNewsActivity;
import com.example.newsapi.activity.EntertainmentNewsActivity;
import com.example.newsapi.activity.HealthNewsActivity;
import com.example.newsapi.activity.ScienceNewsActivity;
import com.example.newsapi.activity.SportsNewsActivity;
import com.example.newsapi.activity.TechnologyNewsActivity;
import com.example.newsapi.model.MenuModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<com.example.newsapi.adapter.MenuAdapter.MyViewHolder>{
    private Context mContext;
    private List<String> menu;

    public MenuAdapter(Context mContext, List<String> items) {
        this.mContext = mContext;
        this.menu = items;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textMenuCell)
        TextView textMenu;
        @BindView(R.id.ivMenu)
        ImageView ivMenu;
        @BindView(R.id.cvMenu)
        CardView cvMenu;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_cell, parent, false);
        return new MenuAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (menu.get(position).equals("Technology")){
            holder.ivMenu.setImageResource(R.mipmap.ic_technology);
            holder.textMenu.setText(""+menu.get(position));
            holder.cvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TechnologyNewsActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else if (menu.get(position).equals("Business")){
            holder.ivMenu.setImageResource(R.mipmap.ic_business);
            holder.textMenu.setText("" + menu.get(position));
            holder.cvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BusinessNewsActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else if (menu.get(position).equals("Science")){
            holder.ivMenu.setImageResource(R.mipmap.ic_science);
            holder.textMenu.setText("" + menu.get(position));
            holder.cvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ScienceNewsActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else if (menu.get(position).equals("Sports")){
            holder.ivMenu.setImageResource(R.mipmap.ic_sports);
            holder.textMenu.setText("" + menu.get(position));
            holder.cvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SportsNewsActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else if (menu.get(position).equals("Entertainment")){
            holder.ivMenu.setImageResource(R.mipmap.ic_entertainment);
            holder.textMenu.setText("" + menu.get(position));
            holder.cvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EntertainmentNewsActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else if (menu.get(position).equals("health")){
            holder.ivMenu.setImageResource(R.mipmap.ic_health);
            holder.textMenu.setText("" + menu.get(position));
            holder.cvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, HealthNewsActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }
}

