package com.example.newsfresh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class newsListAdapter(private val listener : newsItemClicked): RecyclerView.Adapter<newsViewHolder>() {
    private val items: ArrayList<News> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): newsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        val viewHolder = newsViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.absoluteAdapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: newsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        holder.authorView.text = currentItem.author
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.image)
    }

    fun updateNews(updatedNews: ArrayList<News>)
    {
     items.clear()
     items.addAll(updatedNews)
     notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return items.size
    }
}

class newsViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
    val authorView : TextView = itemView.findViewById(R.id.author)
    val titleView : TextView = itemView.findViewById(R.id.title)
    val image: ImageView= itemView.findViewById(R.id.imageView)
}

interface newsItemClicked{
    fun onItemClicked(item: News)
}