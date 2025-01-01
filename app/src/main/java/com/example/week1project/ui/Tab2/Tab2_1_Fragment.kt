package com.example.week1project.ui.Tab2
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week1project.DatabaseHelper
import com.example.week1project.R
import com.google.android.material.button.MaterialButton
import androidx.navigation.findNavController // 이 부분 추가

class Tab2_1_Fragment : Fragment() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Tab2_PhotoAdapter
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("탭2생성확인", "예")
        return inflater.inflate(R.layout.fragment_tab2_1, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 이전 BackStack 제거
        requireActivity().supportFragmentManager.popBackStack(
            null,
            androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

        // DatabaseHelper 초기화
        databaseHelper = DatabaseHelper(requireContext())

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        adapter = Tab2_PhotoAdapter(photoList, isDeleteAction = true) { position, id ->
            // 삭제 로직
            val success = databaseHelper.deleteImageFromGallery(id)
            if (success) {
                photoList.removeAt(position) // RecyclerView 업데이트
                adapter.notifyItemRemoved(position)
                Toast.makeText(requireContext(), "사진이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "삭제 실패", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.adapter = adapter
        // 권한 확인
        checkStoragePermission()
        // 기존 데이터 불러오기
        loadPhotosFromDatabase()

        // 버튼 클릭 이벤트
        val selectPhotosButton = view.findViewById<MaterialButton>(R.id.selectPhotosButton)
        selectPhotosButton.setOnClickListener {
            requestGalleryLauncher.launch("image/*")
        }
        val goToTab2_2Button = view.findViewById<MaterialButton>(R.id.nextButton)
        goToTab2_2Button.setOnClickListener {
            // Tab2_2_Fragment로 이동
            Log.d("asdf", "adsf")
            val navController = view.findNavController() // Fragment의 뷰에서 호출
            navController.navigate(R.id.navigation_tab2_2) // Navigation Graph에 정의된 ID로 이동
        }
    }

    private fun resetRecyclerView() {
        if (photoList.isNotEmpty()) {
            Log.d("RecyclerView 초기화", "RecyclerView는 이미 초기화되어 있습니다.")
            return
        }
        loadPhotosFromDatabase()
        Log.d("RecyclerView 초기화", "RecyclerView 초기화 완료")
    }

    override fun onResume() {
        super.onResume()
        resetRecyclerView() // Fragment가 다시 표시될 때 RecyclerView 초기화
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
            requireContext().contentResolver.openInputStream(uri)?.use { it.readBytes() } // BLOB 데이터로 변환
        } catch (e: Exception) {
            Log.e("getImageBlobFromUri", "Error: ${e.message}")
            null
        }
    }
}