package com.example.week1project.ui.Tab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week1project.R

class Tab2_6_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment 레이아웃을 인플레이트
        return inflater.inflate(R.layout.fragment_tab2_6, container, false)
    }
}