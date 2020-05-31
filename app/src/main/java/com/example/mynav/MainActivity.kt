package com.example.mynav

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.transition.TransitionManager
import android.view.Menu
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.fragment.app.FragmentManager
import com.example.mynav.ui.home.HomeFragment
import kotlinx.android.synthetic.main.nav_header_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    /*
    //2020-05-31-23:22
    */
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val mag = supportFragmentManager
        val tra = mag.beginTransaction()

    }

    fun repla(){
        supportFragmentManager.beginTransaction().apply {

            replace(R.id.nav_host_fragment,HomeFragment.instance)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    //都側邊抽屜被打開時觸發此fun
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun getnotifi(title: String, text: String){
        val notificationManager = NotificationCompat.Builder(this,"Click")
           //.setSmallIcon(R.drawable.avatar1)
            .setContentTitle("$title")
            .setContentText("$text")
            .build()
    }

}



