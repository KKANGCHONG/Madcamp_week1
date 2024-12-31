package com.example.week1project.ui.Tab1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week1project.databinding.FragmentTab11Binding
import com.example.week1project.DatabaseHelper

class Tab1_1_Fragment : Fragment() {

    private var _binding: FragmentTab11Binding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tab1ViewModel =
            ViewModelProvider(this).get(Tab1ViewModel::class.java)
        _binding = FragmentTab11Binding.inflate(inflater, container, false)
        val root: View = binding.root
        databaseHelper = DatabaseHelper(requireContext())
        setupRecyclerView()
        return root
    }
    private fun setupRecyclerView() {
        val recyclerView = binding.rvList // ViewBinding으로 RecyclerView 참조
        val itemList = getCapsuleTitles()
        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = RecyclerViewAdapter(itemList)
        // Divider 추가
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )
    }
    private fun getCapsuleTitles(): MutableList<Item.Item1> {
        val itemList = mutableListOf<Item.Item1>()
        val db = databaseHelper.readableDatabase
        val query = "SELECT ${DatabaseHelper.CAPSULE_TITLE} FROM ${DatabaseHelper.TABLE_NAME1} GROUP BY ${DatabaseHelper.CAPSULE_TITLE}"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val columnIndex = cursor.getColumnIndex(DatabaseHelper.CAPSULE_TITLE)
                if (columnIndex != -1) {
                    val title = cursor.getString(columnIndex)
                    itemList.add(Item.Item1(title))
                    Log.e("추가된 캡슐 제목", title)
                } else {
                    Log.e("CursorError", "Invalid column index for CAPSULE_TITLE")
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemList
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}