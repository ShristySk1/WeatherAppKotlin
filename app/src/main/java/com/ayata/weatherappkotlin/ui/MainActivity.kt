package com.ayata.weatherappkotlin.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.ayata.weatherappkotlin.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

const val MY_PERMISSION_ACCESS_COURSE_LOCATION = 1

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }

    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setUpActionBar work gardaina natra
        setSupportActionBar(toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        navController = navHostFragment.findNavController()
        bottomNavigation.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
        requestLocationPermission()
        if (hasLocationPermission()) {
            bindLocationManager()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COURSE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSION_ACCESS_COURSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bindLocationManager()
            } else {
                Toast.makeText(
                    this,
                    "Please set location manually from settings",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun bindLocationManager() {
        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient,
            locationCallback
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }
}