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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Tab2_1_MainActivity : ComponentActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhotoAdapter
    private val photoList = mutableListOf<Pair<Long, ByteArray>>() // 변경된 photoList 타입

    private val requestGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        uris?.forEach { uri ->
            val imageBlob = getImageBlobFromUri(uri)
            if (imageBlob != null) {
                val galleryId = databaseHelper.insertImageToGallery(imageBlob)
                if (galleryId != -1L) {
                    Log.d("BLOB 저장", "저장된 Gallery ID: $galleryId")
                    photoList.add(Pair(galleryId, imageBlob))
                    adapter.notifyItemInserted(photoList.size - 1) // RecyclerView 업데이트
                } else {
                    Log.e("BLOB 저장", "Gallery 테이블에 삽입 실패")
                }
            } else {
                Log.e("BLOB 변환 실패", "URI: $uri")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab2_1_activity_main)

        // DatabaseHelper 초기화
        databaseHelper = DatabaseHelper(this)

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView)
        adapter = PhotoAdapter(photoList,isDeleteAction = true) { position, id ->
            // 삭제 로직
            val success = databaseHelper.deleteImageFromGallery(id)
            if (success) {
                photoList.removeAt(position) // RecyclerView 업데이트
                adapter.notifyItemRemoved(position)
                Toast.makeText(this, "사진이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "삭제 실패", Toast.LENGTH_SHORT).show()
            }
        } // photoList를 어댑터에 전달
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter

        // 권한 확인
        checkStoragePermission()

        // 기존 데이터 불러오기
        loadPhotosFromDatabase()

        // 버튼 클릭 이벤트
        val selectPhotosButton = findViewById<Button>(R.id.selectPhotosButton)
        selectPhotosButton.setOnClickListener {
            requestGalleryLauncher.launch("image/*")
        }

        val goToTab2_2Button = findViewById<Button>(R.id.nextButton)

        // 버튼 클릭 리스너 설정
        goToTab2_2Button.setOnClickListener {
            // Tab2_2_MainActivity로 이동
            val intent = Intent(this, Tab2_2_MainActivity::class.java)
            startActivity(intent)
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

    private fun getImageBlobFromUri(uri: Uri): ByteArray? {
        return try {
            contentResolver.openInputStream(uri)?.use { it.readBytes() } // BLOB 데이터로 변환
        } catch (e: Exception) {
            Log.e("getImageBlobFromUri", "Error: ${e.message}")
            null
        }
    }
}