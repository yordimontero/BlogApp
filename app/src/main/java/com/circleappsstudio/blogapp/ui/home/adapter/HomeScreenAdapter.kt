package com.circleappsstudio.blogapp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.circleappsstudio.blogapp.core.BaseViewHolder
import com.circleappsstudio.blogapp.data.model.Post
import com.circleappsstudio.blogapp.databinding.PostItemViewBinding

class HomeScreenAdapter(
    private val listPost: List<Post>
): RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {

        // itemBinding inflates the XML view
        val itemBinding = PostItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        /**
            onCreateViewHolder creates the instance of HomeScreenViewHolder() that gives
            as parameters the inflated XML view and the context from parent.
         */
        return HomeScreenViewHolder(itemBinding, parent.context)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {

        when(holder) {
            is HomeScreenViewHolder -> {
                /**
                    Each position for the list of posts will be binded with data.
                 */
                holder.bind(listPost[position])
            }
        }

    }

    override fun getItemCount(): Int = listPost.size

    /**
        HomeScreenViewHolder() extends from BaseViewHolder<> that
        needs a generic value "T" (what is going to "inflate").

        The function bind() overrides "T" generic with the value passed when BaseViewHolder<>
        is extended ("Post" in this case).
    */
    private inner class HomeScreenViewHolder(
        val binding: PostItemViewBinding,
        val context: Context
    ): BaseViewHolder<Post>(binding.root) {

        override fun bind(item: Post) {
            /**
                bind() function gets the position gave for onBindViewHolder
                and sets the data.
             */
            Glide.with(context).load(item.profile_picture).centerCrop().into(binding.profilePicture)
            binding.profileName.text = item.profile_name
            binding.postTimestamp.text = "Testing"
            Glide.with(context).load(item.post_image).centerCrop().into(binding.postImage)
        }

    }

}