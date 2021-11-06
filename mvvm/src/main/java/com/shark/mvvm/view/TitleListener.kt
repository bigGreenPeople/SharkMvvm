package com.shark.view

import android.view.View
import com.shark.activity.SharkActivity

/**
 * 此接口为标题导航接口
 * @property sharkActivity SharkActivity?
 */
interface TitleListener {
    var sharkActivity: SharkActivity?

    //返回方法
    fun back(view: View)
}