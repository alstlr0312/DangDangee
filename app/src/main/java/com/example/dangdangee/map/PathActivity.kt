package com.example.dangdangee.map

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.databinding.ActivityPathBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource

class PathActivity: AppCompatActivity(), OnMapReadyCallback {
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private var markers = mutableListOf<MapModel>()
    private lateinit var key : String
    private val path = PathOverlay()

    private val binding by lazy { ActivityPathBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        key = intent.getStringExtra("key")!!
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_path_frame) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_path_frame, it).commit()
            }
        mapFragment.getMapAsync(this)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
    //맵 레디 콜백
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        //지도 영역 처리
        naverMap.minZoom = 5.0 //최소 줌
        val northWest = LatLng(31.43, 122.37) //서북단
        val southEast = LatLng(44.35, 132.0) //동남단
        naverMap.extent = LatLngBounds(northWest, southEast) //지도 영역을 국내 위주로 축소

        //현재 위치 사용 처리
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow //위치 변해도 지도 안움직임
        naverMap.uiSettings.isLocationButtonEnabled = true //현재 위치 버튼 활성화
        val queryListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val marker = snapshot.getValue(MapModel::class.java)
                if(marker!!.key == key)
                    addMarker(marker, snapshot.key!!, naverMap)
                updatePath()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildChanged: ${snapshot.key}")
                val marker = snapshot.getValue(MapModel::class.java)
                updateMarker(marker!!, snapshot.key!!)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d(ContentValues.TAG, "onChildRemoved:" + snapshot.key!!)
                removeMarker(snapshot.key!!)
                updatePath()

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildMoved:" + snapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "postComments:onCancelled", databaseError.toException())
            }
        }
        FBRef.mapRef.addChildEventListener(queryListener)
    }

    fun updatePath(){
        path.map = null
        val coords : ArrayList<LatLng> = ArrayList()
        for(i in markers.indices){
            if(markers[i].marker!!.map != null)
                coords.add(LatLng(markers[i].lat, markers[i].lng))
        }
        if(coords.size >= 2) {
            path.coords = coords
            path.width = 30
            path.outlineWidth = 0
            path.patternImage = OverlayImage.fromResource(R.drawable.path_pattern)
            path.patternInterval =
                resources.getDimensionPixelSize(R.dimen.overlay_pattern_interval)
            path.color = Color.rgb(63, 121, 255)
            path.map = naverMap
        }
    }
    //마커 & 정보창 등록 함수
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addMarker(mapModel:MapModel, mid: String, naverMap: NaverMap) {
        val marker = Marker()
        marker.position = LatLng(mapModel.lat, mapModel.lng)
        marker.tag = mapModel.tag //최초 등록 or 경로 구분 태그
        marker.captionText = mapModel.name //캡션, 동물 이름
        marker.icon = OverlayImage.fromResource(R.drawable.ic_baseline_pets_24) //아이콘

        marker.width = Marker.SIZE_AUTO //자동 사이즈
        marker.height = Marker.SIZE_AUTO //자동사이즈
        marker.isIconPerspectiveEnabled = true //원근감

        marker.captionRequestedWidth = 200 //캡션 길이
        marker.captionMinZoom = 12.0 //캡션 보이는 범위

        marker.anchor = PointF(0.5f, 0.5f) //마커 이미지가 선택 지점 중앙에 위치하도록

        marker.map = naverMap

        //마커 등록 시험 & 누르면 bottomsheet로 정보 띄움
        marker.onClickListener = Overlay.OnClickListener {
            val bottomSheet = MapBottomSheetFragment()
            bottomSheet.name = mapModel.name
            bottomSheet.address = mapModel.address
            bottomSheet.breed = mapModel.breed
            bottomSheet.img = resources.getDrawable(R.drawable.map_sample_dog, null)
            bottomSheet.key = mapModel.key //수정 필요
            bottomSheet.flag = false //true면 경로 보기 버튼 뜸, false면 경로 보기 버튼 안 뜸(이미 경로 액티비티일 때)
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            false
        }
        mapModel.marker = marker
        mapModel.mid = mid
        markers.add(mapModel)
    }
    fun updateMarker(mapModel: MapModel, mid: String){
        for(i in markers.indices) {
            if(markers[i].mid == mid)
                markers[i].marker?.map = null
        }
        addMarker(mapModel, key, naverMap)
    }
    fun removeMarker(mid: String){
        for(i in markers.indices) {
            if(markers[i].mid == mid)
                markers[i].marker?.map = null
        }
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}