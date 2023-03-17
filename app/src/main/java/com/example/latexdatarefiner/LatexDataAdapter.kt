package com.example.latexdatarefiner

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class LatexDataAdapter(private val items: MutableList<LatexData>) :
    RecyclerView.Adapter<LatexDataAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val latexCode: TextView = view.findViewById(R.id.latex_code)
        val imageView: ImageView = view.findViewById(R.id.image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_latex_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.latexCode.text = item.latexCode
        val imgFile = File(item.imagePath)
        if (imgFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            holder.imageView.setImageBitmap(bitmap)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newData: List<LatexData>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }
}