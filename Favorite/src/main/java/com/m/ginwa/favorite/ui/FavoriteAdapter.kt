package com.m.ginwa.favorite.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.utils.UserDiffUtil
import com.m.ginwa.mygithubrev2.R

class FavoriteAdapter(
    private val context: Context,
    private val dataSets: ArrayList<User> = arrayListOf()
) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private var recentDeleteDataPosition: Int? = null
    private var recentDeleteData: User? = null
    lateinit var onClick: (Bundle) -> Unit
    lateinit var onDelete: (Int, User) -> Unit

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun load(position: Int) {
            val item = dataSets[position]
            val imUser = itemView.findViewById<ImageView>(R.id.imUser)
            val tvName = itemView.findViewById<TextView>(R.id.tvName)
            Glide.with(context)
                .load(item.avatarUrl)
                .apply {
                    transform(CenterCrop(), RoundedCorners(8))
                    override(60, 60)
                }
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imUser)

            tvName.text = item.login
            val bundle = bundleOf("login" to item.login)
            itemView.setOnClickListener { onClick.invoke(bundle) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.load(position)
    }

    override fun getItemCount(): Int = dataSets.size

    fun updateDataSets(dataSets: List<User>) {
        if (this.dataSets != dataSets) {
            val diffResult = DiffUtil.calculateDiff(UserDiffUtil(this.dataSets, dataSets))
            diffResult.dispatchUpdatesTo(this)
        }
    }

    fun deleteData(adapterPosition: Int) {
        recentDeleteData = dataSets[adapterPosition]
        recentDeleteDataPosition = adapterPosition
        recentDeleteData?.let { user->
            recentDeleteDataPosition?.let {pos->
                onDelete.invoke(pos , user)
            }
        }
    }
}