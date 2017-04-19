package cn.psw.okhttphelp.Http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.utils.*;
import com.blankj.utilcode.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpHelp extends Thread {

    Context context;//上下文
    private Call call;//回调
    private static OkHttpClient okhttp; //okhttp请求
    private ProgressDialog progressDialog;  //进度提示
    private final OkHttpBuilder tmsBuilder;  //请求建造者

    //提示
   private static int C00x1=1001; //网络不可用
    private static int C00x2=1002; //连接服务器超时

    private static int C00x3=1003; //显示进度提示
    private static int C00x4=1004; //关闭显示
    private static int C00x5=1005; //请求配置or参数异常

    public OkHttpHelp(Builder tmsBuilder, Context mContext) {
        // TODO Auto-generated constructor stub
        this.tmsBuilder=(OkHttpBuilder) tmsBuilder;
        this.context=mContext;


    }



    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        try {
            if(NetworkUtils.isConnected()){
            H.sendEmptyMessage(C00x3);
            call = okhttp.newCall(tmsBuilder.request.build());
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    H.sendEmptyMessage(C00x4);
                    H.sendEmptyMessage(C00x2);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    H.sendEmptyMessage(C00x4);
                    try {

                        if (response.isSuccessful()) {
                            String text = response.body().string();
                            Message msg = new Message();
                            msg.what = tmsBuilder.what;
                            msg.obj = text;
                            tmsBuilder.mHandler.sendMessage(msg);

                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            });
            }else{
                H.sendEmptyMessage(C00x1);
            }
        } catch (Exception e) {
            H.sendEmptyMessage(C00x4);
            H.sendEmptyMessage(C00x5);
            e.printStackTrace();

        }


    }


    /**
     * 内部处理
     */
    Handler H = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1001:
                    Toast.makeText(context, "网络未连接", Toast.LENGTH_LONG).show();
                    break;

                case 1002:
                    Toast.makeText(context, "请求超时，请检查网络", Toast.LENGTH_LONG).show();
                    break;
                case 1003:
                    showDloag();
                    break;
                case 1004:
                    dismissDloag();
                    break;
                case 1005:
                    Toast.makeText(context, "无法发出异常请求", Toast.LENGTH_LONG).show();
                    break;
            }


        }

        ;
    };

    public void cancel() {
        call.cancel();
    }


    /**
     * 构造者
     */
    public static class OkHttpBuilder implements Builder {
        //是否显示统一Progress提示
        public  boolean  isProgress=false;
        //请求方式
        int HttpType;
        //请求URl
        String url;
        //拦截器
        List<Interceptor> interceptor=new ArrayList<>();
        //参数集
        Map<String,String> Parameter=new HashMap<>();
        //header集
        Map<String,String> headers=new HashMap<>();
        public  Handler mHandler;
        public int what;

        public Request.Builder request;
         String URL;

        private void Init() {
            OkHttpClient.Builder build = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS);

            if(interceptor!=null){
                for(int i=0;i<interceptor.size();i++){
                    build.addInterceptor(interceptor.get(i));
                }
            }
            okhttp=build.build();

        }

        @Override
        public Builder isProgress(boolean isProgress) {
            this.isProgress=isProgress;
            return this;
        }

        @Override
        public Builder Get() {
            HttpType=1;
            return this;
        }

        @Override
        public Builder Post() {
            HttpType=2;
            return this;
        }

        @Override
        public Builder url(String url) {
            if(url!=null) {
                this.URL = url;
            }
            return this;
        }

        @Override
        public Builder addInterceptor(Interceptor interceptor) {
            if(interceptor!=null) {
                this.interceptor.add(interceptor);
            }
            return this;
        }

        @Override
        public Builder addParameter(String name, String value) {
            if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(value)) {
                this.Parameter.put(name, value);
            }
            return this;
        }


        @Override
        public Builder sendHandler(Handler mHandler, int what) {
            if(mHandler!=null&&what!=-1) {
                this.mHandler = mHandler;
                this.what = what;
            }
            return this;
        }

        @Override
        public Builder header(String name, String value) {
            if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(value)) {
                headers.put(name, value);
            }
            return this;
        }

        @Override
        public Builder addParameters(Map<String, String> Parameter) {
            if(Parameter!=null){
                this.Parameter.putAll(Parameter);
            }
            return this;
        }


        @Override
        public OkHttpBuilder build() {
            Init();
            try {
                request = new Request.Builder();
                RequestBody requestBody;
                if(HttpType==2){
                    if (Parameter != null) {
                        request.post( getRequestBody(Parameter));
                    }
                }else if(HttpType==1){
                    if (Parameter != null) {
                        List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(Parameter.entrySet());
                        for(int i=0;i<list.size();i++){
                            Map.Entry<String, String> base = list.get(i);
                            if(i==0){
                                URL+="?"+base.getKey()+"="+base.getValue();
                            }else{
                                URL+="&"+base.getKey()+"="+base.getValue();
                            }
                        }
                    }

                    request.get();
                    }else{

                }

                request.url(URL);

                if(headers!=null){
                    List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(headers.entrySet());
                    for(int i=0;i<list.size();i++){
                        Map.Entry<String, String> base = list.get(i);
                      request.addHeader(base.getKey(),base.getValue());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        /**
         * 将Map集合转换成RequestBody
         * @param map
         * @return
         */
        public static RequestBody getRequestBody(Map<String, String> map) {
            FormBody.Builder bt = new FormBody.Builder();
            new FormBody.Builder();
            Set set = map.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry mapentry = (Map.Entry) iterator.next();
                bt.add(mapentry.getKey().toString(), mapentry.getValue().toString());
                System.out.println(mapentry.getKey() + "/" + mapentry.getValue());
            }
            return bt.build();
        }
    }


    /**
     * 显示dalog
     */
    public void showDloag() {
        if (tmsBuilder.isProgress) {
            progressDialog = progressDialog == null ? new ProgressDialog(context) : progressDialog;
            progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode== KeyEvent.KEYCODE_BACK) {
//                    return true;
//                }
//                return false;
//            }
//        });
            progressDialog.setMessage("请稍后。。。");
            progressDialog.show();
        }
    }

    /**
     * 关闭dalog
     */
    public void dismissDloag() {
        if (null != progressDialog) {
            progressDialog.cancel();
        }
    }

}
