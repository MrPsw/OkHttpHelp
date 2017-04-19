# OkHttpHelp
1.Builder（建造者模式） 封装 OKhttp 
<br />
2.统一消息提示
<br />
3.统一的dalog提示（可配置）       isProgress(true)
<br />
4.添加header（可配置）           header()
<br />
5.发送到Handler处理数据（可配置） sendHandler(mHandler, 1)
<br />
6.可选get post请求方式（可配置） .Get()or.Post()
<br />
7.可添加拦截器                   addInterceptor()
<br />
8.可添加参数（单个or批量）       addParameter()or addParameters()
        
           builder = new OkHttpHelp.OkHttpBuilder()
                        .url("http://www.baidu.com")
                        .isProgress(true)//是否开启通用dalog
                        .addInterceptor()添加拦截器
                        .addParameter()添加参数
                         .header()//添加消息头
                        .sendHandler(mHandler, 1)
                        .Get().build();  
                        
                new OkHttpHelp(builder, MainActivity.this).start();//发送请求
