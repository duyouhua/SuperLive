# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\D\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontoptimize
-dontusemixedcaseclassnames
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-ignorewarnings

-dontwarn
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes InnerClasses,LineNumberTable


-keep public class com.joytouch.superlive.wxapi.WXEntryActivity{*;}

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.auth.AlipaySDK{ public *;}
-keep class com.alipay.sdk.auth.APAuthInfo{ public *;}
-keep class com.alipay.mobilesecuritysdk.*
-keep class com.ut.*

-keep class com.umpay.**{*;}
-keep class com.UCMobile.**{*;}
-keep class com.unionpay.**{*;}
-keep interface com.unionpay.**{*;}
-keep interface com.UCMobile.**{*;}
-keep class com.tenpay.** {*;}
-keep class com.mapabc.** {*;}
-keep class com.amap.** {*;}
-keep class com.google.protobuf.** {*;}
-keep class com.autonavi.** {*;}
-keep class cn.jpush.**{ *; }
-keep class android.support.v4.app.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep class com.baidu.** { *; }
-keep public class com.baidu.** { *; }
-keep class com.umeng.** {*;}
-keep class android.net.http.** {*;}
-keep class sina.sso.** {*;}
-keep class com.sina.sso.** {*;}
-keep class weibo.sdk.android.** {*;}
-keep class com.tencent.weibo.** {*;}
-keep class android.support.v4.** { *;}
-keep interface com.actionbarsherlock.** { *; }
-keep interface android.support.v4.app.** { *; }


-keep public class com.android.vending.licensing.ILicensingService

-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class m.framework.**{*;}


-keep class **.R$* {*;}

-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient
-dontwarn com.umeng.common.**
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient
-dontwarn android.support.v4.**

-dontwarn com.baidu.android.**
-dontwarn com.baidu.**

-dontwarn cn.jpush.**


-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep public class com.lashou.groupurchasing.R$*{
    public static final int *;
}
-keep public class com.umeng.fb.ui.ThreadView {
}

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}



-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**

-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}



-keep public class com.joytouch.superlive.R$*{
    public static final int *;
}

-keepattributes *Annotation*
-keep class com.tencent.mm.sdk.** {
   *;
}