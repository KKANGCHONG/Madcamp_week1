package com.example.week1project.ui.Tab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week1project.DatabaseHelper
import com.example.week1project.DatabaseHelper.Companion.CAPSULE_ID
import com.example.week1project.DatabaseHelper.Companion.CAPSULE_TITLE
import com.example.week1project.R
import com.example.week1project.ui.Tab2.Tab2_1_Fragment
import com.google.android.material.button.MaterialButton

class Tab1_3_Fragment : Fragment() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab1_3, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // DatabaseHelper 초기화
        databaseHelper = DatabaseHelper(requireContext())
        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recyclerView)
        // 전달받은 title 처리
        val title1 = arguments?.getString("title")
        // 캡슐 ID 가져오기
        val capsuleId = getCapsuleIdViaTitle(title1)
        // 데이터 리스트 생성
        val items = listOf(
            Item.TypeA(
                title = databaseHelper.getTitleForCapsule(capsuleId).toString(),
                text = databaseHelper.getTextForCapsule(capsuleId).toString(),
                date = databaseHelper.getDateForCapsule(capsuleId).toString()
            ),
            Item.TypeB(imageRes = databaseHelper.getImagesForCapsule(capsuleId)),
            Item.TypeC(location = "")
        )
        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = Tab1_3_Adapter(items).apply {
            // DatabaseHelper 전달
            this.databaseHelper = this@Tab1_3_Fragment.databaseHelper
        }

    }
    private fun getCapsuleIdViaTitle(title: String?): Long {
        val db = databaseHelper.readableDatabase
        val query = "SELECT $CAPSULE_ID FROM ${DatabaseHelper.TABLE_NAME1} WHERE $CAPSULE_TITLE = ?"
        val cursor = db.rawQuery(query, arrayOf(title))
        var capsuleId: Long = -1 // 기본값: 찾지 못한 경우를 위한 값
        if (cursor.moveToFirst()) {
            capsuleId = cursor.getLong(cursor.getColumnIndexOrThrow(CAPSULE_ID))
        }
        cursor.close()
        db.close()
        return capsuleId
    }
}