package com.shark.sharkmvvm

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import com.shark.mvvm.activity.KeyBoardMove
import com.shark.mvvm.activity.MvvmActivity
import com.shark.mvvm.activity.SharkActivity
import com.shark.mvvm.event.ScanEvent
import com.shark.mvvm.spread.isEmpty
import com.shark.mvvm.utils.setTimeout
import com.shark.mvvm.viewmodel.SharkViewModel
import com.shark.sharkmvvm.databinding.ActivityLoginBinding
import com.shark.sharkmvvm.viewmodel.LoginViewModel


@SuppressLint("NonConstantResourceId")
@SharkActivity(layoutId = R.layout.activity_login)
class LoginActivity : MvvmActivity() {
    var mDataBinding: ActivityLoginBinding? = null


    @SharkViewModel
    var loginViewModel: LoginViewModel? = null

    override fun initView() {
        mDataBinding?.loginViewModel = loginViewModel

        //监听失去编辑框事件
    }

    /**
     * 获得账套信息更新在界面
     * @param username String
     */
    fun getSOB(username: String) {

    }


    /**
     * 登录按钮
     * @param view View
     */
    fun login(view: View) {
//        setTimeout {
//            loginViewModel?.test()
//        }
        loginViewModel?.test()
    }

    /**
     * 跳转界面
     * @param view View
     */
    fun jumpSetting(view: View) {
    }

}