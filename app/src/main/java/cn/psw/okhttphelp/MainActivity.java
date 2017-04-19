package cn.psw.okhttphelp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;

import cn.psw.okhttphelp.Http.Builder;
import cn.psw.okhttphelp.Http.OkHttpHelp;

public class MainActivity extends AppCompatActivity {

    private Builder builder;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1);
        ((Button)findViewById(R.id.send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new OkHttpHelp.OkHttpBuilder()
                        .url("http://www.baidu.com")
                        .isProgress(true)//是否开启通用dalog
//                .addInterceptor()添加拦截器
//                .addParameter()添加参数
//                .header()//添加消息头
                        .sendHandler(mHandler, 1)
                        .Get().build();
                new OkHttpHelp(builder, MainActivity.this).start();
            }
        });


    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv1.setText((String) msg.obj + "");
                    ToastUtils.showShortToast((String) msg.obj + "");
                    break;
            }
            ;
        }
    };
}
