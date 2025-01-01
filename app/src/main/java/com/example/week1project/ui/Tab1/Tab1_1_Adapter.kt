package com.example.week1project.ui.Tab1

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week1project.databinding.ListItemBinding
import com.example.week1project.R
import android.os.Bundle

import androidx.fragment.app.Fragment


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
            val fragmentManager = (holder.itemView.context as androidx.fragment.app.FragmentActivity).supportFragmentManager
            val fragment = Tab1_2_Fragment().apply {
                arguments = Bundle().apply {
                    putString("title", currentItem.text)
                }
            }
            fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment)
                .addToBackStack(null)
                .commit()

        }
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int, item: Item)
    }
}