package com.shark.mvvm.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shark.mvvm.activity.BaseActivity
import com.shark.mvvm.activity.SharkActivity
import com.shark.mvvm.view.TitleListener
import com.shark.mvvm.viewmodel.SharkViewModel
import java.lang.RuntimeException

open class BaseFragment : Fragment(), TitleListener {
    val TAG = "SharkChilli"
    override var sharkActivity: SharkActivity? = null
    var viewDataBinding: ViewDataBinding? = null


    init {
        //获取SharkActivity注解类
        this::class.annotations.find { it is SharkActivity }
            ?.let { sharkActivity = it as SharkActivity }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (sharkActivity == null) return super.onCreateView(
            inflater,
            container,
            savedInstanceState
        )

        //注入ViewModel值
        injectionViewModel()

        val mDataBinding =
            initDataBinding(inflater, container, savedInstanceState)
        viewDataBinding = mDataBinding
        //设置生命周期监听
        mDataBinding.lifecycleOwner = this


        initView()
        return mDataBinding.root
    }

    /**
     * 注入ViewModel
     */
    private fun injectionViewModel() {
        // 扫描出本类所有@SharkViewModel 设置值
        this::class.java.declaredFields.forEach {
            it.isAccessible = true
//            val annotation = it.getAnnotation(SharkViewModel::class.java)
            if (it.isAnnotationPresent(SharkViewModel::class.java)) {
                val viewModel = getViewModel(it.type as Class<ViewModel>)
                Log.i(TAG, "injectionViewModel: $viewModel")
                it.set(this, viewModel)
            }
        }
    }

    /**
     * 在此处初始化界面相关信息
     */
    open fun initView() {

    }

    /**
     * 初始DataBinding
     */
    fun initDataBinding(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewDataBinding {
        //寻找定义的DataBinding属性如果不存在 抛出异常
        this::class.java.declaredFields.forEach {
            it.isAccessible = true
            if (ViewDataBinding::class.java.isAssignableFrom(it.type)) {
                it.set(
                    this,
                    DataBindingUtil.inflate(inflater, sharkActivity!!.layoutId, container, false)
                )
                //设置页面
                val viewDataBinding: ViewDataBinding = it.get(this) as ViewDataBinding
//                viewDataBinding.setVariable(BR.fragment, this)
                return viewDataBinding
            }
        }

        throw RuntimeException("you must declare ViewDataBinding Field in your Fragment!!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "BaseFragment: $activity")
        if (activity is BaseActivity) {
            (activity as BaseActivity).setScanEventListener(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "BaseFragment onDestroy: ")
    }

    /**
     * 页面跳转
     */
    fun jumpActivity(activityClazz: Class<*>) {
        val intent = Intent(activity, activityClazz)
        startActivity(intent)
    }

    /**
     * 获取ViewModel
     * @param viewModelClazz Class<T>
     * @return T
     */
    fun <T : ViewModel> getViewModel(viewModelClazz: Class<T>): T {
        return ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(
            viewModelClazz
        )
    }

    /**
     * 返回方法
     */
    override fun back(view: View) {
    }
}