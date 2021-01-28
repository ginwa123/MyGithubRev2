package com.m.ginwa.mygithubrev2.ui.detailuser.following

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
import com.m.ginwa.core.domain.model.Following
import com.m.ginwa.core.utils.FollowingDiffUtil
import com.m.ginwa.core.utils.GlideApp
import com.m.ginwa.mygithubrev2.databinding.ListUserBinding
import timber.log.Timber

class FollowingsAdapter(
    private val context: Context,
    val dataSets: ArrayList<Following> = arrayListOf()
) :
    RecyclerView.Adapter<FollowingsAdapter.ViewHolder>() {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.load(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSets.size
    fun updateDataSets(dataSets: List<Following>) {
        val diffResult = DiffUtil.calculateDiff(FollowingDiffUtil(this.dataSets, dataSets))
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.clearImage()
    }

}