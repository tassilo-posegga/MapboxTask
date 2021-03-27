package eu.aggesop.mapboxtask

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import eu.aggesop.mapboxtask.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mapBoxMap: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.apply {
            onCreate(savedInstanceState)
            attachToLifeCycle(lifecycle)
            getMapAsync {
                mapBoxMap = it
                mapBoxMap?.setStyle(Style.MAPBOX_STREETS) {
                    initSearchButton()
                }
            }
        }
    }

    private fun initSearchButton() {
        binding.searchButton.setOnClickListener {
            startPlacesPlugin()
        }
    }

    private fun startPlacesPlugin() {
        val placeOptions = PlaceOptions.builder()
            .backgroundColor(Color.WHITE)
            .build()
        val intent = PlaceAutocomplete.IntentBuilder()
            .accessToken(getString(R.string.mapbox_access_token))
            .placeOptions(placeOptions)
            .build(this)
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            handleAutoCompletePlace(data)
        }
    }

    private fun handleAutoCompletePlace(data: Intent?) {
        val selectedCarmenFeature = PlaceAutocomplete.getPlace(data)
        animateCameraToSelectedPlace(selectedCarmenFeature)
    }

    private fun animateCameraToSelectedPlace(selectedCarmenFeature: CarmenFeature) {
        mapBoxMap?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(
                        LatLng(
                            (selectedCarmenFeature.geometry() as Point).latitude(),
                            (selectedCarmenFeature.geometry() as Point).longitude()
                        )
                    )
                    .zoom(14.0)
                    .build()
            ), 4000
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    private companion object {
        const val REQUEST_CODE_AUTOCOMPLETE = 1337
    }
}
