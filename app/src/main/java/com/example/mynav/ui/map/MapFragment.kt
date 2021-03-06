package com.example.mynav.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mynav.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.InputStream
import java.lang.Exception

class MapFragment : Fragment(), SearchView.OnQueryTextListener  {
    companion object{
        private val PERMISSION_ID = 42
        val instance : MapFragment by lazy {
            MapFragment()
        }
    }
    private var whereIClickX:String = ""
    private var whereIClickY:String = ""
    private var myLocation : Boolean = false
    private var mMap: GoogleMap? = null
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

        if (whereIClickX.isNotEmpty() && whereIClickY.isNotEmpty()) {
            var x = whereIClickX
            var y = whereIClickY
            var ll = LatLng(y.toDouble(),x.toDouble())
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ll))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,16f))

            Toast.makeText(context,"Going to $ll",Toast.LENGTH_SHORT).show()
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(taichung))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(taichung, 16f))
        }
        data = resources.openRawResource(R.raw.data)
        readData = data.bufferedReader().use { it.readText() }
        jsonArray = JSONArray(readData)
        //var getGSONData = GetGSONData()
        //request
        CoroutineScope(Dispatchers.IO).launch{
/*
            getGSONData.handleJson()
            val dataArray = getGSONData.jSonArrayfromGetGSONData
            //用來為地圖標記
            withContext(Dispatchers.Main){
                for (i in 0 until jsonArray.length()) {
                    var x = dataArray.getJSONObject(i).getString("X").toDouble()
                    var y = dataArray.getJSONObject(i).getString("Y").toDouble()
                    var position = dataArray.getJSONObject(i).getString("Position").toString()
                    var availableCNT =
                        dataArray.getJSONObject(i).getString("AvailableCNT").toString()
                    var empCNT = dataArray.getJSONObject(i).getString("EmpCNT").toString()
                    var ll = LatLng(y, x)
                    //為標點加上marker同時給予名稱以及數量的資訊
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(ll).title("$position")
                            .snippet("可借數量:$availableCNT , 停車格量:$empCNT")
                    )
                }
            }
            */
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
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike32x32))
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main,menu)
        val searchItem = menu.findItem(R.id.action_search)
        if (searchItem != null){
            val searchView = searchItem.actionView as SearchView
            searchView.queryHint = "Search.."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchView.suggestionsAdapter
//                    if (query == displayList[0]) {
//                    }
//                    Toast.makeText(this@MainActivity,"Searching...$query",Toast.LENGTH_SHORT).show()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    /*
                    displayList.clear()
                    if (newText.isNullOrEmpty()) {
                        val search = newText
                        displayList.forEach{
                            if (it.contains(search)) {
                                displayList.add(it)
                            }
                        }
                    } else {
                        displayList.addAll(array)
                    }
                    */
                    //adapter.filter.filter(newText)
                    return true
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }



    fun takeMeToSomeWhereIClicked(x: String, y: String){
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
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
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
            myLocation = true
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG:Fragment","Map = ${instance.id}")
        myLocation = true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("Not yet implemented")
    }
}