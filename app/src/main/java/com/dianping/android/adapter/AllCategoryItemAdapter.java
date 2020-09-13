package com.dianping.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dianping.android.R;
import com.dianping.android.enity.CategoryItem;
import com.dianping.android.utils.MyUtils;

import java.util.List;

public class AllCategoryItemAdapter extends ArrayAdapter<CategoryItem>{

    private int resourceId;

    public AllCategoryItemAdapter(Context context, int resourceId, List<CategoryItem> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryItem item = getItem(position);
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.home_all_category_item_image);
            holder.textView = (TextView) view.findViewById(R.id.home_all_category_item_text);
            holder.textNumber = (TextView) view.findViewById(R.id.home_all_category_item_number);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.imageView.setImageResource(item.getImageId());
        holder.textView.setText(item.getText());
        holder.textNumber.setText(MyUtils.allCategoryNumber[position] + "");
        return view;
    }

    class ViewHolder {

        ImageView imageView;

        TextView textView;

        TextView textNumber;
    }

}
