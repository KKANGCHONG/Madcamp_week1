package com.example.week1project.ui.Tab2

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week1project.DatabaseHelper
import com.example.week1project.R

class Tab2_2_Fragment : Fragment() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Tab2_PhotoAdapter
    private val selectedImageIds = mutableSetOf<Long>()
    private val photoList = mutableListOf<Pair<Long, ByteArray>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 레이아웃을 인플레이트
        return inflater.inflate(R.layout.fragment_tab2_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DatabaseHelper 초기화
        databaseHelper = DatabaseHelper(requireContext())

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = Tab2_PhotoAdapter(photoList, isDeleteAction = false) { position, id ->
            if (selectedImageIds.contains(id)) {
                selectedImageIds.remove(id)
            } else {
                selectedImageIds.add(id)
            }
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = adapter

        // 권한 확인
        checkStoragePermission()

        // 기존 데이터 불러오기
        loadPhotosFromDatabase()

        // 버튼 클릭 이벤트
        val capsulePhotosButton = view.findViewById<Button>(R.id.capsulePhotosButton)
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
                    Toast.makeText(requireContext(), "캡슐이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "캡슐 생성 실패", Toast.LENGTH_SHORT).show()
                    Log.e("CAPSULE", "캡슐 생성 실패, 선택된 이미지: $selectedImageIds")
                }
            } else {
                Toast.makeText(requireContext(), "선택된 사진이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        val goToTab2_3Button = view.findViewById<Button>(R.id.nextButton2)
        goToTab2_3Button.setOnClickListener {
            // 다음 Fragment로 이동
            val latestCapsuleId = databaseHelper.getLatestCapsuleId()
            if (latestCapsuleId != null) {
                val nextFragment = Tab2_3_Fragment()
                val bundle = Bundle()
                bundle.putLong("capsuleId", latestCapsuleId)
                nextFragment.arguments = bundle

                requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, nextFragment) // fragment_container는 메인 레이아웃의 Fragment 컨테이너 ID
                    .addToBackStack(null) // 뒤로 가기 버튼으로 이전 Fragment로 돌아갈 수 있도록 설정
                    .commit()
            } else {
                Toast.makeText(requireContext(), "캡슐이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
            requireContext().contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (e: Exception) {
            Log.e("getImageBlobFromUri", "Error: ${e.message}")
            null
        }
    }
}

