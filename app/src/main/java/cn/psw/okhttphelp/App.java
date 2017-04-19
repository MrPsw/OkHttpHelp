package cn.psw.okhttphelp;

import android.app.Application;

import com.blankj.utilcode.utils.Utils;

/***
 *写这段代码的时候，只有上帝和我知道它是干嘛的
 *现在，只有上帝知道 
 * Created by Mr.Peng on 2017/4/19.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
