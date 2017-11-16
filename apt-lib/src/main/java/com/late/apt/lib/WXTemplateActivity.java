package com.late.apt.lib;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
/**
 * Created by xuhongliang on 2017/11/15.
 */
public class WXTemplateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //执行登录回调逻辑，包括请求token,获取openid,回给真实项目模块
    }
}
