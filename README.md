### 轻量级 H5 Native Hybrid 框架
轻量级litehybrid框架适合适合大多数 app 的 H5 Native 交互的，交互原理较为简单。
GIT 地址：[LiteHybrid](https://github.com/luoruiyi/LiteHybrid)。其代码分两部分，litehybird/ 为框架代码。 example/ 为示例代码，


####原理：
	
	1. 封装原生 webview，使用 WebViewClient 与 WebChromeClient
	2. H5 调用 Native 采用 js prompt 方式
	3. Native 调用 H5 采用 loadurl API


####使用说明：

1. errorview 和 loadingview 可以自定义

2. 对 WebView 设置可以通过 LHPreferences传递，如 agent等

3. 通过INVOKE注解，来使H5调用相应Native函数，参见INVOKE类，结构合理。


####使用方式：


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



####webview 使用经验


#####H5页面加载过程中的状态处理
webview 通过WebViewClient 处理H5页面加载过程中的状态，页面加载时候, WebviewClient 给出的回调调用顺序不同，通常使用的三种回调分析如下：

正常情况下 
onPageStarted -> onPageFinished

失败情况下:
onPageStarted -> onReceivedError -> onPageFinished

超时情况下:
onPageStarted -> 30s-> onReceivedError -> onPageFinished 

在**加载过程**中重试:
onPageStarted -> 重试-> onPageFinished -> onPageStarted

#####H5内JS的回调处理
webview 通过 WebChromeClient处理 H5 内js的回调，本框架使用了onJsPrompt方式，其他还有alert方式等。



#####其他

1. webview 在被销毁后，litehybrid的 LHWebView 中有标志位标志进行判断，因为webview销毁后并不立即为空，再调用可能会引发异常。此处同Android SDK中的的 webviewfragment处理一致。

2. 提前超时：提前超时时，使用了 loadurl("about:blank") 进行清除缓存，但loadurl("about:blank") 会重新执行 webview 的生命周期，LHWebViewClient 有标志位进行处理。

3. 账户信息应当采用诸如cookie方式注入提取，example中未体现。

4. webview onPagefinished 后，js 不一定完全加载成功。最好方式是用JS的回调通知native调用H5内js时机准备好。


Please read the fucking source code， 不好的地方指出来相互学习