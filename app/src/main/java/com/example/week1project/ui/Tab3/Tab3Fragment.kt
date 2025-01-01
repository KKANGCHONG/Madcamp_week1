package com.example.week1project.ui.Tab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week1project.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.MapView

class Tab3Fragment : Fragment() {

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mapView: MapView
    private var isMapInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab3, container, false) // fragment_tab3.xml과 연결
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FusedLocationSource 초기화 (현재 위치를 추적하기 위해 사용)
        locationSource = FusedLocationSource(this, 1000)

        // MapView 초기화
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        // 지도 초기화
        if (!isMapInitialized) {
            mapView.getMapAsync { naverMap ->
                this.naverMap = naverMap
                this.isMapInitialized = true

                // 지도 설정
                setupMap(naverMap)

                // 전달받은 lat, lng, name으로 마커 설정
                val lat: Double = arguments?.getDouble("lat") ?: -1.0
                val lng: Double = arguments?.getDouble("lng") ?: -1.0
                val name: String = arguments?.getString("name") ?: ""

                if (lat != -1.0 && lng != -1.0 && name.isNotEmpty()) {
                    val marker = Marker()
                    setMarker(marker, lat, lng, name)
                }
            }
        }
    }

    private fun setupMap(naverMap: NaverMap) {
        // 현재 위치 소스 설정
        naverMap.locationSource = locationSource

        // 사용자 현재 위치 추적 활성화
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        // 지도 UI 설정
        val uiSettings = naverMap.uiSettings
        uiSettings.isZoomControlEnabled = true // 줌 컨트롤 활성화
        uiSettings.isCompassEnabled = true // 나침반 활성화
    }

    private fun setMarker(marker: Marker, lat: Double, lng: Double, name: String) {
        marker.position = LatLng(lat, lng)
        marker.captionText = name // 마커에 이름 표시
        marker.icon = OverlayImage.fromResource(R.drawable.marker_icon) // 마커 아이콘 설정
        marker.map = naverMap // 마커를 지도에 추가
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
