package com.example.madcamp_week1

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Tab2_2_MainActivity : ComponentActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Tab2_PhotoAdapter
    private val selectedImageIds = mutableSetOf<Long>()
    private val photoList = mutableListOf<Pair<Long, ByteArray>>() // 변경된 photoList 타입


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab2_2_activity_main)

        //deleteDatabase()

        // DatabaseHelper 초기화
        databaseHelper = DatabaseHelper(this)

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView)
        adapter = Tab2_PhotoAdapter(photoList, isDeleteAction = false) { position, id ->
            if (selectedImageIds.contains(id)) {selectedImageIds.remove(id)} else {selectedImageIds.add(id)}
        }
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter

        // 권한 확인
        checkStoragePermission()

        // 기존 데이터 불러오기
        loadPhotosFromDatabase()

        // 버튼 클릭 이벤트
        val capsulePhotosButton = findViewById<Button>(R.id.capsulePhotosButton)  // 캡슐 DB에 넣는 동작 구현 필요
        capsulePhotosButton.setOnClickListener {
            if (selectedImageIds.isNotEmpty()) {
                val capsuleId = databaseHelper.insertCapsule(
                    title = "My Capsule",
                    text = "Capsule Description",
                    date = "2023-12-31", // 현재 날짜를 동적으로 설정하려면 SimpleDateFormat 활용
                    location = "Some Location",
                    imageIds = selectedImageIds.toList()
                )
                databaseHelper.logCapsules()
                if (capsuleId != -1L) {
                    Toast.makeText(this, "캡슐이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "캡슐 생성 실패", Toast.LENGTH_SHORT).show()
                    Log.e("CAPSULE", "캡슐 생성 실패, 선택된 이미지: $selectedImageIds")
                }
            } else {
                Toast.makeText(this, "선택된 사진이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        val goToTab2_3Button = findViewById<Button>(R.id.nextButton2)

        // 버튼 클릭 리스너 설정
        goToTab2_3Button.setOnClickListener {
            // Tab2_3_MainActivity로 이동
            val latestCapsuleId = databaseHelper.getLatestCapsuleId()
            if (latestCapsuleId != null) {
                val intent = Intent(this, Tab2_3_MainActivity::class.java)
                intent.putExtra("capsuleId", latestCapsuleId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "캡슐이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }
    }

    private fun loadPhotosFromDatabase() {
        // photoList를 초기화하지 않고 데이터베이스에서 읽은 데이터를 추가
        val photos = databaseHelper.getAllImagesFromGallery()
        Log.d("loadPhotosFromDatabase", "불러온 사진 수: ${photos.size}")
        if (photos.isNotEmpty()) {
            photoList.clear() // 중복 방지를 위해 기존 리스트 초기화
            photoList.addAll(photos) // DB에서 가져온 모든 사진 추가
            photoList.sortByDescending { it.first } // Long 값을 기준으로 내림차순 정렬
            adapter.notifyDataSetChanged() // RecyclerView 업데이트
        } else {
            Log.d("loadPhotosFromDatabase", "데이터베이스에 저장된 사진이 없습니다.")
        }
    }

    private fun deleteDatabase() { //이전 갤러리 기록이 있는 디바이스의 경우 1회 호출 후 앱 재실행 필요
        val isDeleted = deleteDatabase(DatabaseHelper.DATABASE_NAME)
        if (isDeleted) {
            Toast.makeText(this, "데이터베이스 삭제 완료", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "데이터베이스 삭제 실패", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImageBlobFromUri(uri: Uri): ByteArray? {
        return try {
            contentResolver.openInputStream(uri)?.use { it.readBytes() } // BLOB 데이터로 변환
        } catch (e: Exception) {
            Log.e("getImageBlobFromUri", "Error: ${e.message}")
            null
        }
    }
}