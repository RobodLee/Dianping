package com.dianping.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dianping.android.enity.Goods;
import com.dianping.android.enity.Shop;
import com.dianping.android.utils.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GoodsDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView goods_detail_back;
    private ImageView goods_image;
    private TextView goods_title;
    private TextView goods_desc;
    private TextView shop_title;
    private TextView shop_phone;
    private ImageView call_image;
    private TextView shop_address;
    private TextView goods_price;
    private TextView goods_old_price;
    private WebView tv_more_details_web_view;
    private WebView wv_gn_warn_prompt;

    Goods goods;
    String imageUrl;

    String detail = "<div class=\\\"prodetail spl\"> <h4 style=\\\"background #720c00V\">" +
            "【本单详情】</h4><p class=\\\"tcV*>凭券可享巴厘巴厘库SPA会馆养生套系一份(全程约135分钟) </p><table id=\\\"tc_ line_ SV\"> <tbody><tr><th>内容</th><th>数量</th> <th>时长</th> <th>价格</th></tr><tr> <td>水晶足疗</td> <td> 1次</td><td>约60分钟</td><td> 108元</td></tr><tr><td>全身精油护理</td> <td> 1次</td> <td>约60分钟</td> <td>288元</td></tr> <tr> <td>肾部保养</d><td> 1次</td><td>约15分钟</td><td>100元</td></tr><tr><th colspan=\\\"4[>赠送</th></tr><tr> <td>淋浴</td><td>2次</td> <td colspan=\\\"21\\\">当日营业时间内最多体验2次</td></tr><tr><td>茶饮</td> <td>1杯</d><td colspan=\\\"2\\\">菊花茶，可续水</d></tr><tr><td>果盘</td><td> 1份</d><td colspan=\\\"2\\\">时令水果</td></tr><tr><td>简餐</td><td>1份</td><td colspan=V\"2\">水饺/牛肉面粥，任选1份</td></tr><tr class=\\tr\"><td colspan=\\\"4\\\" align=\\\"right\">市场价合计496元，拉手团购仅需<strong style=\\\"color#720c00\\\"class=\\\"f18V*>98</strong>元</d> </tr></tbody></table> <h4 style=\\\"background #720c00\\\">" +
            "【温馨提示】 </h4><ul> <li> <p>每张券可享巴厘巴厘库SPA会馆养生套系一份(全程约135分钟) ; </p></i><li><p>拉手券 于2012年5月23日(周三)生效; </p><//i><i> <p>券有效期截止至2014年4月30日; </p></i><li><p><strong>营业时间:10:00-次日凌晨02:00; </strong></p></i><li> <p><strong style=\\\"color#720c00\\*>为保证服务质量，请您至少提前1天预约(每晚19:00-22 00不接受预约，请见谅)，商家于2012年5月23日(周三)开始接受预约，预约电话: 400-0003-668, 预约时无需提供拉手券密码; </strong> </p></i> <li> <p> <strong style=\\\"color #720c00\\*>每张拉手券仅限1人使用次，每人每次仅限使用1张拉手券;</strong></p></i> <li><p><strong style=\"'color:#720c00\\*>本次团购不限男、女宾，团购内容需在当日营业时间内一次性消费完成; </strong></p> </i> <li> <p> <strong style=\\\"color:#720c0\\\">商家免费提供淋浴所需洗浴用品(仅限店内使用，不可带走) ; </strong></p></i><li><p><strong style=V\"color.#720c00\">店内会员可以使用拉手券，但不可享受会员积分，不与会员优惠同享;</strong></p> </i><li><p><span style=\\\"color.#720c00\">拉手券不兑现、不找零，不与店内其他优惠同时享用。</span></p> </i> </ul> <h4 style=\\\"background #720c00\\\">" +
            "【精品展示】</h4><p><strong>[全身精油护理]</strong> &nbsp:有利于清除体内废气、废物、垃圾。有助于清洁、疏通细胞组织液、血液、淋巴液，改善肌肤油脂平衡。</p><p class=\\\"tc\\\"> <imgsrc=ttp://ig lashou com/wg/beijing/201206/07/ali001 yx jpg\\\" /></p><p><strong>[肾部保养]</strong> &nbsp:</p><p>1、有助于改善肾虚症状，并且有助于排除身体多余的水分和毒素; </p><p>2、 全方位调节各个脏器功能的平衡，提高机体免疫力; </p><p>3、 保持健康的心理，缓解紧张状态及压力，振奋心情，提高信心。</p><pclass=\\\"tcl\"><img src=ttp://ig lashou.com/wg/beiing/201206/07/bali002_ yxjpg\\\" 1></p><p class=\\\"tc\\\"><imgsrcttp://ig lashou comwgbejing/201206/07/balio3 yx jpg\\\" /></p><p><strong>环境介绍</strong></p><p class=Vtil\">#会馆优雅环境美丽养生。内设有豪华的贵宾间，异域风格的养生房，淡雅清净的茶室，恬静文化的休闲区，在这里养生的理念是一个永久不老的传说。为满足顾客的多层次需要，巴厘巴厘库为客人准备了众多世界-线品牌和巴厘岛原生态SPA产品。还特别增设了足疗、茶艺、中医美容、经络养生、芳香SPA、专业足疗等丰富的美容美体项目。</p><p class=\\*\"tc\\\"><imgsrc=http://ig lashou.com/wg/beijing/201206/07/bali004 yx.jpgy\" /></p><p class=\\\"tcV\"> <img src='\"http://img lashou comwg/beiing/201206/07/bali005_ yx.jpg\\\" /></p><p class=\\\"tc\"> <img src=thttp://img lashou.com/wg/beijing/201206/07/bali0o6 yx jpgv\" /></p><h4 style=\\\"'background:#720c00\\\">巴厘巴厘库</h4> <p class=\\\"ti\">巴厘巴厘库之名源于著名的印度尼西亚旅游胜地巴厘岛，在那久负盛名的金巴兰海岸，在那神秘典雅的库塔，一个名字像美丽金巴兰的海水-样向我们涌来一巴厘巴厘库。</p><p class=\\\"tc\\\"> <img sc=\\http://img lashou com/wg/beiing/201206/07/ali007_ yx.jpg\" /></p><p class=\\\"tc\\\"><imgsrc=Ihttp /ing lashou com/wg/beijing/201206/07/ali008 yx jpg\\\" /></p><p class=Vti\">巴厘巴厘库SPA养生连锁机构由著名养生专家王镝先生于2010年创立，致力发展成为一家国内外具有连锁规模和良好美誉的养生连锁机构。企业经过稳步的经营拓展，已在京拥有营业规模达1800多平米的SPA养生会馆，巴厘巴厘库会馆内的环境以印尼异国风格为基调，国际五星级为基准，人性化为考量，营造出宽敞舒适、幽雅芳馨的养生沙龙。优秀的品质，细致的服务，美誉的口碑、专业的技术以及过得硬的产品，使巴厘巴厘库赢得了3000多名的会员，顾客的信任和青睐是巴厘巴厘库发展的源泉。</p><p class=\\\"tc\\\"><imgsrc-ttp://img.lashou.comwg/beijing120120607/bali009 _yx.jpg\\\" ></p><p class='ti\">企‘麦止莽生’注重效果。企止高薪聘靖技木怠監、培訓忌監，不断学可先迸的券生項目理於和考止手法，企止定期迸行SPA師級別考核，定期考核_上崗。企打造了一-支考止服努，考止莽生的考止臥伍。重麦止，重效果是企止品牌之根本。</p><pclass='tcl\"> cimg src=Vhttp://img lashou.comwg/beijing/201206107/balli010 _yx jpg'\" ></p>" +
            "</div>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuan_goods_detail);

        goods_detail_back = (TextView) findViewById(R.id.goods_detail_back);
        goods_detail_back.setOnClickListener(this);
        goods_image = (ImageView) findViewById(R.id.goods_image);
        goods_title = (TextView) findViewById(R.id.goods_title);
        goods_desc = (TextView) findViewById(R.id.goods_desc);
        shop_title = (TextView) findViewById(R.id.shop_title);
        shop_phone = (TextView) findViewById(R.id.shop_phone);
        call_image = (ImageView) findViewById(R.id.call_image);
        call_image.setOnClickListener(this);
        shop_address = (TextView) findViewById(R.id.shop_address);
        goods_price = (TextView) findViewById(R.id.goods_price);
        goods_old_price = (TextView) findViewById(R.id.goods_old_price);
        tv_more_details_web_view = (WebView) findViewById(R.id.tv_more_details_web_view);
        wv_gn_warn_prompt = (WebView) findViewById(R.id.wv_gn_warn_parompt);

        goods = (Goods) getIntent().getSerializableExtra("good");

        if (goods != null) {
            HttpUtil.SendOkHttpRequest(goods.getImgUrl(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    imageUrl = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateGoodsImage();
                        }
                    });

                }
            });
            updateGoodsInfo();
            updateShopInfo();
            updateMoreDetails();
        }
    }

    //更新商品的标题页面图片
    private void updateGoodsImage() {
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_empty_dish).into(goods_image);
        }
    }

    //更新商品信息
    private void updateGoodsInfo() {
        goods_title.setText(goods.getTitle());
        goods_desc.setText(goods.getSortTitle());
        goods_price.setText("￥" + goods.getPrice());
        goods_old_price.setText("￥" + goods.getValue());
    }

    //更新商家信息
    private void updateShopInfo() {
        Shop shop = goods.getShop();
        shop_title.setText(shop.getName());
        shop_phone.setText(shop.getTel());
        shop_address.setText(shop.getAddress());
    }

    //加载详情内容
    private void updateMoreDetails() {
        String[] htmlDatas = htmlSub();
        tv_more_details_web_view.loadDataWithBaseURL(null, htmlDatas[0], "text/html", "utf-8",null);
        wv_gn_warn_prompt.loadDataWithBaseURL(null, htmlDatas[1], "text/html", "utf-8",null);

    }

    private String[] htmlSub() {
        String[] datas = new String[3];
        char[] Array = detail.toCharArray();
        int len = detail.length();
        int n = 0;  //记录是第几次遇到'【'
        int oneIndex = 0;
        int secondIndex = 0;
        int thirdIndex = 0;
        for (int i=0; i<len; i++) {
            if (Array[i] == '【') {
                n++;
                if (n==1) {
                    oneIndex = i;
                } else if (n==2) {
                    secondIndex = i;
                } else if (n==3) {
                    thirdIndex = i;
                }
            }
            if (oneIndex>0 && secondIndex>0 && thirdIndex>0) {
                datas[0] = detail.substring(oneIndex,secondIndex);
                datas[1] = detail.substring(secondIndex,thirdIndex);
                datas[2] = detail.substring(thirdIndex,len-6);
            }
        }

        return datas;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call_image:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + goods.getShop().getTel()));
                startActivity(intent);
                break;
            case R.id.goods_detail_back:
                finish();
                break;
            default:break;
        }
    }
}
