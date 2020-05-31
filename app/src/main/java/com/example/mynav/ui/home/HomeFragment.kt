package com.example.mynav.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mynav.R
import com.example.mynav.ui.getJsonData.GetGSONData
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_slideshow.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.InputStream
import java.lang.Exception
import java.util.concurrent.Executors

class HomeFragment : Fragment()  {
    companion object{
        private val PERMISSION_ID = 42
        val instance : HomeFragment by lazy {
            HomeFragment()
        }
    }
    private var whereIClickX:String = ""
    private var whereIClickY:String = ""
    private lateinit var mMap:GoogleMap
    private lateinit var jsonArray: JSONArray
    private lateinit var readData: String
    private lateinit var data: InputStream
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val taichung = LatLng(24.163736, 120.637631)
        //在變數taichung裡面加入marker並且將中心點設其地點
        googleMap.addMarker(MarkerOptions().position(taichung).title("Marker in Taichung11"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(taichung))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(taichung,15f))
        data = resources.openRawResource(R.raw.data)
        readData = data.bufferedReader().use { it.readText() }
        jsonArray = JSONArray(readData)
        //var getGSONData = GetGSONData()
        //request
        CoroutineScope(Dispatchers.IO).launch{

//            getGSONData.handleJson()
//            val dataArray = getGSONData.jSonArrayfromGetGSONData
//            //用來為地圖標記
//            withContext(Dispatchers.Main){
//                for (i in 0 until jsonArray.length()) {
//                    var x = dataArray.getJSONObject(i).getString("X").toDouble()
//                    var y = dataArray.getJSONObject(i).getString("Y").toDouble()
//                    var position = dataArray.getJSONObject(i).getString("Position").toString()
//                    var availableCNT =
//                        dataArray.getJSONObject(i).getString("AvailableCNT").toString()
//                    var empCNT = dataArray.getJSONObject(i).getString("EmpCNT").toString()
//                    var ll = LatLng(y, x)
//                    //為標點加上marker同時給予名稱以及數量的資訊
//                    googleMap.addMarker(
//                        MarkerOptions()
//                            .position(ll).title("$position")
//                            .snippet("可借數量:$availableCNT , 停車格量:$empCNT")
//                    )
//                }
//            }
        }

        for (i in 0 until jsonArray.length()) {
            var x = jsonArray.getJSONObject(i).getString("X").toDouble()
            var y = jsonArray.getJSONObject(i).getString("Y").toDouble()
            var position = jsonArray.getJSONObject(i).getString("Position").toString()
            var availableCNT = jsonArray.getJSONObject(i).getString("AvailableCNT").toString()
            var empCNT = jsonArray.getJSONObject(i).getString("EmpCNT").toString()
            var ll = LatLng(y,x)
            //為標點加上marker同時給予名稱以及數量的資訊
            googleMap.addMarker(MarkerOptions()
                .position(ll).title("$position")
                .snippet("可借數量:$availableCNT , 停車格量:$empCNT"))
        }
        //顯示目前位置的按鈕
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        //可放大縮小的按鈕
        googleMap.uiSettings.isZoomControlsEnabled = true
        //可提供我的位置
        //googleMap.isMyLocationEnabled =true

        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        var x = whereIClickX
        var y = whereIClickY
        if (whereIClickX.isNotEmpty() && whereIClickY.isNotEmpty()) {
            var ll = LatLng(y.toDouble(),x.toDouble())
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,15f))
            Toast.makeText(context,"Here We GO $ll",Toast.LENGTH_SHORT).show()
        }
    }

    fun takeMeToSomeWhereIClick(x: String, y: String){
        Log.d("take","$x  ,  $y")
        whereIClickX = x
        whereIClickY = y
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this.requireActivity()) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        var aaaa = location.latitude.toString()
                        var bbbb = location.longitude.toString()
                       println("-------------${location.latitude}------------------")
                        println("-----------${location.longitude}-----------------")
                    }
                }
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //採用高精準度的方式
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            println("-------------${mLastLocation.latitude.toString()}--------------------------")
            println("-------------${mLastLocation.longitude.toString()}-------------------------***-")
            Log.d("mLocationCallback","mLocationCallback")
        }
    }



    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        Log.d("isLocationEnabled","isLocationEnabled")
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("checkPermissions","True")
            return true
        }
        Log.d("checkPermissions","False")
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this.requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
        Log.d("requestPermissions","come in")
    }

    //檢查權限，如果過就可以使用 getLastLocation()
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
           } catch (e:Exception){
           Toast.makeText(context,"You should not pass,cuz u don't give permission",Toast.LENGTH_LONG).show()
       }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        getLastLocation()
    }
}