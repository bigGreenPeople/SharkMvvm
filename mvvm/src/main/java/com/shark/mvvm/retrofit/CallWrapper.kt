package com.shark.mvvm.retrofit

import android.app.Activity
import android.graphics.Color
import android.util.Log
import cn.pedant.SweetAlert.SweetAlertDialog
import com.zyao89.view.zloading.ZLoadingDialog
import com.zyao89.view.zloading.Z_TYPE
import retrofit2.Call
import retrofit2.Response

@Deprecated("Please Use RetrofitManagement")
class CallWrapper<T>(val mContext: Activity, val mCall: Call<T>) {
    val TAG = "SharkChilli"

    fun enqueue(
        tip: String = "Loading",
//        callback: Callback<T>,
        callback: (t: T) -> Unit
    ) {
        //加载load框
        val pDialog = ZLoadingDialog(mContext).apply {
            setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE) //设置类型
            setLoadingColor(Color.WHITE) //颜色
            setHintText(tip)
            setHintTextSize(16f) // 设置字体大小 dp
            setCanceledOnTouchOutside(false)
            setHintTextColor(Color.WHITE) // 设置字体颜色
            setDialogBackgroundColor(Color.parseColor("#00000000")) // 设置背景色，默认白色
            show()
        }

        //发送请求
        mCall.enqueue(object : retrofit2.Callback<T?> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                if (response.body() == null) {
                    SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE).apply {
                        titleText = "服务器返回异常!"
                        confirmText = "好的"
                        show()
                    }
                    return
                }
                Log.i(TAG, "onResponse: " + response.body().toString())
//                callback.success(response.body()!!)
                callback.invoke(response.body()!!)
                mContext.runOnUiThread { pDialog.cancel() }
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                mContext.runOnUiThread { pDialog.cancel() }
                Log.e(TAG, "onFailure: ", t)
                SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE).apply {
                    titleText = "网络连接超时!"
                    confirmText = "好的"
                    show()
                }
            }
        })
    }
}