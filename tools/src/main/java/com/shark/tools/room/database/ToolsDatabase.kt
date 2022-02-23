package com.shark.tools.room.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shark.mvvm.config.ScanConfig
import com.shark.mvvm.spread.TAG
import com.shark.tools.room.entity.ScanCode
import com.shark.tools.room.dao.ScanCodeDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [ScanCode::class], version = 1, exportSchema = false)
abstract class ToolsDatabase : RoomDatabase() {
    abstract fun settingDao(): ScanCodeDao?

    companion object {
        private const val DB_NAME = "shark_tools.db"
        //添加status字段
//        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE setting_info ADD COLUMN status TEXT")
//            }
//        }

        @Volatile
        private var instance: ToolsDatabase? = null
        fun getInstance(context: Context): ToolsDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        /**
         * 初始化扫描键值
         */
        fun initScanKeyCode() {
            instance?.let { database ->
                Log.i(TAG, "dasdsa")

                val settingDao = database.settingDao()

                Log.i(TAG, "初始化Tools数据库")
                GlobalScope.launch {
                    if (settingDao!!.getCount() == 0) {
                        ScanConfig.keySet.forEach {
                            settingDao.insert(ScanCode(keyCode = it))
                        }
                    }

                }
            }
        }

        /**
         * 初始化数据库
         */
        private object createdCallBack : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                //在新装app时会调用，调用时机为数据库build()之后，数据库升级时不调用此函数
                initScanKeyCode()
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.i(TAG, "onOpen")
            }
        }

        private fun buildDatabase(context: Context): ToolsDatabase {
            val build = Room.databaseBuilder(context, ToolsDatabase::class.java, DB_NAME)
//                .addMigrations(MIGRATION_1_2)
//                .createFromAsset("nidec_read_db.db")
//                .addCallback(createdCallBack)
                .build()
            instance = build
            initScanKeyCode()
            return build
        }

    }


}