package com.xhl.apt;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.late.apt.annotations.WXActvityGenerator;
import com.late.apt.lib.WXTemplateActivity;
@WXActvityGenerator(
        getPackageName = "com.xhl.apt",
        getSupperClass = WXTemplateActivity.class
)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
