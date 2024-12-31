package com.example.week1project.ui.Tab1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.week1project.databinding.ListItemBinding
class RecyclerViewAdapter(private var itemList: MutableList<Item.Item1>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    // ViewHolder 클래스 정의
    class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // ListItemBinding 사용
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 현재 아이템 가져오기
        val currentItem = itemList[position]
        holder.binding.dateHeader.text = currentItem.text // list_item.xml의 TextView ID
        // 클릭 이벤트 추가
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, Tab1_2_Fragment::class.java)
            intent.putExtra("title", currentItem.text) // 데이터 전달
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int, item: Item)
    }
}