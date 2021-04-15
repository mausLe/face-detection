package com.google.mlkit.vision.demo.kotlin

import android.content.Context
import android.graphics.Color
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.mlkit.vision.demo.R

class CustomAdapter (var context: Context, var watchlist : ArrayList<WatchList>) : BaseAdapter() {
    class ViewHolder (row: View) {
        var watchlistID: TextView
        var imageCode: ImageView
        var name : TextView
        var time : TextView
        var borderColor : LinearLayout

        // Gán giá trị ánh
        init {
            watchlistID = row.findViewById(R.id.watchlistID) as TextView
            imageCode = row.findViewById(R.id.imageCode) as ImageView
            name = row.findViewById(R.id.name) as TextView
            time = row.findViewById(R.id.time) as TextView
            borderColor = row.findViewById(R.id.borderLayout) as LinearLayout
        }

    }
    override fun getView(position: Int, convertView: View?, p2: ViewGroup?): View {
        var view : View?
        var viewHolder : ViewHolder
        // On the 1st execution, convertView does have anything
        if (convertView == null) {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.mylist, null)

            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            // On the 2nd, 3rd... exe the binding values with be bind :/
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        var watchList : WatchList = getItem(position) as WatchList
        Glide.with(viewHolder.imageCode)
                .asBitmap()
                .load(watchList.imageCode)
                .into(viewHolder.imageCode)
        // viewHolder.imageCode.setImageResource(watchList.imageCode)
        viewHolder.watchlistID.text = "ID: " + watchList.watchlistID.toString()
        viewHolder.name.text = watchList.name
        viewHolder.time.text = watchList.time.toString()

        if (watchList.type == "Teacher") {
            viewHolder.borderColor!!.setBackgroundColor(Color.parseColor("#cc0000"))
        } else if (watchList.type == "Student") {
            viewHolder.borderColor!!.setBackgroundColor(Color.parseColor("#009933"))
        }

        return view as View
    }

    override fun getItem(position: Int): Any {
        return watchlist.get(position)
    }

    override fun getItemId(itemId: Int): Long {
        return itemId.toLong()
    }

    override fun getCount(): Int {
        return watchlist.size
    }
}