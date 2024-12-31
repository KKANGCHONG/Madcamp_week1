package com.example.madcamp_week1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Tab1_2_MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.capsule_image)

        // ImageView 연결 시키기
        val capsuleImage : ImageView = findViewById(R.id.CapsuleImage)
        val notificationsImage : ImageView = findViewById(R.id.NotificationsImage)
        val cogwheelImage : ImageView = findViewById(R.id.CogwheelImage)
        val title = intent.getStringExtra("title")

        // 이미지 누르면 capsule_open 으로 전환됨
        capsuleImage.setOnClickListener{
            capsuleImage.setImageResource(R.drawable.capsule_open)

            // 3초 후 다음 화면으로 전환
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, Tab1_3_MainActivity::class.java)
                intent.putExtra("title", title) // 데이터 전달
                startActivity(intent)
                finish() // 현재 액티비티 종료
            }, 3000) // 3000ms = 3초
        }

    }
}