package com.shark.tools.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.shark.mvvm.config.ScanConfig
import com.shark.mvvm.viewmodel.BaseViewModel
import com.shark.tools.room.entity.ScanCode
import com.shark.tools.room.repository.ScanCodeRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ScanKeyCodeViewModel(application: Application) : BaseViewModel(application) {
    //0是等待扫描 1 是扫描多 2扫描成功 3添加成功
    val status: MutableLiveData<Int> = MutableLiveData()
    private val scanCodeRepository: ScanCodeRepository = ScanCodeRepository(application)

    init {
        status.value = 0
//        GlobalScope.launch {
//            ScanConfig.keySet.forEach {
//                scanCodeRepository.insert(ScanCode(keyCode = it))
//            }
//        }
    }

    /**
     * 添加扫描事件
     * @param keyCode Int
     */
    fun addCodeKey(keyCode: Int) {
        ScanConfig.keySet.add(keyCode)
        GlobalScope.launch {
            scanCodeRepository.insert(ScanCode(keyCode = keyCode))
        }
    }


}