package com.example.mynav

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import com.example.mynav.ui.BikeCeter.BikeCenterFragment
import com.example.mynav.ui.map.MapFragment
import com.example.mynav.ui.webview.WebViewFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{
    /*
    //2020-06-01-
    */
    private lateinit var appBarConfiguration: AppBarConfiguration
    val fragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        toggle()
        initActivity()
    }

    fun toggle(){
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

    fun replace(){
        this.fragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, MapFragment.instance)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
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



