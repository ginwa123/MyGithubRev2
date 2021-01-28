package com.m.ginwa.mygithubrev2.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.utils.GlideApp
import com.m.ginwa.core.utils.UserDiffUtil
import com.m.ginwa.mygithubrev2.databinding.ListUserBinding
import timber.log.Timber


class SearchAdapter(
    private val context: Context,
    val dataSets: ArrayList<User> = arrayListOf()
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    lateinit var onClick: (Bundle) -> Unit

    inner class ViewHolder(private val binding: ListUserBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun load(position: Int) {
            binding.apply {
                val item = dataSets[position]
                GlideApp.with(context)
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

        fun clearImage() {
            binding.apply {
                try {
                    GlideApp.with(context).clear(imUser)
                    Timber.d("clear viewholder image: $adapterPosition")
                } catch (e: Exception) {

                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.load(position)
    }

    override fun getItemCount(): Int = dataSets.size

    fun updateDataSets(dataSets: List<User>) {
        val diffResult = DiffUtil.calculateDiff(UserDiffUtil(this.dataSets, dataSets))
        diffResult.dispatchUpdatesTo(this)
    }

    fun clearDataSets() {
        this.dataSets.clear()
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.clearImage()
    }


}