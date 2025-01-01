package com.example.week1project.ui.Tab2

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.week1project.DatabaseHelper
import com.example.week1project.R
import com.google.android.material.button.MaterialButton

class Tab2_3_Fragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private var capsuleId: Long = -1L // 전달받은 캡슐 ID 저장

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab2_3, container, false)

        // DatabaseHelper 초기화
        databaseHelper = DatabaseHelper(requireContext())

        val editTextTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editTextDate = view.findViewById<EditText>(R.id.editTextDate)
        val editTextText = view.findViewById<EditText>(R.id.editTextTextMultiLine)

        // 이전 화면에서 전달된 캡슐 ID 가져오기
        capsuleId = arguments?.getLong("capsuleId") ?: -1L

        if (capsuleId == -1L) {
            Toast.makeText(requireContext(), "캡슐 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack() // 이전 화면으로 돌아가기
            return view
        }

        // Next 버튼 클릭 이벤트
        val nextButton = view.findViewById<MaterialButton>(R.id.nextButton)
        nextButton.setOnClickListener {
            // 캡슐 삽입 (필요한 다른 필드 값은 임시로 설정)
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

            // 캡슐 로그 확인 (디버깅용)
            databaseHelper.logCapsules()

            // Tab2_4_Fragment로 이동
            val nextFragment = Tab2_4_Fragment().apply {
                arguments = Bundle().apply {
                    putLong("capsuleId", capsuleId) // 동일한 캡슐 ID 전달
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, Tab2_4_Fragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
