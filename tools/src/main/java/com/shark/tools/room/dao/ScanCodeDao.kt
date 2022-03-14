package com.shark.tools.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shark.tools.room.entity.ScanCode


@Dao
interface ScanCodeDao {
    /**
     * 此处返回的LiveData 的value为null 我们直接使用 observe来动态监听值的变化即可
     * @return LiveData<List<SettingInfo>>
     */
    @Query("SELECT * FROM scan_code")
    fun getAll(): List<ScanCode>

    @Insert
    fun insert(scanCode: ScanCode): Long

    @Update
    fun update(scanCode: ScanCode)

    @Delete
    fun delete(scanCode: ScanCode)

    @Query("DELETE  FROM scan_code where key_code=:scanCode")
    fun delete(scanCode: Int)

    @Query("DELETE FROM scan_code ")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM scan_code")
    fun getCount(): Int
}