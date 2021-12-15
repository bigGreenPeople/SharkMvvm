package com.shark.sharkmvvm

import android.annotation.SuppressLint
import android.view.View
import com.shark.mvvm.activity.MvvmActivity
import com.shark.mvvm.activity.SharkActivity
import com.shark.mvvm.retrofit.RetrofitManagement
import com.shark.mvvm.viewmodel.SharkViewModel
import com.shark.sharkmvvm.databinding.ActivityLoginBinding
import com.shark.mvvm.retrofit.interceptor.HeaderInterceptor
import com.shark.sharkmvvm.viewmodel.LoginViewModel


@SuppressLint("NonConstantResourceId")
@SharkActivity(layoutId = R.layout.activity_login)
class LoginActivity : MvvmActivity() {
    var mDataBinding: ActivityLoginBinding? = null


    @SharkViewModel
    lateinit var loginViewModel: LoginViewModel

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

    fun add(view: View) {
        HeaderInterceptor.addHeader("test_token", "213154")
    }

    /**
     * 登录按钮
     * @param view View
     */
    fun login(view: View) {
//        setTimeout {
//            loginViewModel?.test()
//        }
//        loginViewModel?.test()

        loginViewModel.test()
//
//        alertWarning("警告", "是否确定",
//            cancelClick = {
//                "取消点击".showToast()
//            },
//            okClick = {
//                "确定点击".showToast()
//
//            })
    }

    /**
     * 跳转界面
     * @param view View
     */
    fun jumpSetting(view: View) {
    }

}