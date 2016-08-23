# LiteHybrid

A light hybrid framework, works for H5 and native interactions via webview.
轻量级litehybrid框架适合适合大多数 app 的 H5 Native 交互的

代码分两部分，litehybird/ 为框架代码。 example/ 为示例代码

原理：
	
	1. 封装原生 webview，使用 WebViewClient 与 WebChromeClient
	2. H5 调用 Native 采用 js prompt 方式
	3. Native 调用 H5 采用 loadurl API


该框架曝露出来接口：

1. errorview 和 loadingview 可以自定义

2. 对 WebView 设置可以通过 LHPreferences传递，如 agent等

3. 通过INVOKE注解调用相应函数。


###使用方式：

 


其中，H5调用 native，以 JSONObject 方式传递调用信息，格式约定如下：

	{
	  "invoke": "ANNOTATION_METHOD" 
	  "paras": JSONObject
	}
其中，**"invoke"** 值为字符串，对应调用方法，如下文中 **popToast**。**"paras"**  为对应参数信息，参数内容约定为 JSONObject.
	
**“ANNOTATION_METHOD”** 声明如下，参见 WebViewActivity 里的 Invoker1：

	@INVOKE("popToast")
    public void popToast(JSONObject msgObj) {
    ...
    }
    
注意事项：
	
	1. 方法为 public，否则会有 IllegalAccessException
	2. 方法约定一个参数，JSONObject paras，为H5 传递来 paras 值
	3. 方法 @INVOKE("popToast") 注解中的 popToast等字段，对应 H5 传递来的 XXX_METHOD，采用字符串对比方式定位应该调用函数。
