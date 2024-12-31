package com.example.madcamp_week1

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button // Button 임포트
import android.graphics.Color


class Tab2_PhotoAdapter(
    private val photos: List<Pair<Long, ByteArray>>,
    private val isDeleteAction: Boolean,
    private val onButtonClick: (Int, Long) -> Unit // 버튼 클릭 시 호출
) : RecyclerView.Adapter<Tab2_PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView2)
        val actionButton: Button = itemView.findViewById(R.id.actionButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        val bitmap = BitmapFactory.decodeByteArray(photo.second, 0, photo.second.size)
        holder.imageView.setImageBitmap(bitmap)

        var isSelected = false

        // 버튼 텍스트 및 동작 설정
        if (isDeleteAction) {
            holder.actionButton.text = "Delete"
        } else {
            holder.actionButton.text = "Select"
        }
        holder.actionButton.setOnClickListener {
            onButtonClick(position, photo.first)
            if (!isDeleteAction)  {
                if (isSelected) {
                    holder.actionButton.setBackgroundColor(Color.LTGRAY)
                    isSelected = false
                } else {
                    holder.actionButton.setBackgroundColor(Color.GREEN)
                    isSelected = true
                }
                }
            }
        }
    override fun getItemCount() = photos.size
}

