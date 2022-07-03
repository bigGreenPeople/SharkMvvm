---
description: >-
  SharkMvvm中推荐使用DataBinding来构建一个Activity，与正常的Activity绑定xml不同的是去除了很多重复的操作，您只需声明好你的DataBinding变量，上层架构会自动帮您完成初始化的操作。
---

# Activity

#### 编写xml布局文件

xml与正常的DataBinding编写没有任何区别

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
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="SharkMvvm"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />
    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

#### 创建Activity类

SharkMvvm推荐您继承MvvnActivity，并使用@SharkActivity注解来标记你创建的类。在类中您应该声明你创建的xml对应的DataBinding类。当Activity创建时将自动为你声明的DataBinding变量赋值。

在SharkMvvm中非必要的情况下不推荐重写OnCreate方法，对应Activity的一些初始化操作您可以重写initView方法，在此方法中完成操作

{% hint style="info" %}
@SharkActivity标记类看上去似乎没有任何作用，但是这里还是建议您使用他进行标记，因为后续版本的组件化开发和标题等声明我们都会在这个注解上实现
{% endhint %}

```kotlin
@SharkActivity
class DemoActivity : MvvmActivity() {
    lateinit var mDataBinding: ActivityDemoBinding

    override fun initView() {
        Log.i(TAG, "DemoActivity init")
    }
}
```

在AndroidManifest.xml文件中声明你的Activity类，运行即可看你编写的界面，这样一个最简单的模版就完成了。
