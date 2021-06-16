package com.froyo.ridekaro.views.navDrawerFragments

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.froyo.ridekaro.R
import com.froyo.ridekaro.fragments.BottomSheetFragment
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback, LocationListener,
    GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraIdleListener {

    private var mMap: GoogleMap? = null

    private lateinit var mapView: MapView

    private var totalDistance = ""

    private val LOCATION_REQUEST_CODE = 1
    private var count = 0

//    private val receiver = GetBroadcast()

    private var userLocationMarker: Marker? = null
    private var userLocationMarker2: Marker? = null
    private var userLocationMarker3: Marker? = null
    private var searchLocationDistanceMarker: Marker? = null
    var end_latitude = 0.0
    var end_longitude = 0.0
    var latitude = 0.0
    var longitude = 0.0

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
//            startActivity(Intent(this, LocationSearch::class.java))
            getArea("Dahisar station")

        }

        bottomLinearLayout.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
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
                            latitude = currentLocation.latitude
                            longitude = currentLocation.longitude
                            if (userLocationMarker == null && userLocationMarker2 == null && userLocationMarker3 == null) {
//                                userLocationMarker =  Marker(p0)
//                                userLocationMarker2 = Marker(null)
//                                userLocationMarker3 = Marker(null)
                                val latitude1 = currentLocation.latitude
                                val longitude1 = currentLocation.longitude + 0.009
                                val latitude2 = currentLocation.latitude + 0.009
                                val bearing = currentLocation.bearing
                                val longitude2 = currentLocation.longitude
                                val latitude3 = currentLocation.latitude
                                val longitude3 = currentLocation.longitude - 0.006
                                val latLng = LatLng(latitude1, longitude1)
                                val latLng2 = LatLng(latitude2, longitude2)
                                val latLng3 = LatLng(latitude3, longitude3)
                                val markerOptions = MarkerOptions()
                                val markerOptions2 = MarkerOptions()
                                val markerOptions3 = MarkerOptions()
//                                userLocationMarker: Marker()
//                                val userLocationMarker2: Marker?
//                                val userLocationMarker3: Marker?
                                markerOptions.position(latLng)
                                markerOptions2.position(latLng)
                                markerOptions3.position(latLng)
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_png))
                                markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_png))
                                markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_png))
                                markerOptions.rotation(bearing)
                                markerOptions2.rotation(bearing)
                                markerOptions3.rotation(bearing)
                                userLocationMarker = mMap!!.addMarker(markerOptions)
                                userLocationMarker2 = mMap!!.addMarker(markerOptions2)
                                userLocationMarker3 = mMap!!.addMarker(markerOptions3)
                                userLocationMarker!!.position = latLng
                                userLocationMarker2!!.position = latLng2
                                userLocationMarker3!!.position = latLng3
                                count++
                            } else {
                                val latitude1 = currentLocation.latitude
                                val longitude1 = currentLocation.longitude + 0.009
                                val latitude2 = currentLocation.latitude + 0.009
//                                val bearing = currentLocation.bearing
                                val longitude2 = currentLocation.longitude
                                val latitude3 = currentLocation.latitude
                                val longitude3 = currentLocation.longitude - 0.006
                                val latLng = LatLng(latitude1, longitude1)
                                val latLng2 = LatLng(latitude2, longitude2)
                                val latLng3 = LatLng(latitude3, longitude3)
                                userLocationMarker!!.position = latLng
                                userLocationMarker2!!.position = latLng2
                                userLocationMarker3!!.position = latLng3
//                                getArea("Thane")
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

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
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

    fun getArea(address: String) {
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

                    mMap!!.addMarker(destination!!)

                    Toast.makeText(context, "Distance = $totalDistance KM", Toast.LENGTH_SHORT).show()
                    val origin_latlong = LatLng(latitude, longitude)
                    val destination_latlong = LatLng(end_latitude, end_longitude)

//                    val url: String =
//                        getDirectionsUrl(origin!!.position, destination!!.position)
//                    val downloadTask: DownloadTask = DownloadTask()
//                    downloadTask.execute(url)
                }
            }
        }
    }
//    private fun getDirectionsUrl(origin: LatLng, destination: LatLng): String {
//        val str_origin = "origin" + origin.latitude + "," + origin.longitude
//        val str_destination = "destination" + destination.latitude + "," + destination.longitude
//        val mode = "mode=driving"
//        val parameters = "$str_origin&$str_destination&$mode"
//        val output = "json"
//        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=AIzaSyCtbdDgl8vD6w7KyuVIQEYAaTEeG8Vebn4"
//    }
//
//
////    override fun onStop() {
////        super.onStop()
////        unregisterReceiver(receiver)
////    }
////
////    inner class GetBroadcast : BroadcastReceiver() {
////        override fun onReceive(context: Context?, intent: Intent?) {
////            val address: String? = intent?.getStringExtra("address");
////            getArea(address!!)
////        }
////    }
//
//    inner class DownloadTask : AsyncTask<String?, Void?, String>() {
//        override fun doInBackground(vararg url: String?): String {
//            var data = ""
//            try {
//                data = downloadUrl(url[0].toString()).toString()
//            } catch (e: Exception) {
//                Log.d("Background Task", e.toString())
//            }
//            return data
//        }
//
//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
//            val parserTask = ParserTask()
//            parserTask.execute(result)
//        }
//
//
//    }
//
//    @Throws(IOException::class)
//    private fun downloadUrl(strUrl: String): String? {
//        var data = ""
//        var iStream: InputStream? = null
//        var urlConnection: HttpURLConnection? = null
//        try {
//            val url = URL(strUrl)
//            urlConnection = url.openConnection() as HttpURLConnection
//            urlConnection.connect()
//            iStream = urlConnection.getInputStream()
//            val br = BufferedReader(InputStreamReader(iStream))
//            val sb = StringBuffer()
//            var line: String? = ""
//            while (br.readLine().also { line = it } != null) {
//                sb.append(line)
//            }
//            data = sb.toString()
//            br.close()
//        } catch (e: Exception) {
//            Log.d("mylog", "Exception downloading URL: $e")
//        } finally {
//            iStream!!.close()
//            urlConnection!!.disconnect()
//        }
//        return data
//    }
//
//    inner class ParserTask :
//        AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>() {
//        var taskCallback: TaskLoadedCallback
//        var directionMode = "driving"
//
//        // Executes in UI thread, after the parsing process
//        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
//            var points: ArrayList<LatLng?>
//            var lineOptions: PolylineOptions? = null
//            // Traversing through all the routes
//            for (i in result!!.indices) {
//                points = ArrayList()
//                lineOptions = PolylineOptions()
//                // Fetching i-th route
//                val path = result[i]
//                // Fetching all the points in i-th route
//                for (j in path.indices) {
//                    val point = path[j]
//                    val lat = point["lat"]!!.toDouble()
//                    val lng = point["lng"]!!.toDouble()
//                    val position = LatLng(lat, lng)
//                    points.add(position)
//                }
//                // Adding all the points in the route to LineOptions
//                lineOptions.addAll(points)
//                if (directionMode.equals("walking", ignoreCase = true)) {
//                    lineOptions.width(10f)
//                    lineOptions.color(Color.MAGENTA)
//                } else {
//                    lineOptions.width(20f)
//                    lineOptions.color(Color.BLUE)
//                }
//                Log.d("mylog", "onPostExecute lineoptions decoded")
//            }
//
//            // Drawing polyline in the Google Map for the i-th route
//            if (lineOptions != null) {
//                //mMap.addPolyline(lineOptions);
//                taskCallback.onTaskDone(lineOptions)
//            } else {
//                Log.d("mylog", "without Polylines drawn")
//            }
//        }
//
//        init {
//            taskCallback = mContext as TaskLoadedCallback
//            this.directionMode = directionMode
//        }
//
//        override fun doInBackground(vararg params: String?): List<List<HashMap<String, String>>>? {
//            val jObject: JSONObject
//            var routes: List<List<HashMap<String, String>>>? = null
//            try {
//                jObject = JSONObject(jsonData[0])
//                Log.d("mylog", jsonData[0])
//                val parser = DataParser()
//                Log.d("mylog", parser.toString())
//
//                // Starts parsing data
//                routes = parser.parse(jObject)
//                Log.d("mylog", "Executing routes")
//                Log.d("mylog", routes.toString())
//            } catch (e: java.lang.Exception) {
//                Log.d("mylog", e.toString())
//                e.printStackTrace()
//            }
//            return routes
//        }
//    }
}