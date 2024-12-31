package com.example.madcamp_week1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.DatabaseHelper.Companion.CAPSULE_ID
import com.example.madcamp_week1.DatabaseHelper.Companion.CAPSULE_TITLE

class Tab1_3_MainActivity : ComponentActivity() {
    private lateinit var databaseHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab1_3_activity_main)

        // DatabaseHelper 초기화
        databaseHelper = DatabaseHelper(this)

        // RecyclerView 초기화
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val title1 = intent.getStringExtra("title")
        val capsuleid = getCapsuleIdViaTitle(title1)

        val items = listOf(
            Item.TypeA(title = databaseHelper.getTitleForCapsule(capsuleid).toString(),
                text = databaseHelper.getTextForCapsule(capsuleid).toString(),
                date = databaseHelper.getDateForCapsule(capsuleid).toString()),
            Item.TypeB(imageRes = databaseHelper.getImagesForCapsule(capsuleid)),
            Item.TypeC(location = "paris")
        )

        recyclerView.adapter = Tab1_3_Adapter(items)


    }


    private fun getCapsuleIdViaTitle(title: String?): Long {
        val itemList = mutableListOf<com.example.madcamp_week1.Item>()
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val query =
            "SELECT $CAPSULE_ID AS 캡슐_아이디 FROM ${DatabaseHelper.TABLE_NAME1} GROUP BY $CAPSULE_TITLE"
        val cursor = db.rawQuery(query, null)

        var capsuleId: Long = -1 // 기본값: 찾지 못한 경우를 위한 값

        // 결과 처리
        if (cursor.moveToFirst()) {
            capsuleId = cursor.getLong(cursor.getColumnIndexOrThrow(CAPSULE_ID))
        }

        // 리소스 해제
        cursor.close()
        db.close()

        return capsuleId
    }
}
