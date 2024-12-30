package com.example.madcamp_week1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import android.widget.EditText
import android.widget.Toast

class Tab2_3_MainActivity : ComponentActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private var capsuleId: Long = -1L // 전달받은 캡슐 ID 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab2_3_activity_main)

        // DatabaseHelper 초기화
        databaseHelper = DatabaseHelper(this)

        val editTextTitle = findViewById<EditText>(R.id.editTextTitle)
        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val editTextText = findViewById<EditText>(R.id.editTextTextMultiLine)

        // 이전 화면에서 전달된 캡슐 ID 가져오기
        capsuleId = intent.getLongExtra("capsuleId", -1L)

        if (capsuleId == -1L) {
            Toast.makeText(this, "캡슐 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish() // 캡슐 ID가 없으면 화면 종료
            return
        }

        // Next 버튼 클릭 이벤트
        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            // 캡슐 삽입 (필요한 다른 필드 값은 임시로 설정)
            val title = editTextTitle.text.toString().trim()
            val date = editTextDate.text.toString().trim()
            val text = editTextText.text.toString().trim()

            if (title.isEmpty() || date.isEmpty() || text.isEmpty()) {
                Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 데이터베이스 업데이트
            val isUpdated = databaseHelper.updateCapsuleTitle(capsuleId, title) &&
                    databaseHelper.updateCapsuleDate(capsuleId, date) &&
                    databaseHelper.updateCapsuleText(capsuleId, text)

            if (isUpdated) {
                Toast.makeText(this, "캡슐이 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "캡슐 업데이트 실패.", Toast.LENGTH_SHORT).show()
            }

            // 캡슐 로그 확인 (디버깅용)
            databaseHelper.logCapsules()

            // Tab2_4_MainActivity로 이동
            val intent = Intent(this, Tab2_4_MainActivity::class.java)
            intent.putExtra("capsuleId", capsuleId) // 동일한 캡슐 ID 전달
            startActivity(intent)
        }
    }
}