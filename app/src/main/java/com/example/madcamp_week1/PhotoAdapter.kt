package com.example.madcamp_week1

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button // Button 임포트


class PhotoAdapter(
    private val photos: List<Pair<Long, ByteArray>>,
    private val isDeleteAction: Boolean,
    private val onButtonClick: (Int, Long) -> Unit // 버튼 클릭 시 호출
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
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

        // 버튼 텍스트 및 동작 설정
        holder.actionButton.text = if (isDeleteAction) "Delete" else "Upload"
        holder.actionButton.setOnClickListener {
            onButtonClick(position, photo.first)
        }
    }

    override fun getItemCount() = photos.size
}