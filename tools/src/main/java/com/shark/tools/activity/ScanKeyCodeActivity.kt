package com.shark.tools.activity

import android.view.KeyEvent
import android.view.View
import com.shark.mvvm.activity.MvvmActivity
import com.shark.mvvm.utils.schedule
import com.shark.mvvm.viewmodel.SharkViewModel
import com.shark.tools.R
import com.shark.tools.databinding.ActivityScanKeyCodeBinding
import com.shark.tools.viewmodel.ScanKeyCodeViewModel
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ScanKeyCodeActivity : MvvmActivity() {
    lateinit var mDataBinding: ActivityScanKeyCodeBinding

    @SharkViewModel
    lateinit var viewModel: ScanKeyCodeViewModel

    //这个用来存储按下并弹起的按键 key为ACTION_DOWN val为ACTION_UP
    private val keyDownUpMap: ConcurrentHashMap<Int, Int> = ConcurrentHashMap()

    init {
        initSharkActivity(R.layout.activity_scan_key_code)
    }

    lateinit var timer: Timer
    override fun initView() {
        timer = schedule(period = 300) {
            checkScanKey()
        }
        mDataBinding.viewModel = viewModel
    }

    fun add(view: View) {
        reset(view)
        timer = schedule(period = 300) {
            checkScanKey()
        }
    }

    fun next(view: View) {

        finish()
    }

    /**
     * 确认添加扫描按键
     * @param view View
     */
    fun confirm(view: View) {
        val filter = keyDownUpMap.filter { keyDownUp -> keyDownUp.value != -1 }
        if (filter.size != 1) return

        timer.cancel()

        val keyCode = filter.values.toMutableList()[0]
        viewModel.addCodeKey(keyCode)
        mDataBinding.tvMessage.text = "添加${keyCode}成功"

        keyDownUpMap.clear()
        viewModel.status.value = 3
    }

    fun reset(view: View) {
        keyDownUpMap.clear()
        mDataBinding.tvMessage.text = "请按下扫描按键"
        viewModel.status.value = 0
    }

    private fun checkScanKey() {
        val filter = keyDownUpMap.filter { keyDownUp -> keyDownUp.value != -1 }
        if (filter.isEmpty()) {
            runOnUiThread {
                mDataBinding.tvMessage.text = "请按下扫描按键"
                viewModel.status.value = 0
            }
            return
        }
        if (filter.size > 1) {
            runOnUiThread {
                mDataBinding.tvMessage.text = "检测到扫描按键有多个,无法确定扫描按键"
                viewModel.status.value = 1
            }
            return
        }

        runOnUiThread {
            mDataBinding.tvMessage.text = "检测成功! 扫描按键为:${filter.values.toMutableList()[0]}"
            viewModel.status.value = 2
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