package com.codepath.apps.restclienttemplate

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codepath.apps.restclienttemplate.models.Tweet

private val TAG = "TweetAdapter"
class TweetAdapter(val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetAdapter.Viewholder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetAdapter.Viewholder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        //Inflate layout
        val view = inflater.inflate(R.layout.item_tweet,parent,false)

        return  Viewholder(view)
    }
    //Populating data into the item through holder
    override fun onBindViewHolder(holder: TweetAdapter.Viewholder, position: Int) {
        //get data based on positon
        val tweet: Tweet = tweets.get(position)

        holder.tvUsername.text = tweet.user?.name
        holder.tvTweetBody.text = tweet.body
        holder.tvCreatedAt.text = tweet.createdAt
        //holder.tvCreatedAt.setText(tweet.getFormattedTimestamp())

        Glide.with(holder.itemView)
            .load(tweet.user?.publicImageUrl)
            .into(holder.ivProfileImage)

    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    // Clean all elements of the recycler
    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)
        val tvCreatedAt = itemView.findViewById<TextView>(R.id.tvCreatedAt)
    }


}