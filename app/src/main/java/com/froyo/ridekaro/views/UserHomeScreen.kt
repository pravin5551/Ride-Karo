package com.froyo.ridekaro.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.froyo.ridekaro.R
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.activity_user_home_screen.*
import java.io.IOException
import java.lang.Exception
import java.util.*

class UserHomeScreen : AppCompatActivity(), OnMapReadyCallback, LocationListener,
    GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraIdleListener {

    private var mMap: GoogleMap? = null

    lateinit var mapView: MapView

    val LOCATION_REQUEST_CODE = 1

    var address: List<Address>? = null


    private val DEFAULT_ZOOM = 15f

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private val map_view_bundle_key = "My_Bundle"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home_screen)
        mapView = findViewById(R.id.googleMap)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(map_view_bundle_key)
        }
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

        tvEnterDestination.setOnClickListener {
            startActivity(Intent(this, LocationSearch::class.java))
        }
    }


    override fun onMapReady(p0: GoogleMap) {

        mapView.onResume()

        mMap = p0

        askForPermission()

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@UserHomeScreen,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )

        } else {
            mMap!!.setMyLocationEnabled(true)
            mMap!!.setOnCameraMoveListener(this)
            mMap!!.setOnCameraMoveStartedListener(this)
            mMap!!.setOnCameraIdleListener(this)
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        askForPermission()
        var mapViewBundle = outState.getBundle(map_view_bundle_key)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(map_view_bundle_key, mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }

    private fun askForPermission() {
        askPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) {
            getCurrentLocation()

        }.onDeclined { e ->
            if (e.hasDenied()) {
                e.denied.forEach {
                }
                AlertDialog.Builder(this)
                    .setMessage("Please grant location permission to continue")
                    .setPositiveButton("yes") { _, _ ->
                        e.askAgain()

                    }
                    .setNegativeButton("no") { dialog, _ ->
                        dialog.dismiss()

                    }.show()
            }
            if (e.hasForeverDenied()) {
                e.foreverDenied.forEach {
                    e.goToSettings()
                }
            }
        }
    }

    private fun getCurrentLocation() {

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        try {
            @SuppressLint("MissingPermission")
            val location = fusedLocationProviderClient!!.lastLocation
            location.addOnCompleteListener(object : OnCompleteListener<Location> {
                override fun onComplete(p0: Task<Location>) {
                    if (p0.isSuccessful) {
                        val currentLocation = p0.result as Location?
                        if (currentLocation != null) {
                            moveCamera(
                                LatLng(currentLocation.latitude, currentLocation.longitude),
                                DEFAULT_ZOOM
                            )
                        } else {
                            askForPermission()
                        }
                    }
                }

            })

        } catch (e: Exception) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    override fun onLocationChanged(location: Location) {

        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            address = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        setAddress(address!![0])
    }

    private fun setAddress(address: Address) {

        if (address.getAddressLine(0) != null) {
            tvCurrentAddress.text = address.getAddressLine(0)
        }
        if (address.getAddressLine(1) != null) {
            tvCurrentAddress.getText().toString() + (address.getAddressLine(1))
        }
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
    }

    override fun onCameraMove() {
//        val geocoder = Geocoder(this, Locale.getDefault())
//        try {
//            address = geocoder.getFromLocation(
//                mMap!!.cameraPosition.target.latitude,
//                mMap!!.cameraPosition.target.longitude,
//                1
//            )
//            setAddress(address!![0])
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    override fun onCameraMoveStarted(p0: Int) {
//        val geocoder = Geocoder(this, Locale.getDefault())
//        try {
//            address = geocoder.getFromLocation(
//                mMap!!.cameraPosition.target.latitude,
//                mMap!!.cameraPosition.target.longitude,
//                1
//            )
//            setAddress(address!![0])
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    override fun onCameraIdle() {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            address = geocoder.getFromLocation(
                mMap!!.cameraPosition.target.latitude,
                mMap!!.cameraPosition.target.longitude,
                1
            )
            setAddress(address!![0])

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}