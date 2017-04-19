package cn.psw.okhttphelp.Http;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by Mr.peng on 2016/11/10.
 */

public class RequestBodyHelp {
    /**
     * 将Map集合转换成RequestBody
     * @param map
     * @return
     */
    public static RequestBody BoayMap(Map<String, String> map) {
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
