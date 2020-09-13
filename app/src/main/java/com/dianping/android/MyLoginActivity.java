package com.dianping.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dianping.android.constants.Constants;
import com.dianping.android.enity.ResponseObject;
import com.dianping.android.enity.User;
import com.dianping.android.fragment.FragmentMy;
import com.dianping.android.utils.HttpUtil;
import com.dianping.android.utils.SharedUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mob.MobSDK;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.dianping.android.R.id.login_btn;

public class MyLoginActivity extends AppCompatActivity implements View.OnClickListener,PlatformActionListener{

    ImageView loginBack;
    TextView register;
    EditText loginName;
    EditText loginPassword;
    Button checkRandom;
    Button loginBtn;
    TextView loginByWechat;
    TextView loginByQQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_login);

        loginBack = (ImageView) findViewById(R.id.login_back);
        loginBack.setOnClickListener(this);
        register = (TextView) findViewById(R.id.login_register);
        register.setOnClickListener(this);
        loginName = (EditText) findViewById(R.id.user_name);
        loginPassword = (EditText) findViewById(R.id.user_pass);
        checkRandom = (Button) findViewById(R.id.login_check_random);
        checkRandom.setOnClickListener(this);
        setRandomView(checkRandom);     //初始化验证码
        loginBtn = (Button) findViewById(login_btn);
        loginBtn.setOnClickListener(this);
        loginByWechat = (TextView) findViewById(R.id.login_by_wechat);
        loginByWechat.setOnClickListener(this);
        loginByQQ = (TextView) findViewById(R.id.login_by_qq);
        loginByQQ.setOnClickListener(this);

        MobSDK.init(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_back:
                finish();
                break;
            case R.id.login_register:
                Intent intent = new Intent(this, MyRegisterAcitvity.class);
                startActivity(intent);
                break;
            case R.id.login_check_random:
                setRandomView(checkRandom);     //初始化验证码
                break;
            case R.id.login_btn:
                handleLogin();
                break;
            case R.id.login_by_wechat:

                break;
            case R.id.login_by_qq:
                Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();
                loginByQQClient();
                break;
            default:break;
        }
    }

    //点击登录的时候调用的方法
    private void handleLogin() {
        final String userName = loginName.getText().toString();
        String password = loginPassword.getText().toString();
        HttpUtil.SendOkHttpRequest(Constants.USER_NAME + "&username=" + userName + "&password=" + password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"登录失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new GsonBuilder().create();
                ResponseObject<User> responseObject = gson.fromJson(response.body().string(),new TypeToken<ResponseObject<User>>(){}.getType());
                if (responseObject.getState() == 1) {
                    SharedUtils.setShareUserName(userName);
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),responseObject.getMsg(),Toast.LENGTH_SHORT).show();
                    loginSuccess(userName);
                    Looper.loop();
                } else {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),responseObject.getMsg(),Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }
        });
    }

    //登录成功的时候调用的方法
    private void loginSuccess(String userName) {
        Intent intent = new Intent(MyLoginActivity.this, FragmentMy.class);
        intent.putExtra("user_name",loginName.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }

    //获取随机验证码
    private void setRandomView(TextView checkRandom){
        String allChars = "123456789QWERTYUIOPLKJHGFDSAZXCVBNMqwertyuioplkjhgfdsazxcvbnm";
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<4; i++) {
            int location = new Random().nextInt(allChars.length());
            builder.append(allChars.charAt(location));
        }
        checkRandom.setText(builder.toString());
    }

    private void loginByQQClient() {
        Platform plat = ShareSDK.getPlatform(QQ.NAME);
        //移除授权状态和本地缓存，下次授权会重新授权
        plat.removeAccount(true);
        //SSO授权，传false默认是客户端授权
        plat.SSOSetting(true);
        //授权回调监听，监听oncomplete，onerror，oncancel三种状态
        plat.setPlatformActionListener(this);
        if(plat.isClientValid()){
            //判断是否存在授权凭条的客户端，true是有客户端，false是无
        }
        if(plat.isAuthValid()){
            //判断是否已经存在授权状态，可以根据自己的登录逻辑设置
            Toast.makeText(this, "已经授权过了", Toast.LENGTH_SHORT).show();
            return;
        }
        //抖音登录适配安卓9.0
        ShareSDK.setActivity(this);
        //要数据不要功能，主要体现在不会重复出现授权界面
        plat.showUser(null);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        String userName = platform.getDb().getUserName();
        SharedUtils.setShareUserName(userName);
        loginSuccess(userName);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Toast.makeText(this,"授权失败，请重试",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Toast.makeText(this,"授权已取消",Toast.LENGTH_SHORT).show();
    }
}
