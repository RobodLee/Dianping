package com.dianping.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dianping.android.MyLoginActivity;
import com.dianping.android.R;
import com.dianping.android.utils.SharedUtils;

import static android.app.Activity.RESULT_OK;

public class FragmentMy extends Fragment {

    TextView loginText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_index,container,false);

        loginText = (TextView) view.findViewById(R.id.my_index_login_text);
        loginText.setText(SharedUtils.getShareUserName());
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyLoginActivity.class);
                startActivityForResult(intent,1);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String userName = data.getStringExtra("user_name");
                    loginText.setText(userName);
                }
                break;
        }
    }
}
