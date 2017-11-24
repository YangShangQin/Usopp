## Usopp版本

### 版本说明：
####   1.0.10版本
		优化日志打印

####   1.0.9版本
		优化逻辑

####   1.0.7版本
		解决类重复问题
		Usopp.init方法新增参数
		集成步骤有更新

####   1.0.6版本
		新增方法耗时统计排行
####   1.0.5版本

   	compile "com.meiyou:usopp:1.0.5"
    升级内容：
        优化日志打印
        优化Thread线程处理
        过滤不必要的方法处理

####   1.0.1版本
		创建版本


## Usopp介绍

### 一、这是个App初始化框架，主要用于解决几个问题：

1、模块和主app的耦合关系；模块边界不明确，目前模块初始化代码依然还在主app，还有一些event之类的，且需要关心在哪里进行初始化

2、随着模块的增多，初始化代码臃肿，初始化流程过于凌乱，app就是大集合，出现问题很难进行业务梳理；

### 二、App运行流程
Usopp 将app的运行流程梳理为：

>1、FrameworkApplicaion级初始化 //Framework层，仅限Framework使用

>2、AppApplication级初始化//App层，仅限App使用

>3、ModuleApplication级初始化//Module层，仅限Module使用

>4、Activity级初始化

>5、AppBackground App回到后台

>6、AppForground App回到前台

>7、Before //待定

>8、After //待定
对于以上流程都提供了线程支持；使用@Thread即可；

任意方可以自己模块里根据情况加入自己需要处理动作而不产生耦合；

目前仅对所有动作进行了时间监控；后续可根据具体情况对功能进行新增

#### 这里需要注意的是：

若ModuleApplication的初始化依赖了AppApplication,则要求一定要app先实现usopp,否则可能会出现问题；

所以module在设计初始化的时候，尽量不要依赖app,不然就不灵活了。

## 使用

### App方

#### 1、在app build.gradle里加入compile "com.meiyou:usopp:1.0.7"
		这一步目前集成在Framwork，所以暂时可以不用。

#### 2、在app任意位置创建一个类，并在混淆加入keep,比如：


```java
package com.meiyou.usoppproject;

import com.meiyou.usopp.annotations.Activity;
import com.meiyou.usopp.annotations.AppApplication;
import com.meiyou.usopp.annotations.AppBackground;
import com.meiyou.usopp.annotations.AppForground;
import com.meiyou.usopp.annotations.Thread;
import com.meiyou.usopp.annotations.Usopp;

/**
 *
 * Author: lwh
 * Date: 17/8/16 15:49.
 */
@Usopp("com.lingan.seeyou")//内容请填写本模块包名
public class AppInit {


   /* @ModuleApplication
    @FrameworkApplication
    @Activity
    @Thread
    @AppBackground
    @AppForground*/

    /**
     * App在Application UI线程初始化
     */
    @AppApplication
    public void init(){

    }

    /**
     * App在Application 线程初始化
     */

    @AppApplication
    @Thread
    public void init2(){

    }


    /**
     * App退到后台
     * 如：按Home键
     * 这里也可以加 @Thread来进行线程处理
     */
    @AppBackground
    public void init22(){

    }

    /**
     * App在从后台回到前台
     * 这里从后台回到前台不包括杀进程重启的情况，只适合按home键或者被其他第三方app覆盖的情况
     * 这里也可以加 @Thread来进行线程处理
     */
    @AppForground
    public void init23(){

    }

    /**
     * App进入com.meiyou.usoppproject.MainActivity时需要做什么
     * 这里也可以加 @Thread来进行线程处理
     */
    @Activity("com.meiyou.usoppproject.MainActivity")
    public void init233(){

    }
}


```

这样之后，Usopp就会在编译的时候识别到这个类，并在运行的时候根据这个类的的注解按顺序执行这个类里的方法，从而完成初始化的工作；



### Module方-也就是独立模块


#### 1、在build.gradle里加入compile "com.meiyou:usopp:1.0.7"
		这一步目前集成在Framwork，所以暂时可以不用。

#### 2、在module的任意位置创建一个类，并在混淆加入keep,比如：


```java
package com.meiyou.usoppproject;

import com.meiyou.usopp.annotations.Activity;
import com.meiyou.usopp.annotations.AppApplication;
import com.meiyou.usopp.annotations.AppBackground;
import com.meiyou.usopp.annotations.AppForground;
import com.meiyou.usopp.annotations.ModuleApplication;
import com.meiyou.usopp.annotations.Thread;
import com.meiyou.usopp.annotations.Usopp;

/**
 * Author: lwh
 * Date: 17/8/16 16:21.
 */
@Usopp("com.lingan.community")//内容请填写本模块包名
public class ModuleInit {


    /**
     * Module在Application UI线程初始化
     */
    @ModuleApplication
    public void init(){

    }

    /**
     * Module在Application 线程初始化
     */

    @AppApplication
    @Thread
    public void init2(){

    }


    /**
     * App退到后台
     * 如：按Home键
     * 这里也可以加 @Thread来进行线程处理
     */
    @AppBackground
    public void init22(){

    }

    /**
     * App在从后台回到前台
     * 这里从后台回到前台不包括杀进程重启的情况，只适合按home键或者被其他第三方app覆盖的情况
     * 这里也可以加 @Thread来进行线程处理
     */
    @AppForground
    public void init23(){

    }

    /**
     * App进入com.meiyou.usoppproject.MainActivity时需要做什么
     * 这里也可以加 @Thread来进行线程处理
     */
    @Activity("com.meiyou.usoppproject.MainActivity")
    public void init233(){

    }

}



```

这样之后，Usopp就会在编译的时候识别到这个类，并在运行的时候根据这个类的的注解按顺序执行这个类里的方法，从而完成初始化的工作；

### 最后集成

需要在application里加入 Usopp.getInstance().init(List<String> moduleName);（这一步已经集成在Framework）；

只需要在主app的application(必须集成LinganApplcation或者LinganApplicationTinker)；
重写getUsoppList()；

把之前声明的Usopp的注解名都加进去即可；如果不加进去，则无法初始化生效；
（这一步是为了解决类重复和路径寻找的问题）



## 优点

### 业务梳理

	

### 耗时监控和异常监控

目前只是简单的打印：
初始化时间；
执行方法体耗时；

监控后续可以慢慢加入；


```java
08-21 08:59:56.661 16465-16465/? D/Usopp: Usopp init cost time:9
08-21 08:59:56.661 16465-16465/? D/Usopp: => fire method cost time:0
08-21 08:59:56.662 16465-16465/? D/Usopp: =>invoke method：com.meiyou.frameworkapp.usopptest.ApplicationModule:init
08-21 08:59:56.662 16465-16465/? D/Usopp: =>invoke method：com.meiyou.frameworkapp.usopptest.ApplicationModule:init4
08-21 08:59:56.662 16465-16465/? D/Usopp: => fire method cost time:1
08-21 08:59:56.662 16465-16465/? D/Usopp: => fire method cost time:0
08-21 08:59:56.875 16465-16465/? D/Usopp: =>invoke method：com.meiyou.frameworkapp.usopptest.ApplicationModule:init2
08-21 08:59:56.875 16465-16465/? D/Usopp: =>invoke method：com.meiyou.frameworkapp.usopptest.ApplicationModule:init3
08-21 08:59:56.875 16465-16465/? D/Usopp: => fire method cost time:0
08-21 08:59:56.910 16465-16465/? D/Usopp: =>invoke method：com.meiyou.frameworkapp.usopptest.ApplicationModule:init7
08-21 08:59:56.910 16465-16465/? D/Usopp: =>invoke method：com.meiyou.frameworkapp.usopptest.ApplicationModule:init8
08-21 08:59:56.910 16465-16465/? D/Usopp: => fire method cost time:0

```




	