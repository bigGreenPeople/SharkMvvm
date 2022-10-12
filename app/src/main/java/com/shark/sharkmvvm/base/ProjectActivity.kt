package com.shark.sharkmvvm.base

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.shark.mvvm.activity.MvvmActivity
import com.shark.mvvm.exui.SmartRecyclerExAdapter
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager
import com.xuexiang.xui.utils.KeyboardUtils
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.dialog.DialogLoader
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.dialog.strategy.InputInfo
import com.yanzhenjie.recyclerview.*

open class ProjectActivity : MvvmActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ActivityManager.addActivity(this)

//        sharkActivity?.let {
//            findViewById<TitleBar?>(R.id.tb_title)
//        }?.apply {
//            if (sharkActivity?.title!!.isNotEmpty())
//                setTitle(sharkActivity?.title)
//            setLeftClickListener { finish() }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        ActivityManager.removeActivity(this)
    }


    open fun getRecyclerViewItem(recyclerView: RecyclerView?, position: Int): View? {
        if (recyclerView?.getLayoutManager() == null || recyclerView.getAdapter() == null) {
            return null
        }
        if (position > recyclerView.getAdapter()!!.getItemCount()) {
            return null
        }
        val viewHolder: RecyclerView.ViewHolder = recyclerView.getAdapter()!!
            .createViewHolder(recyclerView, recyclerView.getAdapter()!!.getItemViewType(position))
        recyclerView.getAdapter()!!.onBindViewHolder(viewHolder, position)
        viewHolder.itemView.measure(
            View.MeasureSpec.makeMeasureSpec(
                recyclerView.getWidth(),
                View.MeasureSpec.EXACTLY
            ), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        return viewHolder.itemView
    }
    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private fun <T> getClickListener(
        listData: MutableLiveData<MutableList<T>>,
        deleteCallback: ((data: T) -> Unit)? = null,
    ): OnItemMenuClickListener =
        OnItemMenuClickListener { menuBridge: SwipeMenuBridge, position: Int ->
            menuBridge.closeMenu()
            val direction = menuBridge.direction // 左侧还是右侧菜单。
            val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                val data = listData.value!![position]
                listData.value!!.removeAt(position)
                listData.postValue(listData.value)
                deleteCallback?.invoke(data)
            }
        }

    val mSwipeMenuCreator =
        SwipeMenuCreator { swipeLeftMenu: SwipeMenu, swipeRightMenu: SwipeMenu, position: Int ->

            val width = resources.getDimensionPixelSize(com.shark.sharkmvvm.R.dimen.dp_70)

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            val height = ViewGroup.LayoutParams.MATCH_PARENT

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            run {
                val deleteItem: SwipeMenuItem =
                    SwipeMenuItem(this).setBackground(com.shark.sharkmvvm.R.drawable.menu_selector_red)
                        .setImage(com.shark.sharkmvvm.R.drawable.ic_swipe_menu_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height)
                swipeRightMenu.addMenuItem(deleteItem) // 添加菜单到右侧。
            }
        }

    /**
     * 初始化列表
     * @param swipeRecyclerView SwipeRecyclerView
     * @param swipeRefreshLayout SwipeRefreshLayout
     * @param brId Int
     * @param layoutId Int
     * @param loadData Function0<Unit>
     * @return SmartRecyclerExAdapter<T, TBinding>
     */
    fun <T, TBinding : ViewDataBinding> initList(
        swipeRecyclerView: SwipeRecyclerView,
        brId: Int, layoutId: Int,
        hasDelete: Boolean = false,
        listData: MutableLiveData<MutableList<T>>? = null,
        deleteCallback: ((data: T) -> Unit)? = null,
        swipeRefreshLayout: SwipeRefreshLayout? = null,
        loadData: (() -> Unit)? = null
    ): SmartRecyclerExAdapter<T, TBinding> {
        return swipeRecyclerView.let {
            it.layoutManager = XLinearLayoutManager(it.context)
            it.itemAnimator = DefaultItemAnimator()
            //必须在setAdapter之前调用
            if (hasDelete) {
                it.setSwipeMenuCreator(mSwipeMenuCreator)
                it.setOnItemMenuClickListener(getClickListener(listData!!, deleteCallback))
            }

            val smartRecyclerExAdapter = SmartRecyclerExAdapter<T, TBinding>(
                this,
                brId,
                layoutId
            )

            it.adapter = smartRecyclerExAdapter

            swipeRefreshLayout?.setColorSchemeColors(
                -0xff6634,
                -0xbbbc,
                -0x996700,
                -0x559934,
                -0x7800
            )

            //下拉刷新
            swipeRefreshLayout?.setOnRefreshListener { loadData?.invoke() }
            smartRecyclerExAdapter
        }
    }

    /**
     * 弹出对话框
     * @param title String
     * @param content String
     * @param type Int
     * @param submitCall Function1<[@kotlin.ParameterName] String, Unit>?
     */
    fun inputDialog(
        title: String = "填写",
        content: String = "请填写数量",
        type: Int = InputType.TYPE_CLASS_NUMBER,
        submitCall: ((text: String) -> Unit)? = null
    ) {
        DialogLoader.getInstance().showInputDialog(
            this,
            com.shark.mvvm.R.drawable.icon_tip,
            title,
            content,
            InputInfo(type),
            { _, _ -> },
            "确认",
            { dialog: DialogInterface, _ ->
                KeyboardUtils.hideSoftInput(dialog)
                dialog.dismiss()
                if (dialog is MaterialDialog) {
                    val number =
                        dialog.inputEditText!!.text.toString()
                    submitCall?.invoke(number)
                }
            },
            "取消"
        ) { dialog: DialogInterface, _: Int ->
            KeyboardUtils.hideSoftInput(dialog)
            dialog.dismiss()
        }
    }
}