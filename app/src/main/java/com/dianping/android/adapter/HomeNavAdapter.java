package com.dianping.android.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dianping.android.AllCategoryActivity;
import com.dianping.android.MyApplication;
import com.dianping.android.R;
import com.dianping.android.enity.Navigation;
import com.dianping.android.utils.MyUtils;

import java.util.List;

public class HomeNavAdapter extends RecyclerView.Adapter<HomeNavAdapter.ViewHolder> {

    private List<Navigation> navigations;

    private int position;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView navImage;
        TextView navName;

        public ViewHolder(View view) {
            super(view);
            navImage = (ImageView) view.findViewById(R.id.home_nav_item_image);
            navName = (TextView) view.findViewById(R.id.home_nav_item_name);
        }
    }

    public HomeNavAdapter(List<Navigation> navigations) {
        this.navigations = navigations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_nav_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Navigation navigation = navigations.get(position);
        holder.navImage.setImageResource(navigation.getImageId());
        holder.navName.setText(navigation.getName());
        //为索引为(MyUtils.navsSort.length-1)的选项设置点击事件
        if (position == MyUtils.navsSort.length-1) {
            holder.navImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.getContext().startActivity(new Intent(MyApplication.getContext(), AllCategoryActivity.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return navigations.size();
    }

}
