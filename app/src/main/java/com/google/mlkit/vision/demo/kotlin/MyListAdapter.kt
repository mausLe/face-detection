package com.google.mlkit.vision.demo.kotlin


import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.mlkit.vision.demo.R

class MyListAdapter(private val context: Activity, private val imageID: Array<Int>, private val name: Array<String>, private val id: Array<Int>)
    : ArrayAdapter<String>(context, R.layout.mylist, name) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.mylist, null, true)

        val imageView = rowView.findViewById(R.id.imageID) as ImageView
        val nameText = rowView.findViewById(R.id.name) as TextView
        val idText = rowView.findViewById(R.id.id) as TextView

        imageView.setImageResource(imageID[position])
        nameText.text = name[position]
        idText.text = id[position].toString()

        return rowView
    }
}