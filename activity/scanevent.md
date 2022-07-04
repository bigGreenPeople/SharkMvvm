---
description: >-
  对于一些物联网设备要用到的扫描事件我们也做了支持，在原生的Android中您需要重写dispatchKeyEvent方法来监听扫描事件。在SharkMvvm中这些繁琐的操作都被简化了。
---

# ScanEvent

### 布局文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:bind="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/main"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:id="@+id/textView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="SharkMvvm"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <EditText
            android:id="@+id/et_scan"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            app:layout_constraintBottom_toTopOf="@+id/textView"
            app:layout_constraintTop_toTopOf="parent" />
    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

### Activity

在您布局文件对应的Activity中添加以下方法

```kotlin
/**
* 在注解中指定你需要监听文本框的id
* @param code String 这个参数必须实现它就是您设备所扫描到的文本,你不必在方法中对它进行非空判断因为在空的情况下不会调用此方法
*/
@ScanEvent(id = R.id.et_scan)
fun scanCode(code: String) {
   //TODO 你的业务操作
   Log.i(TAG, code)
}
```

### 自定义添加扫描Code

因为不同厂家的设备甚至同一厂家的不同型号设备的扫描Code定义都不一样，默认情况下扫描事件的code只有我所使用的设备对应的code。当您的设备的扫描code不在我默认的code中时您需要手动添加您设备的扫描code

#### 默认扫描定义

```kotlin
/*
 * 扫描配置
 */
object ScanConfig {
    //屏蔽事件
    val closeSystemKeySet: MutableSet<Int> = mutableSetOf(
        KeyEvent.KEYCODE_ENTER
    )

    //存储所有定义的扫描按键
    val keySet: MutableSet<Int> = mutableSetOf(
        288, 289, 285, 286,
        305,
        KeyEvent.KEYCODE_UNKNOWN, KeyEvent.KEYCODE_ENTER
    )

```

#### 添加您的扫描code

建议您在项目Application初始化的时候添加扫描code，这样它就会作用于您整个项目

```kotlin
//将scanCode.keyCode替换成您的设备的code
ScanConfig.keySet.add(scanCode.keyCode)
```

{% hint style="info" %}
在代码中添加这类代码似乎显得过于笨拙，因为我们的app可能运行在各种未知的设备上。

所以在SharkMvvm的另一个模块tools中有相应的初始化设备的界面。

您只需在合适的时候进入到我们所提供的初始化界面中，在界面中用户完成与设备的互动操作即可初始化设备。(在后续的文档中会有相应的使用说明)
{% endhint %}
