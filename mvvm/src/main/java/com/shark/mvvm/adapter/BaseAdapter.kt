package com.shark.mvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, TBinding : ViewDataBinding>(
    var itemList: MutableList<T>,
    val brId: Int
) : RecyclerView.Adapter<BaseAdapter<T, TBinding>.ViewHolder>() {
    val TAG: String = "SharkChilli"
    protected var mContext: Context? = null

    //点击事件
    var onClickItem: ((item: T) -> Unit)? = null
    //长按事件
    var onLongClickItem: ((item: T) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //相当于itemView
        lateinit var binding: TBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (mContext == null) {
            mContext = parent.context
        }
        //因为TBinding的具体类型不确定，这里只能用DataBindingUtil.inflate()，而不能用XXXBinding.inflate()
        val binding: TBinding =
            DataBindingUtil.inflate(LayoutInflater.from(mContext), getLayoutId(), parent, false)
        val holder = ViewHolder(binding.root)
        holder.binding = binding
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.setVariable(brId, itemList[position])
        //立即执行绑定，在对view变化时效敏感的地方常用，不加这句有可能出现itemView更新滞后、闪烁等问题
        holder.binding.executePendingBindings()

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    //获取具体子adapter对应的itemLayoutId
    abstract fun getLayoutId(): Int

}