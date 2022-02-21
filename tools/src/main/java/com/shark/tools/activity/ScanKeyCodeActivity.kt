package com.shark.tools.activity

import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.shark.mvvm.activity.MvvmActivity
import com.shark.mvvm.service.Service
import com.shark.mvvm.utils.schedule
import com.shark.tools.R
import com.shark.tools.databinding.ActivityScanKeyCodeBinding
import com.shark.tools.viewmodel.ScanKeyCodeViewModel
import java.util.concurrent.ConcurrentHashMap

class ScanKeyCodeActivity : MvvmActivity() {
    lateinit var mDataBinding: ActivityScanKeyCodeBinding

    @Service
    lateinit var viewModel: ScanKeyCodeViewModel

    //这个用来存储按下并弹起的按键 key为ACTION_DOWN val为ACTION_UP
    private val keyDownUpMap: ConcurrentHashMap<Int, Int> = ConcurrentHashMap()

    init {
        initSharkActivity(R.layout.activity_scan_key_code)
    }


    override fun initView() {
        schedule(period = 300) {
            checkScanKey()
        }
        mDataBinding.viewModel = viewModel
    }

    fun confirm(view: View) {

    }

    fun reset(view: View) {
        keyDownUpMap.clear()
    }

    private fun checkScanKey() {
        val filter = keyDownUpMap.filter { keyDownUp -> keyDownUp.value != -1 }
        if (filter.isEmpty()) {
            runOnUiThread {
                mDataBinding.tvMessage.text = "请按下扫描按键"
            }
            return
        }
        if (filter.size > 1) {
            runOnUiThread {
                mDataBinding.tvMessage.text = "检测到扫描按键有多个,无法确定扫描按键"
            }
            return
        }

        runOnUiThread {
            mDataBinding.tvMessage.text = "检测成功! 扫描按键为:${filter.values.toMutableList()[0]}"
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event == null) return super.dispatchKeyEvent(event)

        if (event.action == KeyEvent.ACTION_DOWN) {
            keyDownUpMap[event.keyCode] = -1
        } else if (event.action == KeyEvent.ACTION_UP && keyDownUpMap.containsKey(event.keyCode)) {
            keyDownUpMap[event.keyCode] = event.keyCode
        }
        return super.dispatchKeyEvent(event)
    }
}