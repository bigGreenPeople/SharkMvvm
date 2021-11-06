package com.shark.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class BaseSpinnerAdapter<T, TBinding : ViewDataBinding>(
    var itemList: List<T>,
    val brId: Int
) : BaseAdapter() {
    val TAG: String = "SharkChilli"
    protected var mContext: Context? = null
    lateinit var binding: TBinding

    var onClickItem: ((item: T) -> Unit)? = null

    override fun getCount(): Int = itemList.size

    override fun getItem(position: Int): Any = itemList[position]!!

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent?.context),
                getLayoutId(),
                parent,
                false
            )
            binding.root
        } else {
            //去除convertView中bangding的dataBinding
            binding = DataBindingUtil.getBinding(convertView)!!
        }

        binding.setVariable(brId, itemList[position])
        return binding.root
    }

    //获取具体子adapter对应的itemLayoutId
    abstract fun getLayoutId(): Int

}