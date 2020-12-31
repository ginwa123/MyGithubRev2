package com.m.ginwa.core.utils

import androidx.recyclerview.widget.DiffUtil
import com.m.ginwa.core.domain.model.User

class UserDiffUtil(
    private var oldItems: List<User>,
    private var newItems: List<User>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].login == newItems[newItemPosition].login
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems == newItems
    }
}