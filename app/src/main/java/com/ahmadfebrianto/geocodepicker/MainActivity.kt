package com.ahmadfebrianto.geocodepicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ahmadfebrianto.geocodepicker.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity :
    AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener {

    private lateinit var geocode: String
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.google_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val coordinate = LatLng(-6.20906, 106.83539)
        googleMap.addMarker(
            MarkerOptions()
                .position(coordinate)
                .draggable(true)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 6.0f))
        googleMap.setOnMarkerDragListener(this)

        googleMap.setOnMapClickListener {
            googleMap.clear()
            googleMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .draggable(true)
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 6.0f))
            googleMap.setOnMarkerDragListener(this)
            val gc = extractGeocode(it.toString()).split(",")
            val lat = gc[0].toDouble()
            val long = gc[1].toDouble()
            mainBinding.tvGeocodeValue.text = formatGeocode(lat, long)
        }
    }

    override fun onMarkerDragStart(p0: Marker) {

    }

    override fun onMarkerDrag(p0: Marker) {
        setGeocode(p0)
    }

    override fun onMarkerDragEnd(p0: Marker) {
        setGeocode(p0)
    }


    private fun formatGeocode(lat: Double, long: Double): String {
        val newLat = String.format("%.10f", lat)
        val newLong = String.format("%.10f", long)
        return "$newLat,$newLong"
    }


    private fun setGeocode(p0: Marker) {
        val position = p0.position
        geocode = formatGeocode(position.latitude, position.longitude)
        mainBinding.tvGeocodeValue.text = geocode
    }

    private fun extractGeocode(latLong: String): String {
        var result = ""
        val pattern = Regex("""\(([^]]+)\)""")
        val match = pattern.find(latLong)
        if (match != null) {
            result = match.groupValues[1]
        }
        return result
    }
}