package com.example.madcamp_week1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class Tab2_3_MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab2_3_activity_main)

        // Next 버튼 클릭 이벤트
        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            val intent = Intent(this, Tab2_4_MainActivity::class.java)
            startActivity(intent)
        }
    }
}