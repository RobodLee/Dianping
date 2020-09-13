package com.dianping.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dianping.android.constants.Constants;
import com.dianping.android.enity.ResponseObject;
import com.dianping.android.enity.User;
import com.dianping.android.utils.HttpUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mob.MobSDK;

import java.io.IOException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyRegisterAcitvity extends AppCompatActivity implements View.OnClickListener{

    private ImageView registerBack;
    private EditText phoneNumber;
    private Button checkPassBtn;
    private EditText checkPass;
    private EditText userPass;
    private Button register;
    private CountTime countTime = new CountTime(5000, 1000);

    private boolean checkPassIsRight = false;   //用于判断验证码是否是正确的

    EventHandler eventHandler;

    private static final String TAG = "MyRegisterAcitvity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_register);

        init();

        MobSDK.init(this);
    }

    private void init() {
        //返回按钮
        registerBack = (ImageView) findViewById(R.id.register_back);
        registerBack.setOnClickListener(this);
        //电话号码输入框
        phoneNumber = (EditText) findViewById(R.id.register_phone);
        //发送验证码按钮
        checkPassBtn = (Button) findViewById(R.id.register_get_check_pass);
        checkPassBtn.setOnClickListener(this);
        //验证码输入框
        checkPass = (EditText) findViewById(R.id.register_check_upass);
        //密码输入框
        userPass = (EditText) findViewById(R.id.register_upass);
        //注册按钮
        register = (Button) findViewById(R.id.register_btn);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_back:    //返回按钮
                finish();
                break;
            case R.id.register_get_check_pass:      //获取验证码
                sendSMSRandom();
                countTime.start();
                break;
            case R.id.register_btn:
                // 提交验证码，其中的code表示验证码，如“1357”
                registerUser();
                SMSSDK.submitVerificationCode("86", phoneNumber.getText().toString(), checkPass.getText().toString());
                break;
            default:break;
        }
    }
    //发送验证码
    private void sendSMSRandom() {
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                new Handler(Looper.getMainLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        int event = msg.arg1;
                        int result = msg.arg2;
                        Object data = msg.obj;
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理成功得到验证码的结果
                                // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                                Toast.makeText(getApplicationContext(),"验证码已发送",Toast.LENGTH_SHORT).show();
                            } else {
                                // TODO 处理错误的结果
                                ((Throwable) data).printStackTrace();
                            }
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理验证码验证通过的结果
                                checkPassIsRight = true;
                                if (checkPassIsRight) {
                                    registerUser();
                                }
                            } else {
                                // TODO 处理错误的结果
                                if (!checkPassIsRight) {
                                    Toast.makeText(getApplicationContext(),"验证码错误",Toast.LENGTH_SHORT).show();
                                }
                                ((Throwable) data).printStackTrace();
                            }
                        }
                        // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                        return false;
                    }
                }).sendMessage(msg);
            }
        };
        // 注册一个事件回调，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler);

        // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
        SMSSDK.getVerificationCode("86", phoneNumber.getText().toString());

        Log.d(TAG,phoneNumber.getText().toString());
    }

    private void registerUser() {
        String phone = phoneNumber.getText().toString();
        String pass = userPass.getText().toString();
        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(pass)) {
            Toast.makeText(this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
        } else {
            HttpUtil.SendOkHttpRequest(Constants.USER_REGISTER + "&username=" + phone + "&password=" + pass, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseObject<User> responseObject = new GsonBuilder().create().fromJson(response.body().string(),new TypeToken<ResponseObject<User>>(){}.getType());
                    if (responseObject.getState() == 1) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MyRegisterAcitvity.this,MyLoginActivity.class);
                        startActivity(intent);
                        finish();
                        Looper.loop();
                    }
                }
            });
        }
    }

    // 使用完EventHandler需注销，否则可能出现内存泄漏
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    private class CountTime extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CountTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            checkPassBtn.setText(millisUntilFinished/1000 + "s后重新获取");
            checkPassBtn.setClickable(false);
        }

        @Override
        public void onFinish() {
            checkPassBtn.setText("获取验证码");
            checkPassBtn.setClickable(true);
        }
    }
}
