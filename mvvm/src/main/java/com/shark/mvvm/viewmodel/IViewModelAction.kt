package com.shark.viewmodel

import androidx.lifecycle.MutableLiveData
import com.shark.viewmodel.BaseActionEvent


interface IViewModelAction {
    var actionLiveData: MutableLiveData<BaseActionEvent>?

    fun startLoading()

    fun startLoading(message: String?)

    fun dismissLoading()

    fun showToast(message: String?)

    fun finish()

    fun finishWithResultOk()

}