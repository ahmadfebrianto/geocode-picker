package com.ahmadfebrianto.geocodepicker

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ahmadfebrianto.geocodepicker.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity :
    AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener, View.OnClickListener {

    private var geocode: String? = null
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.google_map) as? SupportMapFragment

        mapFragment?.getMapAsync(this)
        mainBinding.tvGeocodeValue.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setOnMapClickListener {
            googleMap.clear()
            googleMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .draggable(true)
            )
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 6.0f), 500, null)
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

    override fun onClick(v: View?) {
        if (geocode != null) {
            val clipData: ClipData = ClipData.newPlainText("geocode", geocode)
            val clipBoard =
                applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipBoard.setPrimaryClip(clipData)
            setToast("Geocode copied to clipboard")
        }
    }

    private fun setToast(text: String) {
        val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}