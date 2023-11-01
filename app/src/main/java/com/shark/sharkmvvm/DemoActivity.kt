package com.shark.sharkmvvm

import android.util.Log
import android.view.View
import com.shark.mvvm.activity.MvvmActivity
import com.shark.mvvm.activity.SharkActivity
import com.shark.mvvm.event.CleanModel
import com.shark.mvvm.event.ScanEvent
import com.shark.mvvm.viewmodel.SharkViewModel
import com.shark.sharkmvvm.databinding.ActivityDemoBinding
import com.shark.sharkmvvm.viewmodel.DemoViewModel

@SharkActivity
class DemoActivity : MvvmActivity() {
    lateinit var mDataBinding: ActivityDemoBinding

    @SharkViewModel
    lateinit var viewModel: DemoViewModel

    override fun initView() {
        Log.i(TAG, "DemoActiv ity init")

        mDataBinding.viewModel = viewModel
    }

    /**
     * 在注解中指定你需要监听文本框的id
     * @param code String 这个参数必须实现它就是您设备所扫描到的文本,你不必在方法中对它进行非空判断因为在空的情况下不会调用此方法
     */
    @ScanEvent(id = R.id.et_scan)
    fun scanCode(code: String) {
        //TODO 你的业务操作
        Log.i(TAG, "et_scan: $code")
    }

    @ScanEvent(code = 523)
    fun scan(code: String) {
        Log.i(TAG, "scan: $code")
    }

    @ScanEvent(id = R.id.et_scan2, clean = CleanModel.ALWAYS, code = 523)
    fun scanCode2(code: String) {
        //TODO 你的业务操作
        Log.i(TAG, "et_scan2: $code")
    }


    fun successCall(view: View) {
        viewModel.test("0")
    }

    fun errorCall(view: View) {
        viewModel.test("1")
    }

    fun jumpPage(view: View) {
        jumpActivity(LoginActivity::class.java)
    }


}