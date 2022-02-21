package com.shark.sharkmvvm

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.View
import com.shark.mvvm.activity.MvvmActivity
import com.shark.mvvm.activity.SharkActivity
import com.shark.mvvm.event.ScanEvent
import com.shark.mvvm.viewmodel.SharkViewModel
import com.shark.sharkmvvm.databinding.ActivityLoginBinding
import com.shark.mvvm.retrofit.interceptor.HeaderInterceptor
import com.shark.mvvm.utils.schedule
import com.shark.sharkmvvm.viewmodel.LoginViewModel
import com.shark.tools.activity.ScanKeyCodeActivity
import java.util.concurrent.ConcurrentHashMap


@SuppressLint("NonConstantResourceId")
@SharkActivity(layoutId = R.layout.activity_login)
class LoginActivity : MvvmActivity() {
    lateinit var mDataBinding: ActivityLoginBinding

    //这个用来存储按下并弹起的按键 key为ACTION_DOWN val为ACTION_UP
    private val keyDownUpMap: ConcurrentHashMap<Int, Int> = ConcurrentHashMap()

    @SharkViewModel
    lateinit var loginViewModel: LoginViewModel

    override fun initView() {
        mDataBinding.loginViewModel = loginViewModel

        //监听失去编辑框事件
        schedule(period = 300) {
            checkScanKey()
        }
    }

    /**
     * 获得账套信息更新在界面
     * @param username String
     */
    fun getSOB(username: String) {

    }

    fun add(view: View) {
        HeaderInterceptor.addHeader("test_token", "213154")
    }

    /**
     * 登录按钮
     * @param view View
     */
    fun login(view: View) {
        jumpActivity(ScanKeyCodeActivity::class.java)
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

    /**
     * 跳转界面
     * @param view View
     */
    fun jumpSetting(view: View) {
    }

    @ScanEvent(R.id.input_password)
    fun testSelect(msg: String) {

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