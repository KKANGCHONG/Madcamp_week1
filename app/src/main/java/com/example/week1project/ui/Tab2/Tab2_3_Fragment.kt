package com.example.week1project.ui.Tab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.week1project.DatabaseHelper
import com.example.week1project.R
import com.google.android.material.button.MaterialButton

class Tab2_3_Fragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private var capsuleId: Long = -1L // 전달받은 캡슐 ID 저장
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDate: EditText
    private lateinit var editTextText: EditText
    private lateinit var nextButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab2_3, container, false)

        // DatabaseHelper 초기화
        databaseHelper = DatabaseHelper(requireContext())

        editTextTitle = view.findViewById(R.id.editTextTitle)
        editTextDate = view.findViewById(R.id.editTextDate)
        editTextText = view.findViewById(R.id.editTextTextMultiLine)
        nextButton = view.findViewById(R.id.nextButton)

        // 이전 화면에서 전달된 캡슐 ID 가져오기
        capsuleId = arguments?.getLong("capsuleId") ?: -1L

        if (capsuleId == -1L) {
            Toast.makeText(requireContext(), "캡슐 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack() // 이전 화면으로 돌아가기
            return view
        }

        // Next 버튼 클릭 이벤트
        nextButton.setOnClickListener {
            val title = editTextTitle.text.toString().trim()
            val date = editTextDate.text.toString().trim()
            val text = editTextText.text.toString().trim()

            if (title.isEmpty() || date.isEmpty() || text.isEmpty()) {
                Toast.makeText(requireContext(), "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 데이터베이스 업데이트
            val isUpdated = databaseHelper.updateCapsuleTitle(capsuleId, title) &&
                    databaseHelper.updateCapsuleDate(capsuleId, date) &&
                    databaseHelper.updateCapsuleText(capsuleId, text)

            if (isUpdated) {
                Toast.makeText(requireContext(), "캡슐이 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "캡슐 업데이트 실패.", Toast.LENGTH_SHORT).show()
            }

            // Tab2_4_Fragment로 이동
            val nextFragment = Tab2_4_Fragment().apply {
                arguments = Bundle().apply {
                    putLong("capsuleId", capsuleId) // 동일한 캡슐 ID 전달
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, nextFragment)
                .addToBackStack(null)
                .commit()
        }

        // 뒤로 가기 버튼 동작 커스터마이즈
        handleBackPress()

        return view
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로 가기 동작 커스터마이즈: 현재 Fragment를 다시 표시
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, this@Tab2_3_Fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
    }
}
