package com.shark.sharkmvvm.view

import android.view.View
import android.widget.AdapterView

class SharkOnItemSelectedListener(private val onItemSelected: (view: View?, position: Int, id: Long) -> Unit) :
    AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onItemSelected?.invoke(view, position, id)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}