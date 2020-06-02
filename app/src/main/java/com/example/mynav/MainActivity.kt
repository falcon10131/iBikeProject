package com.example.mynav

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.core.view.get
import com.example.mynav.ui.BikeCeter.Adapter
import com.example.mynav.ui.BikeCeter.BikeCenterFragment
import com.example.mynav.ui.map.MapFragment
import com.example.mynav.ui.webview.WebViewFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_slideshow.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{
    /*
    //2020-06-01-17:53
    */
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var dataArray:JSONArray
    private var array:List<String> = ArrayList()
    private lateinit var iBike : iBikeModelItem
    var displayList:MutableList<String> = ArrayList()
    val fragmentManager = supportFragmentManager
    lateinit var adapter:ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
/*
        CoroutineScope(Dispatchers.IO).launch {
            val json = URL("http://e-traffic.taichung.gov.tw/DataAPI/api/YoubikeAllAPI")
                .readText()
            iBike = Gson().fromJson(json,
                object : TypeToken<iBikeModelItem>() {}.type)
            array = listOf(iBike.toString())
        }//launch
*/
        okHttpRequest()

        /*
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener{
        }
        */
//        val listView:ListView = findViewById(R.id.listView)
//        adapter = ArrayAdapter(this@MainActivity,android.R.layout.simple_list_item_1,array)
//        listView.adapter = adapter
        toggle()
        initActivity()
    }
    private fun okHttpRequest(){
        var client: OkHttpClient = OkHttpClient()
        var jSonArrayFromGetGSONData = JSONArray()
        val request = Request.Builder()
            .url("http://e-traffic.taichung.gov.tw/DataAPI/api/YoubikeAllAPI")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
            }
            @Throws(IOException::class)
            override fun onResponse(response: Response){
                CoroutineScope(Dispatchers.IO).launch {
                    val resStr = response.body().string()
                    dataArray = JSONArray(resStr)
                    for (i in 0 until dataArray.length()){
                        displayList.add(dataArray.getJSONObject(i).get("Position").toString())
                    }
                    Log.d("arr","$displayList")
                }
            }
        })
    }


    private fun toggle(){
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }
    private fun initActivity(){
        setSupportActionBar(toolbar)

         supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_camera)
        }
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, MapFragment.instance).commit()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        if (searchItem != null){
            val searchView = searchItem.actionView as SearchView
            searchView.queryHint = "Search.."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchView.suggestionsAdapter
                   if (query == displayList[0]) {

                   }
                    Toast.makeText(this@MainActivity,"Searching...$query",Toast.LENGTH_SHORT).show()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    displayList.clear()
                    if (newText.isNotEmpty()) {
                        val search = newText.toLowerCase()
                        array.forEach{
                            if (it.toLowerCase().contains(search)) {
                                displayList.add(it)
                            }
                        }
                    } else {
                        displayList.addAll(array)
                    }
                    rcv.adapter?.notifyDataSetChanged()
                    return true
                }
            })
        }
        return true
    }



    //都側邊抽屜被打開時觸發此fun
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }



    fun getnotifi(title: String, text: String){
        val notificationManager = NotificationCompat.Builder(this,"Click")
           //.setSmallIcon(R.drawable.avatar1)
            .setContentTitle("$title")
            .setContentText("$text")
            .build()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                supportActionBar?.title = "台中市iBike"
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,MapFragment.instance).commit()
            }
            R.id.nav_gallery -> {
                supportActionBar?.title = "Youbike官網"
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,WebViewFragment.instance).commit()
            }
            R.id.nav_slideshow -> {
                supportActionBar?.title = "站點中心"
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,BikeCenterFragment.instance).commit()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}



