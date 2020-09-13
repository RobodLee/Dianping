package com.dianping.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dianping.android.GoodsDetailActivity;
import com.dianping.android.MyApplication;
import com.dianping.android.R;
import com.dianping.android.constants.Constants;
import com.dianping.android.enity.Goods;
import com.dianping.android.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentTuan extends Fragment {

    RefreshLayout refreshLayout;
    ListView goodsListView;
    List<Goods> goodsList = new ArrayList<>();
    GoodsAdapter adapter;

    String imageUrl;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tuan_index, container, false);

        refreshLayout = (RefreshLayout) view.findViewById(R.id.smartrefreshlayout);
        refreshLayout.setRefreshHeader(new BezierRadarHeader(getContext()).setPrimaryColor(Color.rgb(2, 159, 241)));
        refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadDatas(false);
                refreshlayout.finishRefresh();
                refreshlayout.setEnableLoadMore(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page<=count) {
                    loadDatas(true);
                    refreshLayout.finishLoadMore();
                }
                if (page > count){
                    Toast.makeText(getContext(),"没有更多数据",Toast.LENGTH_SHORT).show();
                    refreshLayout.finishLoadMore();
                }
            }
        });
        goodsListView = (ListView) view.findViewById(R.id.index_goods_list);

        goodsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
                intent.putExtra("good",goodsList.get(position));
                startActivity(intent);
            }
        });

        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                loadDatas(false);
            }
        }.sendEmptyMessageDelayed(0,300);

        return view;
    }

    private int page = 1;
    private int size = 10;
    private int count = 10;

    private void loadDatas(final boolean isLoadMore) {
        if (isLoadMore){
            page++;
        } else {
            page = 1;
        }
        HttpUtil.SendOkHttpRequest(Constants.GOODS_DATA_URL+"?page="+page+"&size="+size, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if (!TextUtils.isEmpty(responseText)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseText);
                        String datas = jsonObject.getJSONArray("datas").toString();
                        Gson gson = new Gson();
                        if (isLoadMore) {
                            goodsList.addAll((Collection<? extends Goods>) gson.fromJson(datas,new TypeToken<List<Goods>>(){}.getType()));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            goodsList = gson.fromJson(datas, new TypeToken<List<Goods>>() {}.getType());
                            count = jsonObject.getInt("count");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new GoodsAdapter(MyApplication.getContext(), R.layout.tuan_goods_list_item, goodsList);
                                    goodsListView.setAdapter(adapter);
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private class GoodsAdapter extends ArrayAdapter {
        public GoodsAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Goods good = goodsList.get(position);
            View view;
            ViewHolder holder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.tuan_goods_list_item, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) view.findViewById(R.id.index_goods_list_item_image);
                holder.title = (TextView) view.findViewById(R.id.index_goods_list_item_title);
                holder.titleContent = (TextView) view.findViewById(R.id.index_goods_list_item_titlecontent);
                holder.price = (TextView) view.findViewById(R.id.index_goods_list_item_price);
                holder.value = (TextView) view.findViewById(R.id.index_goods_list_item_value);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            HttpUtil.SendOkHttpRequest(good.getImgUrl(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "图片加载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    imageUrl = response.body().string();
                }
            });
            Glide.with(MyApplication.getContext()).load(imageUrl).into(holder.image);
            holder.title.setText(good.getTitle());
            holder.titleContent.setText(good.getSortTitle());
            holder.price.setText(good.getPrice());
            holder.value.setText(good.getValue());
            return view;
        }

        class ViewHolder {

            ImageView image;
            TextView title;
            TextView titleContent;
            TextView price;
            TextView value;

        }
    }
}
