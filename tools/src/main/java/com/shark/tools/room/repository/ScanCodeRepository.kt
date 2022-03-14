package com.shark.tools.room.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.shark.mvvm.config.ScanConfig
import com.shark.mvvm.spread.TAG
import com.shark.tools.room.entity.ScanCode
import com.shark.tools.room.dao.ScanCodeDao
import com.shark.tools.room.database.ToolsDatabase

class ScanCodeRepository(val context: Context) {
    private var scanCodeDao: ScanCodeDao? = null

    init {
        val database = ToolsDatabase.getInstance(context)
        scanCodeDao = database.settingDao()
    }

    fun update(scanCode: ScanCode) =
        scanCodeDao?.update(scanCode)


    fun delete(scanCode: ScanCode) = scanCodeDao?.delete(scanCode)
    fun delete(scanCode: Int) = scanCodeDao?.delete(scanCode)

    fun deleteAllSetting() =
        scanCodeDao?.deleteAll()


    fun insert(settingInfo: ScanCode) = scanCodeDao?.insert(settingInfo)

    //同步数据库的扫描值到内存中
    fun synchronizeScanCode() {
        val scanCodeList = scanCodeDao?.getAll()
        Log.i(TAG, "synchronizeScanCode: $scanCodeList")
        scanCodeList?.forEach { scanCode ->
            ScanConfig.keySet.add(scanCode.keyCode)
        }
    }
}