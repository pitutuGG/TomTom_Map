package com.example.tomtom;

import android.Manifest
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.tomtom.quantity.Distance
import com.tomtom.sdk.location.GeoPoint
import com.tomtom.sdk.map.display.MapOptions
import com.tomtom.sdk.map.display.camera.CameraOptions
import com.tomtom.sdk.map.display.style.LoadingStyleFailure
import com.tomtom.sdk.map.display.style.StandardStyles
import com.tomtom.sdk.map.display.style.StyleLoadingCallback
import com.tomtom.sdk.map.display.style.StyleMode
import com.tomtom.sdk.map.display.ui.MapFragment
import com.tomtom.sdk.location.android.AndroidLocationProvider
import com.tomtom.sdk.location.android.AndroidLocationProviderConfig
import com.tomtom.sdk.location.LocationProvider
import com.tomtom.sdk.map.display.image.ImageFactory
import com.tomtom.sdk.map.display.location.LocationMarkerClickListener
import com.tomtom.sdk.map.display.location.LocationMarkerOptions
import kotlin.time.Duration.Companion.milliseconds
import com.tomtom.sdk.map.display.marker.MarkerOptions

class MainActivity : AppCompatActivity() {

    enum class StandartStyleEnum {
        BROWSING,
        DRIVING,
        SATELLITE
    }

    private lateinit var switchMapStyleButton: Button
    private lateinit var mapFragment: MapFragment
    private lateinit var startingPoint: GeoPoint

    private val onStyleLoadedCallback = object : StyleLoadingCallback {
        override fun onSuccess() {
            /* YOUR CODE GOES HERE */
        }

        override fun onFailure(error: LoadingStyleFailure) {
            /* YOUR CODE GOES HERE */
        }
    }
    private var standardStyleMode: StandartStyleEnum = StandartStyleEnum.BROWSING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMap()

        switchMapStyleButton = findViewById(R.id.switchStyle)
        switchMapStyleButton.setOnClickListener { switchMapStyleHandler() }

        askForPermision()

        locationProvider()
        
        //startingLocation()

        addMarkers()


    }

    private fun switchMapStyleHandler() {
        mapFragment.getMapAsync { it ->
            standardStyleMode =
                StandartStyleEnum.values()[(standardStyleMode.ordinal + 1) % StandartStyleEnum.values().size]
            it.showHillShading()
            println(standardStyleMode.ordinal)
            when (standardStyleMode) {
                StandartStyleEnum.BROWSING -> {
                    it.loadStyle(StandardStyles.BROWSING, onStyleLoadedCallback)
                    it.showHillShading()
                }

                StandartStyleEnum.DRIVING -> {
                    it.loadStyle(StandardStyles.DRIVING, onStyleLoadedCallback)
                    it.hideHillShading()
                    it.setStyleMode(StyleMode.DARK)
                    it.showTrafficFlow()
                    it.showTrafficIncidents()
                    it.loadStyle(StandardStyles.VEHICLE_RESTRICTIONS, onStyleLoadedCallback)
                }

                StandartStyleEnum.SATELLITE -> {
                    it.loadStyle(StandardStyles.SATELLITE, onStyleLoadedCallback)
                    it.hideTrafficFlow()
                    it.hideTrafficIncidents()
                    it.setStyleMode(StyleMode.MAIN)
                }

                else -> {
                    throw Error("This is dumb")
                }
            }
        }
    }

    private fun initMap() {
        val mapOptions = MapOptions(
            mapKey = "4qYsSQeBWlI3vmtpw6YGPDR3UaoaMadT",
            cameraOptions = CameraOptions(zoom = 10.0)
        )

        mapFragment = MapFragment.newInstance(mapOptions)

        supportFragmentManager.beginTransaction()
            .replace(R.id.map_container, mapFragment)
            .commit()

        mapFragment.getMapAsync(){
           //it.cameraTrackingMode = CameraTrackingMode.Follow
        }
    }

    private fun askForPermision() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                }

                else -> {
                    // No location access granted.
                }
            }
        }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun locationProvider() {
        val androidLocationProviderConfig = AndroidLocationProviderConfig(
            minTimeInterval = 250L.milliseconds,
            minDistance = Distance.meters(10.0)
        )
        val locationProvider: LocationProvider = AndroidLocationProvider(
            context = applicationContext,
            config = androidLocationProviderConfig
        )
        val locationMarkerOptions = LocationMarkerOptions(
            type = LocationMarkerOptions.Type.Pointer,
            markerMagnification = 1.0
        )
        mapFragment.getMapAsync { it ->
            it.setLocationProvider(locationProvider)

            it.enableLocationMarker(locationMarkerOptions)
            val locationMarkerClickListener =
                LocationMarkerClickListener { point: Point, position: GeoPoint ->

                }
            it.addLocationMarkerClickListener(locationMarkerClickListener)
            it.removeLocationMarkerClickListener(locationMarkerClickListener)
        }
        locationProvider.enable()
    }
    /*
    private fun startingLocation() {
        val cameraOptions = CameraOptions(

            position = startingPoint,
            zoom = 10.0,
        )
        mapFragment.getMapAsync(){
            it.moveCamera(cameraOptions)
        }
    }
     */
    private fun addMarkers() {
        val TomTomPolska = GeoPoint(51.769224475150104, 19.46506939204617)
        val markerOptions = MarkerOptions(
            coordinate = TomTomPolska,
            pinImage = ImageFactory.fromResource(R.drawable.ic_marker_pin)
        )
        mapFragment.getMapAsync(){
            it.addMarker(markerOptions)
            it.addMarkerClickListener(){
            }
        }
    }
}