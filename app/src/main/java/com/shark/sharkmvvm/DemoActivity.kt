package com.shark.sharkmvvm

import android.util.Log
import com.shark.mvvm.activity.MvvmActivity
import com.shark.mvvm.activity.SharkActivity
import com.shark.sharkmvvm.databinding.ActivityDemoBinding

@SharkActivity
class DemoActivity : MvvmActivity() {
    lateinit var mDataBinding: ActivityDemoBinding

    override fun initView() {
        Log.i(TAG, "DemoActivity init")
    }
}