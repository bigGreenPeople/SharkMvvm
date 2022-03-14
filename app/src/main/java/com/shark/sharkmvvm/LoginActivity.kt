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
import com.shark.tools.viewmodel.ScanKeyCodeViewModel
import java.util.concurrent.ConcurrentHashMap


@SuppressLint("NonConstantResourceId")
@SharkActivity(layoutId = R.layout.activity_login)
class LoginActivity : MvvmActivity() {
    lateinit var mDataBinding: ActivityLoginBinding

    @SharkViewModel
    lateinit var loginViewModel: LoginViewModel

    @SharkViewModel
    lateinit var scanKeyCodeViewModel: ScanKeyCodeViewModel

    override fun initView() {
        mDataBinding.loginViewModel = loginViewModel

    }

    /**
     * 获得账套信息更新在界面
     * @param username String
     */
    fun getSOB(username: String) {

    }

//    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
//        if (event!!.keyCode == KeyEvent.KEYCODE_ENTER) return false
//        return super.dispatchKeyEvent(event)
//    }

    fun add(view: View) {
        HeaderInterceptor.addHeader("test_token", "213154")
    }

    /**
     * 登录按钮
     * @param view View
     */
    fun login(view: View) {
//        jumpActivity(ScanKeyCodeActivity::class.java)
//        scanKeyCodeViewModel.deleteAllCodeKey()
        scanKeyCodeViewModel.deleteCodeKey(KeyEvent.KEYCODE_ENTER)

        info("删除成功")
    }


    /**
     * 跳转界面
     * @param view View
     */
    fun jumpSetting(view: View) {
    }

    @ScanEvent(R.id.input_password)
    fun testSelect(msg: String) {
        info("扫描成功:$msg")
    }


}