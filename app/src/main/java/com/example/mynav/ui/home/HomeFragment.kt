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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mynav.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import java.lang.Exception

class HomeFragment : Fragment()  {

    private val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient

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

        var data = resources.openRawResource(R.raw.data)
        var readdata = data.bufferedReader().use { it.readText() }
        var jsonArray = JSONArray(readdata)

        for (i in 0 until jsonArray.length()) {
            var x = jsonArray.getJSONObject(i).getString("X").toDouble()
            var y = jsonArray.getJSONObject(i).getString("Y").toDouble()
            var position = jsonArray.getJSONObject(i).getString("Position").toString()
            var availableCNT = jsonArray.getJSONObject(i).getString("AvailableCNT").toString()
            var empCNT = jsonArray.getJSONObject(i).getString("EmpCNT").toString()
            var ll = LatLng(y,x)

            googleMap.addMarker(MarkerOptions()
                .position(ll).title("$position")
                .snippet("可借數量:$availableCNT , 停車格量:$empCNT"))

        }


        //顯示目前位置的按鈕
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        //可放大縮小的按鈕
        googleMap.uiSettings.isZoomControlsEnabled = true
        //
        googleMap.isMyLocationEnabled =true

        }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)

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
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
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

            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            activity?.runOnUiThread {
                mapFragment?.getMapAsync(callback)
            }
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this.requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
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
           Toast.makeText(context,"Permission needed",Toast.LENGTH_SHORT).show()
       }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        //mapFragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getLastLocation()

    }
}