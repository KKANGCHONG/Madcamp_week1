package com.example.madcamp_week1

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Tab1_3_Adapter(private val itemList: List<Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var databaseHelper: DatabaseHelper

    companion object {
        private const val VIEW_TYPE_A = 0
        private const val VIEW_TYPE_B = 1
        private const val VIEW_TYPE_C = 2
    }

    // ViewHolder for TypeA
    class TypeAViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.capsuleTitle)
        val text: TextView = itemView.findViewById(R.id.capsuleText)
        val date: TextView = itemView.findViewById(R.id.capsuleDate)
    }

    // ViewHolder for TypeB
    class TypeBViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.innerRecyclerView)
    }

    // ViewHolder for TypeC
    class TypeCViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.findViewById(R.id.capsulelocation)
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is Item.TypeA -> VIEW_TYPE_A
            is Item.TypeB -> VIEW_TYPE_B
            is Item.TypeC -> VIEW_TYPE_C
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_A -> {
                val view = inflater.inflate(R.layout.show_capsule_string, parent, false)
                TypeAViewHolder(view)
            }
            VIEW_TYPE_B -> {
                val view = inflater.inflate(R.layout.show_capsule_images, parent, false)
                TypeBViewHolder(view)
            }
            VIEW_TYPE_C -> {
                val view = inflater.inflate(R.layout.show_capsule_location, parent, false)
                TypeCViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itemList[position]) {
            is Item.TypeA -> {
                val viewHolder = holder as TypeAViewHolder
                viewHolder.title.text = item.title
                viewHolder.text.text = item.text
                viewHolder.date.text = item.date

                viewHolder.title.textSize = 16f
                viewHolder.date.textSize = 12f
                viewHolder.text.textSize = 10f

            }
            is Item.TypeB -> {
                val viewHolder = holder as TypeBViewHolder
                val gridAdapter = GridImageAdapter(item.imageRes, databaseHelper)
                viewHolder.recyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 3)
                viewHolder.recyclerView.adapter = gridAdapter
            }
            is Item.TypeC -> {
                val viewHolder = holder as TypeCViewHolder
                viewHolder.location.text = item.location
            }
        }
    }

    class GridImageAdapter(
        private val imageList: List<Long>,
        private val databaseHelper: DatabaseHelper // DatabaseHelper를 생성자에서 전달받음
    ) : RecyclerView.Adapter<GridImageAdapter.ImageViewHolder>() {

        private val photos: List<ByteArray> = databaseHelper.getImageListFromIds(imageList)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.show_capsule_image, parent, false)
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            // 데이터베이스에서 이미지 가져오기
            val imageBlob = photos[position]
            val bitmap = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.size)
            holder.imageView.setImageBitmap(bitmap) // 이미지 설정
        }

        override fun getItemCount() = photos.size

        class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.capsuleimage)
        }
    }

    override fun getItemCount() = itemList.size
}