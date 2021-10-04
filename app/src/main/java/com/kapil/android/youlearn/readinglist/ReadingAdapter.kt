package com.kapil.android.youlearn.readinglist

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

class ReadingAdapter(private val onItemClick: OnClick): RecyclerView.Adapter<ReadingAdapter.ReadingViewHolder>() {

    inner class ReadingViewHolder(view: View): RecyclerView.ViewHolder(view){
        val thumbnail : ImageView = view.findViewById(R.id.reading_thumbnail)
        val title: TextView = view.findViewById(R.id.reading_title)
        val channelName: TextView = view.findViewById(R.id.reading_channelName)
    }

    private val diffCallBack = object :DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id.searchVideoId == newItem.id.searchVideoId
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reading_list_single_view, parent, false)
        val viewHolder = ReadingViewHolder(view)
        view.setOnClickListener {
            onItemClick.onItemClicked(differ.currentList[viewHolder.adapterPosition])
        }
        return  viewHolder
    }

    override fun onBindViewHolder(holder: ReadingViewHolder, position: Int) {
        val data = differ.currentList[position]
        holder.title.text = data.snippet.title
        holder.channelName.text = data.snippet.channelTitle
        Picasso.get().load(data.snippet.thumbnails.high.highUrl).into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnClick{
        fun onItemClicked(item: Item)
    }
}