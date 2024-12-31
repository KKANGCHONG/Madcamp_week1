package com.example.madcamp_week1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.DatabaseHelper.Companion.CAPSULE_TITLE

class Tab1_1_MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab1_1_activity_main)

        databaseHelper = DatabaseHelper(this)
        databaseHelper.logCapsules()



        // RecyclerView 초기화
        val recyclerView: RecyclerView = findViewById(R.id.rv_list)

        // 리스트 데이터 생성
        val itemList = getCapsuleTitles()

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerViewAdapter(itemList)

        // Divider 추가
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    private fun getCapsuleTitles(): MutableList<Item.Item1> {
        val itemList = mutableListOf<Item.Item1>()
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val query = "SELECT $CAPSULE_TITLE FROM ${DatabaseHelper.TABLE_NAME1} GROUP BY $CAPSULE_TITLE"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val columnIndex = cursor.getColumnIndex(DatabaseHelper.CAPSULE_TITLE)
                if (columnIndex != -1) { // 유효한 열 인덱스인지 확인
                    val title = cursor.getString(columnIndex)
                    itemList.add(Item.Item1(title))
                    Log.e("추가된 캡슐 제목", "$title")
                } else {
                    // 예외 처리 또는 로그 출력
                    Log.e("CursorError", "Invalid column index for CAPSULE_TITLE")
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        Log.e("ㅑㅅㄷㅁㄴㄹㅁㄴㄹㅁㄴㄹㅁㄴㄹㄴㅁ", "$itemList")
        return itemList
    }
}
