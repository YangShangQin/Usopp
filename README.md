# Usopp


## 介绍

[项目地址](https://github.com/iceAnson/Usopp)

Usopp 是一个基于“模块化”开发模式的初始化框架。使用Usopp以后，“模块”的初始化代码无需在主app编写，
甚至可以在主app的运行流程中插入自己的代码，实现完全解耦。

## 特点

-	基于编译时注解，运行效率高
- 	独立模块对主app完全无入侵
-  主app集成仅需一行代码
-  使用@Thread取代new Thread或者线程池，统一初始化线程，避免各个模块自己开线程初始化，节省开销
-  使用@FrameworkAppcaliton ，@AppApplication，@ModuleApplication三个优先级来分别满足底层架构，app，和独立模块之间的运行流程顺序
-  使用@Activity提供给独立模块代码插入时机
-  使用@ AppForground和@ AppBackground来提供前后台事件的代码插入时机
-	可对整个app初始化流程进行监控，如方法耗时，并以此进行优化，梳理启动业务流程




## 集成


### 一、主app

#### 1、在app build.gradle里加入
```java
compile "com.meiyou:usopp:2.0.1"
```

#### 2、在app application的onCreate加入
```java
List<String> list = new ArrayList<>();
list.add("ModuleOneInit");
list.add("ModuleTwoInit");
Usopp.getInstance().init(this,list);
```
其中ModuleOneInit和ModuleTwoInit是和两个独立模块协商的一个标识符字符串

至此，主app集成结束，接下来看独立模块如何在主app的各个运行阶段插入执行代码。
	

### 二、独立模块或者依赖模块

#### 1、在module内的build.gradle里加入
```java
	compile "com.meiyou:usoppcompiler:2.0.1"
```

#### 2、创建一个任意类，加入Usopp注解，并加入混淆keep,如下，可直接阅读第二段注释

```java

@Usopp("ModuleOneInit")//标记此类为Usopp识别类
public class ModuleOneInit {
    private static final String TAG="ModuleOneInit";
    @AppApplication
    public void runAtApplication(){
        Log.d(TAG,"runAtApplication");
    }
    @AppApplication
    @Thread
    public void runAtApplicationAtThread(){
        Log.d(TAG,"runAtApplicationAtThread");
    }
    @AppBackground
    public void runWhenAppBackground(){
        Log.d(TAG,"runWhenAppBackground");
    }
    @AppForground
    public void runWhenAppBacktoFront(){
        Log.d(TAG,"runWhenAppBacktoFront");
    }
    @Activity("com.meiyou.usoppproject.MainActivity")
    public void runWhenAppStartMainActivity(){
        Log.d(TAG,"runWhenAppStartMainActivity");
    }
}

```

	其中
	@Usopp("ModuleOneInit") 表示这是一个Usopp类，将在编译时被框架扫描到。
	
	@AppApplication 表示这个方法将在application onCreate执行
	
	@Thread 表示这个方法将在线程里执行
	
	@AppBackground 表示这个方法将在app回到后台的时候执行
	
	@AppForground 这个方法表示将在app从后台回到前台的时候执行
	
	@Activity("com.meiyou.usoppproject.MainActivity") 这个方法表示将在进入MainActivity的onCreate之后执行
	
	考虑到大部分的项目开发都会自己的公共底层库，且对执行顺序会有一定的要求，Usopp提供注解优先级执行顺序是：
	
	@FrameworkApplicaion >	@AppApplication  > @ModuleApplication > @Activity
		
	其中FrameworkApplicaion会最先执行，其次是AppAplication，依次之。
	

至此，子模块集成结束，接下来看下运行成功	

## Demo演示


#### 1、运行app，在logcat搜索“Module”字符串即可看到如下日志：

```java

11-24 11:52:45.060 20095-20095/com.meiyou.usoppproject D/ModuleTwoInit: runAtApplication
11-24 11:52:45.060 20095-20095/com.meiyou.usoppproject D/ModuleOneInit: runAtApplication
11-24 11:52:45.060 20095-20113/com.meiyou.usoppproject D/ModuleTwoInit: runAtApplicationAtThread
11-24 11:52:45.060 20095-20113/com.meiyou.usoppproject D/ModuleOneInit: runAtApplicationAtThread
11-24 11:52:45.070 20095-20095/com.meiyou.usoppproject D/ModuleTwoInit: runWhenAppStartMainActivity
11-24 11:52:45.070 20095-20095/com.meiyou.usoppproject D/ModuleOneInit: runWhenAppStartMainActivity
11-24 11:52:47.332 20095-20095/com.meiyou.usoppproject D/UsoppTimerCounter: Usopp耗时统计排行：
                                                                            Cost:0=>isThread:false=>com.meiyou.moduletwo.ModuleTwoInit#runAtApplication
                                                                            Cost:0=>isThread:false=>com.meiyou.moduleone.ModuleOneInit#runAtApplication
                                                                            Cost:0=>isThread:true=>com.meiyou.moduletwo.ModuleTwoInit#runAtApplicationAtThread
                                                                            Cost:0=>isThread:true=>com.meiyou.moduleone.ModuleOneInit#runAtApplicationAtThread
                                                                            Cost:0=>isThread:false=>com.meiyou.moduletwo.ModuleTwoInit#runWhenAppStartMainActivity
                                                                            Cost:0=>isThread:false=>com.meiyou.moduleone.ModuleOneInit#runWhenAppStartMainActivity
11-24 11:53:03.988 20095-20095/com.meiyou.usoppproject D/ModuleTwoInit: runWhenAppBackground
11-24 11:53:03.988 20095-20095/com.meiyou.usoppproject D/ModuleOneInit: runWhenAppBackground
11-24 11:53:07.151 20095-20095/com.meiyou.usoppproject D/ModuleTwoInit: runWhenAppBacktoFront
11-24 11:53:07.151 20095-20095/com.meiyou.usoppproject D/ModuleOneInit: runWhenAppBacktoFront


```

我们可以看到，ModuleInitOne和ModuleInitTwo的代码在主app的各个阶段均执行了插入代码；

其中我们新增了个api进行方法统计耗时，只要用于帮助启动耗时的分布分析。

## 框架背景

#### 1、为什么想写这个东西，它解决哪些痛点？
	
随着项目快速迭代以后，我们发现了一个问题，底层模块，独立模块，包括主app本身，在启动这个流程上（从点击icon到进入主页）；各个模块
对主app在各个地方插入了代码，于是出现了几个问题；
	
		1、优化启动耗时 太困难，无法理清逻辑顺序，也无法快速拆除一些逻辑的集成；
		
		2、独立模块的集成代码频繁升级，导致主app一直修改，对于多个主app的公司来说，经常出现抱怨，维护成本极高。
		

而Usopp 将app的运行流程梳理为：

>1、FrameworkApplicaion级初始化 //Framework层，仅限Framework使用

>2、AppApplication级初始化//App层，仅限App使用

>3、ModuleApplication级初始化//Module层，仅限Module使用

>4、Activity级初始化

>5、AppBackground App回到后台

>6、AppForground App回到前台

>7、Before //待定

>8、After //待定
对于以上流程都提供了线程支持；使用@Thread即可；

这样便可以最大程度的减小主app的代码逻辑，清晰又简单；
		

1、模块和主app的耦合关系；模块边界不明确，目前模块初始化代码依然还在主app，还有一些event之类的，且需要关心在哪里进行初始化

2、随着模块的增多，初始化代码臃肿，初始化流程过于凌乱，app就是大集合，出现问题很难进行业务梳理；


#### 2、Usopp还能做什么

1、除了对业务逻辑梳理上有极大的优势以外，Usopp可以在各个阶段进行打点监控，如方法耗时统计，开发者可根据实际项目请
针对不同情况，在测试阶段进行警告

2、参照Usopp的思路，其实activity可以完全按照此种模式拆分为各个业务的集合体，但目前尚未支持，欢迎广大开发者提PR。



## [License Apache-2.0](LICENSE)









	