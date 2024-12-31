package com.example.week1project.ui.Tab1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.week1project.R

class Tab1_2_Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Layout inflate
        return inflater.inflate(R.layout.show_capsule_image, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // ImageView 연결
        val capsuleImage: ImageView = view.findViewById(R.id.CapsuleImage)
        val notificationsImage: ImageView = view.findViewById(R.id.NotificationsImage)
        val cogwheelImage: ImageView = view.findViewById(R.id.CogwheelImage)
        val title = arguments?.getString("title")
        // 이미지 누르면 capsule_open으로 전환됨
        capsuleImage.setOnClickListener {
            capsuleImage.setImageResource(R.drawable.capsule_open)
            // 3초 후 다음 화면으로 전환
            Handler(Looper.getMainLooper()).postDelayed({
                val context = requireContext()
                val intent = Intent(context, Tab1_3_Fragment::class.java).apply {
                    putExtra("title", "Some Title") // 전달할 데이터
                }
                context.startActivity(intent)
                Log.d("선택된 타이틀 전송", "Some Title")
            }, 3000) // 3000ms = 3초
        }
    }
}