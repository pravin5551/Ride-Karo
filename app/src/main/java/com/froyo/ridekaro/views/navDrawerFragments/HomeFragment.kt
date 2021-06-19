package com.froyo.ridekaro.views.navDrawerFragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.froyo.ridekaro.R
import com.froyo.ridekaro.fragments.BottomSheetFragment
import com.froyo.ridekaro.viewModel.AfterClickingRideNow
import com.froyo.ridekaro.viewModel.DistanceViewModel
import com.froyo.ridekaro.viewModel.LatLongViewModel
import com.froyo.ridekaro.viewModel.RidesViewModel
import com.froyo.ridekaro.views.DataParser
import com.froyo.ridekaro.views.LocationSearchFragment
import com.froyo.ridekaro.views.LocationViewModel
import com.froyo.ridekaro.views.RiderComing
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_first_screen.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.Flow
import kotlin.concurrent.thread


class HomeFragment : Fragment(), OnMapReadyCallback, LocationListener,
    GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraIdleListener {


    private var mMap: GoogleMap? = null
    private var toastCount = 0

    private lateinit var pendingIntent2: PendingIntent

    private var bottomCount = 0


    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var areaIntent: Intent

    private lateinit var mapView: MapView

    private var riderCount = 0

    private var totalDistance = ""

    private var resumeCount = 0

    private lateinit var distanceViewModel: DistanceViewModel
    private lateinit var afterClickingRideNow: AfterClickingRideNow
//    private lateinit var ridesViewModel: RidesViewModel

    private val LOCATION_REQUEST_CODE = 1
    private var count = 0

    private var userLocationMarker: Marker? = null
    private var userLocationMarker2: Marker? = null
    private var userLocationMarker3: Marker? = null
    var end_latitude = 0.0
    var end_longitude = 0.0
    var latitude = 0.0
    var rider_latitude = 0.0
    var rider_longitudee = 0.0
    var longitude = 0.0

    private var allMarker = arrayListOf<Marker>()

    var origin: MarkerOptions? = null
    var destination: MarkerOptions? = null

    private var address: List<Address>? = null

    private val DEFAULT_ZOOM = 15f

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private val map_view_bundle_key = "My_Bundle"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

        locationViewModel.getLocation().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val area = it.toString()
            tvEnterDestination.text = area
            getArea(area)
        })

        view.apply {
            mapView = findViewById(R.id.googleMap)
        }

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(map_view_bundle_key)
        }
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

        tvEnterDestination.setOnClickListener {
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            ft.add(
                R.id.fragmentContainerView,
                LocationSearchFragment(),
                "LocationSearchFragment"
            ).addToBackStack("LocationSearchFragment")
            ft.commit()
        }

        distanceViewModel = ViewModelProviders.of(this).get(DistanceViewModel::class.java)
        afterClickingRideNow = ViewModelProviders.of(this).get(AfterClickingRideNow::class.java)

        bottomLinearLayout.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)

        }
    }

    override fun onResume() {
        super.onResume()
        afterClickingRideNow = ViewModelProviders.of(this).get(AfterClickingRideNow::class.java)

        afterClickingRideNow.getMapRider().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            CoroutineScope(Dispatchers.IO).launch {
                if (riderCount == 0) {
                    startTheLoop()
                    riderCount++
                }
            }
        })
        setMaker()
//        ridesViewModel = ViewModelProviders.of(this).get(RidesViewModel::class.java)
//        ridesViewModel.setRiderFragment().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            if (it == 1) {
//                val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
//                ft.add(
//                    R.id.fragmentContainerView,
//                    MyRidesFragment(),
//                    "MyRides"
//                ).addToBackStack(null)
//                ft.commit()
//            }
//        })
    }

    private fun removeMarker() {
        for (maker in allMarker) {
            maker.remove()
        }
    }

    private fun setMaker() {
        if (userLocationMarker != null) {
            val markerOptions3 = MarkerOptions()
            markerOptions3.position(LatLng(rider_latitude, rider_longitudee))
            userLocationMarker!!.isVisible = true
            markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_png))
            userLocationMarker = mMap!!.addMarker(markerOptions3)
//            userLocationMarker2 = null
//            userLocationMarker3 = null
//            removeMarker()
        }
    }

    private suspend fun startTheLoop() {

        for (i in 0..9) {
            requireActivity().runOnUiThread {
                getRiderClose()
            }
            delay(3000)
        }
    }

    private fun getRiderClose() {
        if (rider_longitudee >= longitude) {
            removeMarker()
            val markerOptions2 = MarkerOptions()
            rider_longitudee -= 0.001
            markerOptions2.position(LatLng(rider_latitude, rider_longitudee))
//            Toast.makeText(context, "Rider came closer", Toast.LENGTH_SHORT).show()
            markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_png))
            userLocationMarker = mMap!!.addMarker(markerOptions2)
            allMarker.add(userLocationMarker!!)

        } else {
            if (toastCount == 0) {
                Toast.makeText(context, "Rider Arrived", Toast.LENGTH_SHORT).show()
                toastCount++
            }
            showNotification()
        }
    }

    private fun showNotification() {
        NotificationManagerCompat.from(requireContext()).cancelAll()

        val intent = Intent(context, RiderArrived::class.java)

        pendingIntent2 =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager =
            requireActivity().applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "workExample",
                "workExample",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val builder =
            NotificationCompat.Builder(requireActivity().applicationContext, "workExample")
                .setContentTitle("Rider Arrived")
                .setContentText("Your rider has arrived at your location!")
                .setSmallIcon(R.mipmap.motorbike)
                .setContentIntent(pendingIntent2)
        notificationManager.notify(1, builder.build())
    }


    override fun onMapReady(p0: GoogleMap) {

        mapView.onResume()

        mMap = p0

        askForPermission()

        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
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
                AlertDialog.Builder(context as Activity)
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
            LocationServices.getFusedLocationProviderClient(context)
        try {
            @SuppressLint("MissingPermission")
            val location = fusedLocationProviderClient!!.lastLocation
            location.addOnCompleteListener(object : OnCompleteListener<Location> {
                override fun onComplete(p0: Task<Location>) {
                    if (p0.isSuccessful) {
                        val currentLocation = p0.result
                        if (currentLocation != null) {
                            moveCamera(
                                LatLng(currentLocation.latitude, currentLocation.longitude),
                                DEFAULT_ZOOM
                            )
                            val geocoder3 = Geocoder(context, Locale.getDefault())
                            val address4 = geocoder3.getFromLocation(
                                currentLocation.latitude,
                                currentLocation.longitude,
                                10
                            )

                            setCurrentAddress(address4!![0])
                            latitude = currentLocation.latitude
                            longitude = currentLocation.longitude
                            if (userLocationMarker == null && userLocationMarker2 == null && userLocationMarker3 == null) {
                                val latitude1 = currentLocation.latitude
                                rider_latitude = currentLocation.latitude
                                val longitude1 = currentLocation.longitude + 0.009
                                rider_longitudee = currentLocation.longitude + 0.009
                                val latitude2 = currentLocation.latitude + 0.009
                                val bearing = currentLocation.bearing
                                val longitude2 = currentLocation.longitude
//                                val latitude3 = currentLocation.latitude
//                                val longitude3 = currentLocation.longitude - 0.006
                                val latLng = LatLng(latitude1, longitude1)
                                val latLng2 = LatLng(latitude2, longitude2)
//                                val latLng3 = LatLng(latitude3, longitude3)
                                val markerOptions = MarkerOptions()
                                val markerOptions2 = MarkerOptions()
                                val markerOptions3 = MarkerOptions()
                                markerOptions.position(latLng)
                                markerOptions2.position(latLng)
//                                markerOptions3.position(latLng)
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_png))
                                markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_png))
//                                markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_png))
                                markerOptions.rotation(bearing)
                                markerOptions2.rotation(bearing)
//                                markerOptions3.rotation(bearing)
                                userLocationMarker = mMap!!.addMarker(markerOptions)
                                userLocationMarker2 = mMap!!.addMarker(markerOptions2)
//                                userLocationMarker3 = mMap!!.addMarker(markerOptions3)
                                userLocationMarker!!.position = latLng
                                userLocationMarker2!!.position = latLng2
//                                userLocationMarker3!!.position = latLng3
                                allMarker.add(userLocationMarker!!)
                                allMarker.add(userLocationMarker2!!)
//                                allMarker.add(userLocationMarker3!!)
                                count++
                            } else {
                                val latitude1 = currentLocation.latitude
                                val longitude1 = currentLocation.longitude + 0.009
                                val latitude2 = currentLocation.latitude + 0.009
//                                val bearing = currentLocation.bearing
                                val longitude2 = currentLocation.longitude
//                                val latitude3 = currentLocation.latitude
//                                val longitude3 = currentLocation.longitude - 0.006
                                val markerOptions5 = MarkerOptions()
                                val latLng = LatLng(latitude1, longitude1)
                                val latLng2 = LatLng(latitude2, longitude2)
                                markerOptions5.position(latLng2)
                                markerOptions5.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_png))
//                                val latLng3 = LatLng(latitude3, longitude3)
                                removeMarker()
                                userLocationMarker!!.position = latLng
                                userLocationMarker2!!.position = latLng2
                                userLocationMarker = mMap!!.addMarker(markerOptions5)
//                                userLocationMarker3!!.position = latLng3
                            }
                        } else {
                            askForPermission()
                        }
                    }
                }

            })

        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    override fun onLocationChanged(location: Location) {

        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            address = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        setAddress(address!![0])
    }

    private fun setAddress(address: Address) {

        if (address.getAddressLine(0) != null) {
            val allAddress = address.getAddressLine(0)
            var array: List<String> = allAddress.split(",")
            tvCurrentAddress.text = array[0] + "," + array[1] + "," + array[2]
        }
//        if (address.getAddressLine(1) != null) {
//            tvEnterDestination.getText().toString() + (address.getAddressLine(1))
//        }
    }

    private fun setCurrentAddress(address: Address) {

        if (address.getAddressLine(0) != null) {
            val allAddress = address.getAddressLine(0)
            var array: List<String> = allAddress.split(",")
//            tvEnterDestination.text = array[0] + "," + array[1]
        }
//        if (address.getAddressLine(1) != null) {
//            tvCurrentAddress.getText().toString() + (address.getAddressLine(1))
//        }
    }


    private fun setFinalDestinationText(address: Address?) {
        if (address!!.getAddressLine(0) != null) {
            val allAddress = address.getAddressLine(0)
            var array: List<String> = allAddress.split(",")
            tvEnterDestination.text = array[0] + "," + array[1] + "," + array[2] + "," + array[3]
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
        val geocoder = Geocoder(context, Locale.getDefault())
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


    private fun getArea(address: String) {
        var addressList: List<Address>? = null
        val markerOptions = MarkerOptions()
        if (address != "") {
            val geocoder = Geocoder(context)
            try {
                addressList = geocoder.getFromLocationName(address, 5)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (addressList != null) {
                for (i in addressList.indices) {
                    val myAddress = addressList[i]
                    val latLng = LatLng(myAddress.latitude, myAddress.longitude)
                    markerOptions.position(latLng)
                    mMap!!.addMarker(markerOptions)
                    end_latitude = myAddress.latitude
                    end_longitude = myAddress.longitude
                    mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    val mo = MarkerOptions()
                    mo.title("Distance")
                    val results = FloatArray(10)
                    Location.distanceBetween(
                        latitude,
                        longitude,
                        end_latitude,
                        end_longitude,
                        results
                    )
                    totalDistance = String.format("%.1f", results[0] / 1000)
                    origin =
                        MarkerOptions().position(LatLng(latitude, longitude)).title("HSR Layout")
                            .snippet("origin")

                    destination =
                        MarkerOptions().position(LatLng(end_latitude, end_longitude)).title(address)
                            .snippet("Distance= $totalDistance KM")

                    mMap!!.addMarker(destination)

                    val float1: Float? = totalDistance.toFloat()
                    distanceViewModel.addDistance(float1!!)

                    val geocoder2 = Geocoder(context, Locale.getDefault())
                    val end_address = geocoder2.getFromLocation(
                        end_latitude,
                        end_longitude,
                        10
                    )
                    setAddress(end_address!![0])

                    val origin_latlong = LatLng(latitude, longitude)
                    val destination_latlong = LatLng(end_latitude, end_longitude)

                    val geocoder4 = Geocoder(context, Locale.getDefault())
                    val address6 = geocoder4.getFromLocation(
                        destination_latlong.latitude,
                        destination_latlong.longitude,
                        10
                    )
                    setFinalDestinationText(address6!![0])
                    if (bottomCount == 0) {
                        val bottomSheetFragment = BottomSheetFragment()
                        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                        bottomCount++
                    }

                    val url: String? =
                        getDirectionsUrl(origin_latlong, destination_latlong)
                    val downloadTask = DownloadTask()
                    downloadTask.execute(url)
                }
            }
        }
    }


    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String? {
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        val mode = "mode=driving"
        val parameters = "$str_origin&$str_dest&$mode"
        val output = "json"
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=AIzaSyCv_5zK9gY5rZfL36LspmVQIBXJ3wSrAzQ"
    }

    inner class DownloadTask :
        AsyncTask<String?, Void?, String>() {

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parserTask = ParserTask()
            parserTask.execute(result)
        }

        override fun doInBackground(vararg url: String?): String {
            var data = ""
            try {
                data = downloadUrl(url[0].toString()).toString()
            } catch (e: java.lang.Exception) {
                Log.d("Background Task", e.toString())
            }
            return data
        }
    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String? {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection!!.inputStream
            val br =
                BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
            br.close()
        } catch (e: java.lang.Exception) {
            Log.d("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    inner class ParserTask :
        AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>() {
        override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? =
                null
            try {
                jObject = JSONObject(jsonData[0])
                val parser = DataParser()
                routes = parser.parse(jObject)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return routes
        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            val points = ArrayList<LatLng?>()
            val lineOptions = PolylineOptions()
            for (i in result!!.indices) {
                val path =
                    result[i]
                for (j in path.indices) {
                    val point = path[j]
                    val lat = point["lat"]!!.toDouble()
                    val lng = point["lng"]!!.toDouble()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
                lineOptions.addAll(points)
                lineOptions.width(8f)
                lineOptions.color(Color.RED)
                lineOptions.geodesic(true)
            }

            if (points.size != 0)
                mMap!!.addPolyline(lineOptions)
        }
    }
//    private fun getLatestLocation() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//        locationRequest = LocationRequest()
//        locationRequest.interval = 50000
//        locationRequest.fastestInterval = 50000
//        locationRequest.smallestDisplacement = 170f
//        locationRequest.priority =
//            LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult?) {
//                locationResult ?: return
//                if (locationResult.locations.isNotEmpty()) {
//                    val currentLocation =
//                        locationResult.lastLocation
//                    moveCamera(
//                        LatLng(currentLocation.latitude, currentLocation.longitude),
//                        DEFAULT_ZOOM
//                    )
//                    val geocoder3 = Geocoder(context, Locale.getDefault())
//                    val address4 = geocoder3.getFromLocation(
//                        currentLocation.latitude,
//                        currentLocation.longitude,
//                        10
//                    )
//                    setCurrentAddress(address4!![0])
//                    latitude = currentLocation.latitude
//                    longitude = currentLocation.longitude
//                    if (userLocationMarker == null && userLocationMarker2 == null && userLocationMarker3 == null) {
//                        val latitude1 = currentLocation.latitude
//                        val longitude1 = currentLocation.longitude + 0.009
//                        val latitude2 = currentLocation.latitude + 0.009
//                        val bearing = currentLocation.bearing
//                        val longitude2 = currentLocation.longitude
//                        val latitude3 = currentLocation.latitude
//                        val longitude3 = currentLocation.longitude - 0.006
//                        val latLng = LatLng(latitude1, longitude1)
//                        val latLng2 = LatLng(latitude2, longitude2)
//                        val latLng3 = LatLng(latitude3, longitude3)
//                        val markerOptions = MarkerOptions()
//                        val markerOptions2 = MarkerOptions()
//                        val markerOptions3 = MarkerOptions()
//                        markerOptions.position(latLng)
//                        markerOptions2.position(latLng)
//                        markerOptions3.position(latLng)
//                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_png))
//                        markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_png))
//                        markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_png))
//                        markerOptions.rotation(bearing)
//                        markerOptions2.rotation(bearing)
//                        markerOptions3.rotation(bearing)
//                        userLocationMarker = mMap!!.addMarker(markerOptions)
//                        userLocationMarker2 = mMap!!.addMarker(markerOptions2)
//                        userLocationMarker3 = mMap!!.addMarker(markerOptions3)
//                        userLocationMarker!!.position = latLng
//                        userLocationMarker2!!.position = latLng2
//                        userLocationMarker3!!.position = latLng3
//                        count++
//                    } else {
//                        val latitude1 = currentLocation.latitude
//                        val longitude1 = currentLocation.longitude + 0.009
//                        val latitude2 = currentLocation.latitude + 0.009
////                                val bearing = currentLocation.bearing
//                        val longitude2 = currentLocation.longitude
//                        val latitude3 = currentLocation.latitude
//                        val longitude3 = currentLocation.longitude - 0.006
//                        val latLng = LatLng(latitude1, longitude1)
//                        val latLng2 = LatLng(latitude2, longitude2)
//                        val latLng3 = LatLng(latitude3, longitude3)
//                        userLocationMarker!!.position = latLng
//                        userLocationMarker2!!.position = latLng2
//                        userLocationMarker3!!.position = latLng3
//                    }
//                }
//
//            }
//        }
//    }
}