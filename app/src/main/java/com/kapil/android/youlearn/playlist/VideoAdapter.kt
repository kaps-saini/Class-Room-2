package com.kapil.android.youlearn.playlist


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kapil.android.youlearn.R
import com.kapil.android.youlearn.models.search.Item
import com.squareup.picasso.Picasso

class VideoAdapter(private val OnItemClicked: OnItemClickListener):
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        val title : TextView = itemView.findViewById(R.id.title)
        val channelName : TextView = itemView.findViewById(R.id.channelName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_view, parent, false)
        val videoViewHolder = VideoViewHolder(view)
        view.setOnClickListener {
            OnItemClicked.onClick(differ.currentList[videoViewHolder.adapterPosition])
        }
        return videoViewHolder
    }

    private  val diffCallBack = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
           return oldItem.id.searchVideoId == newItem.id.searchVideoId
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val items =  differ.currentList[position]
        holder.title.text = items?.snippet?.title
        holder.channelName.text = items?.snippet?.channelTitle
        Picasso.get().load(items?.snippet?.thumbnails?.high?.highUrl).into(holder.thumbnail)
    }

    interface OnItemClickListener{
        fun onClick(items: Item)
    }
}

