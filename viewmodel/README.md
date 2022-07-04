---
description: >-
  ViewModel:
  负责完成View与Model间的交互，负责业务逻辑。对应页面上数据您都应该在ViewModel中声明。并且在ViewModel做网络请求数据的操作。在SharkMvvm我们已经简化了这一系列的复杂操作。
---

# ViewModel

### 1.创建ViewModel

在创建的ViewModel中你需要继承BaseViewModel,并在其中定义好你在View在要显示的数据

```kotlin
class DemoViewModel(application: Application) : BaseViewModel(application) {

    val dataString: MutableLiveData<String> = MutableLiveData()

    init {
        //初始化数据
        dataString.value = "SharkChilli"
    }
}
```

### 2.布局文件中使用ViewModel

xml文件中使用ViewModel与DataBinding没有任何区别

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:bind="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="viewModel"
            type="com.shark.sharkmvvm.viewmodel.DemoViewModel" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/main"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:id="@+id/textView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{viewModel.dataString}"
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

### 3.Activity中创建ViewModel并与DataBinding关联

在您声明好ViewModel变量后，你只需使用@SharkViewModel注解标记他，上层架构会自动帮您初始化他，接下来在initView中将它与DataBinding关联即可

```kotlin
@SharkActivity
class DemoActivity : MvvmActivity() {
    lateinit var mDataBinding: ActivityDemoBinding

    @SharkViewModel
    lateinit var viewModel: DemoViewModel

    override fun initView() {
        Log.i(TAG, "DemoActivity init")

        mDataBinding.viewModel = viewModel
    }
}
```
