package com.shark.tools.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_code")
data class ScanCode(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "device_name", defaultValue = "") var name: String? = "",
    @ColumnInfo(name = "key_code") var keyCode: Int
)