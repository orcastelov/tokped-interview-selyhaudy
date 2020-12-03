package com.tokopedia.maps

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.tokopedia.maps.model.Country
import com.tokopedia.maps.viewmodel.MapsViewModel
import com.tokopedia.maps.viewmodel.MapsViewModelFactory


open class MapsActivity : AppCompatActivity() {

    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null
    private val viewModel: MapsViewModel by lazy {
        ViewModelProvider(this, MapsViewModelFactory()).get(MapsViewModel::class.java)
    }

    private lateinit var textCountryName: TextView
    private lateinit var textCountryCapital: TextView
    private lateinit var textCountryPopulation: TextView
    private lateinit var textCountryCallCode: TextView

    private var editText: AutoCompleteTextView? = null
    private var buttonSubmit: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        bindViews()
        initListeners()
        loadMap()
        bindViewModel()
    }

    private fun bindViews() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        editText = findViewById(R.id.editText)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        textCountryName = findViewById(R.id.txtCountryName)
        textCountryCapital = findViewById(R.id.txtCountryCapital)
        textCountryPopulation = findViewById(R.id.txtCountryPopulation)
        textCountryCallCode = findViewById(R.id.txtCountryCallCode)
    }

    private fun initListeners() {
        buttonSubmit!!.setOnClickListener {
            // search by the given country name, and
            val country = viewModel.findCountryByName(editText?.text.toString())
            // 1. pin point to the map
            pinPointOnMap(country)
            // 2. set the country information to the textViews.
            showSelectedCountry(country)
        }
    }

    fun loadMap() {
        mapFragment!!.getMapAsync { googleMap -> this@MapsActivity.googleMap = googleMap }
    }

    private fun bindViewModel() {
        viewModel.allCountries.observe(this, Observer {
            editText?.setAdapter(ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_dropdown_item_1line, it.map { it.name }))
        })
    }

    private fun pinPointOnMap(country: Country?) {
        googleMap?.clear()
        country?.also {
            val latlng = LatLng(it.latlng.first(), it.latlng.last())
            val builder = LatLngBounds.Builder()
            builder.include(latlng)
            val cameraPosition = CameraPosition.Builder()
                    .target(latlng)
                    .zoom(4f)
                    .build()
            googleMap?.addMarker(MarkerOptions().position(latlng))
            googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    private fun showSelectedCountry(country: Country?) {
        val countryName = country?.name ?: ""
        val countryCapital = country?.capital ?: ""
        val countryPopulation = country?.population ?: ""
        val countryCallingCodes = country?.callingCodes?.joinToString(", ") ?: ""

        textCountryName.text = "$COUNTRY_NAME_PREFIX $countryName"
        textCountryCapital.text = "$COUNTRY_CAPITAL_PREFIX $countryCapital"
        textCountryPopulation.text = "$COUNTRY_POPULATION_PREFIX $countryPopulation"
        textCountryCallCode.text = "$COUNTRY_CALLING_CODES_PREFIX $countryCallingCodes"
    }

    companion object {
        const val COUNTRY_NAME_PREFIX = "Nama negara: "
        const val COUNTRY_CAPITAL_PREFIX = "Ibukota: "
        const val COUNTRY_POPULATION_PREFIX = "Jumlah penduduk: "
        const val COUNTRY_CALLING_CODES_PREFIX = "Kode telepon: "
    }
}
