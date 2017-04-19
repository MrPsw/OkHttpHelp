package cn.psw.okhttphelp.Http;

import android.os.Handler;

import java.util.Map;

import okhttp3.Interceptor;


/***
 *写这段代码的时候，只有上帝和我知道它是干嘛的
 *现在，只有上帝知道 
 * Created by Mr.Peng on 2017/4/6.
 */
public interface Builder {
     Builder isProgress(boolean isProgress);
     Builder Get();
     Builder Post();
     Builder url(String url);
     Builder addInterceptor(Interceptor interceptor);
     Builder addParameter(String name, String value);
     Builder sendHandler(Handler mHandler, int what);
     Builder header(String name, String value);
     Builder addParameters(Map<String, String> Parameter);
     Builder build();


}
