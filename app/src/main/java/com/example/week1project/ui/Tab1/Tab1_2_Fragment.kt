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
import com.example.week1project.R

class Tab1_2_Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Layout inflate
        return inflater.inflate(R.layout.fragment_tab1_2, container, false)
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

                val nextFragment = Tab1_3_Fragment().apply {
                    arguments = Bundle().apply {
                        putString("title", title) // 전달할 데이터
                    }
                }

                // FragmentTransaction을 통해 Fragment 교체
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, nextFragment) // Fragment를 표시할 Container ID
                    .addToBackStack(null) // 뒤로 가기 지원
                    .commit()
                Log.d("선택된 타이틀 전송", "Some Title")
            }, 3000) // 3000ms = 3초
        }
    }
}