package com.shark.mvvm.exui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder

/**
 * 增强SmartRecyclerAdapter
 */
open class SmartRecyclerExAdapter<T, TBinding : ViewDataBinding> : SmartRecyclerAdapter<T> {
    var mContext: Context? = null
    private var mLayoutId: Int? = null
    private var mBrId: Int? = null

    constructor(context: Context, brId: Int, layoutId: Int) : super(layoutId) {
        mContext = context
        mLayoutId = layoutId
        mBrId = brId
    }

    constructor(
        context: Context,
        brId: Int,
        collection: MutableCollection<T>?,
        layoutId: Int
    ) : super(
        collection,
        layoutId
    ) {
        mContext = context
        mLayoutId = layoutId
        mBrId = brId
    }

    constructor(
        context: Context, brId: Int,
        collection: MutableCollection<T>?,
        layoutId: Int,
        listener: SmartViewHolder.OnItemClickListener?
    ) : super(collection, layoutId, listener) {
        mContext = context
        mLayoutId = layoutId
        mBrId = brId
    }


    inner class ViewHolder : SmartViewHolder {
        //相当于itemView
        var binding: TBinding? = null

        constructor(itemView: View) : super(itemView)
        constructor(
            itemView: View,
            onItemClickListener: OnItemClickListener?,
            onItemLongClickListener: OnItemLongClickListener?
        ) : super(itemView, onItemClickListener, onItemLongClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (mContext == null) {
            mContext = parent.context
        }
        //因为TBinding的具体类型不确定，这里只能用DataBindingUtil.inflate()，而不能用XXXBinding.inflate()
        val binding: TBinding =
            DataBindingUtil.inflate(LayoutInflater.from(mContext), mLayoutId!!, parent, false)
        val holder = ViewHolder(binding.root, mOnItemClickListener, mOnItemLongClickListener)
        holder.binding = binding
        return holder
    }

    /*override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.setVariable(mBrId!!, listData[position])
        //立即执行绑定，在对view变化时效敏感的地方常用，不加这句有可能出现itemView更新滞后、闪烁等问题
        holder.binding?.executePendingBindings()
    }*/
    override fun onBindViewHolder(holder: SmartViewHolder?, model: T, position: Int) {
        holder as SmartRecyclerExAdapter<*, *>.ViewHolder
        holder.binding?.setVariable(mBrId!!, listData[position])
        //立即执行绑定，在对view变化时效敏感的地方常用，不加这句有可能出现itemView更新滞后、闪烁等问题
        holder.binding?.executePendingBindings()
    }
}