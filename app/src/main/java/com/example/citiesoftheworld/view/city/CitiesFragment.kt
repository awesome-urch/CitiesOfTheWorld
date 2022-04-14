package com.example.citiesoftheworld.view.city

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citiesoftheworld.R
import com.example.citiesoftheworld.database.model.cityAndCountry.CityAndCountry
import com.example.citiesoftheworld.network.model.WorldCitiesResultState
import com.example.citiesoftheworld.utils.OnMapAndViewReadyListener
import com.example.citiesoftheworld.view.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_cities.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*


@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
class CitiesFragment : Fragment(), GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnInfoWindowLongClickListener,
    GoogleMap.OnInfoWindowCloseListener,
    OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    private val citiesViewModel : CitiesViewModel by viewModel()

    private lateinit var citiesAdapter: CitiesAdapter
//    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMapObject: GoogleMap
    /** map to store place names and locations */
    private val places = mapOf(
        "BRISBANE" to LatLng(-27.47093, 153.0235),
        "MELBOURNE" to LatLng(-37.81319, 144.96298),
        "DARWIN" to LatLng(-12.4634, 130.8456),
        "SYDNEY" to LatLng(-33.87365, 151.20689),
        "ADELAIDE" to LatLng(-34.92873, 138.59995),
        "PERTH" to LatLng(-31.952854, 115.857342),
        "ALICE_SPRINGS" to LatLng(-24.6980, 133.8807)
    )

    private val random = Random()

    /** Demonstrates customizing the info window and/or its contents.  */
    /*internal inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {

        // These are both view groups containing an ImageView with id "badge" and two
        // TextViews with id "title" and "snippet".
        private val window: View = layoutInflater.inflate(R.layout.custom_info_window, null)
        private val contents: View = layoutInflater.inflate(R.layout.custom_info_contents, null)

        override fun getInfoWindow(marker: Marker): View? {
            if (options.checkedRadioButtonId != R.id.custom_info_window) {
                // This means that getInfoContents will be called.
                return null
            }
            render(marker, window)
            return window
        }

        override fun getInfoContents(marker: Marker): View? {
            if (options.checkedRadioButtonId != R.id.custom_info_contents) {
                // This means that the default info contents will be used.
                return null
            }
            render(marker, contents)
            return contents
        }

        private fun render(marker: Marker, view: View) {
            val badge = when (marker.title!!) {
                "Brisbane" -> R.drawable.badge_qld
                "Adelaide" -> R.drawable.badge_sa
                "Sydney" -> R.drawable.badge_nsw
                "Melbourne" -> R.drawable.badge_victoria
                "Perth" -> R.drawable.badge_wa
                in "Darwin Marker 1".."Darwin Marker 4" -> R.drawable.badge_nt
                else -> 0 // Passing 0 to setImageResource will clear the image view.
            }

            view.findViewById<ImageView>(R.id.badge).setImageResource(badge)

            // Set the title and snippet for the custom info window
            val title: String? = marker.title
            val titleUi = view.findViewById<TextView>(R.id.title)

            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                titleUi.text = SpannableString(title).apply {
                    setSpan(ForegroundColorSpan(Color.RED), 0, length, 0)
                }
            } else {
                titleUi.text = ""
            }

            val snippet: String? = marker.snippet
            val snippetUi = view.findViewById<TextView>(R.id.snippet)
            if (snippet != null && snippet.length > 12) {
                snippetUi.text = SpannableString(snippet).apply {
                    setSpan(ForegroundColorSpan(Color.MAGENTA), 0, 10, 0)
                    setSpan(ForegroundColorSpan(Color.BLUE), 12, snippet.length, 0)
                }
            } else {
                snippetUi.text = ""
            }
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        OnMapAndViewReadyListener(mapFragment, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_cities, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

//        mapFragment.getMapAsync(this)
        OnMapAndViewReadyListener(mapFragment, this)
        // Inflate the layout for this fragment
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_search -> {
                true
            }
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.world_cities_menu, menu)
        val searchView = ((context as MainActivity).supportActionBar?.themedContext ?: context)?.let {
            SearchView(
                it
            )
        }

        menu.findItem(R.id.menu_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(cityName: String): Boolean {
//                citiesViewModel.setWorldCitiesApiParams(cityName)
                citiesViewModel.setFilter(cityName)
//                citiesViewModel.triggerSearchEvent()
                return false
            }

            override fun onQueryTextChange(newCityName: String): Boolean {
//                citiesViewModel.setWorldCitiesApiParams(newCityName)
                citiesViewModel.setFilter(newCityName)
//                citiesViewModel.triggerSearchEvent()
                return false
            }
        })
        searchView?.setOnClickListener { view -> Timber.d("clicked view")  }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        citiesRecyclerView.addItemDecoration(decoration)
        setupScrollListener()
        setUpAdapter()

        citiesViewModel.setFilter("")
        citiesViewModel.getWorldCitiesResultMutableLiveData.postValue(citiesViewModel.worldCitiesApiParams)
        progressBar.visibility = View.VISIBLE

        citiesViewModel.getWorldCitiesResultLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is WorldCitiesResultState.Success -> {
                    progressBar.visibility = View.GONE
                    citiesViewModel.saveCloseShowrooms(it.itemList)
                }
                is WorldCitiesResultState.Error -> {
                    progressBar.visibility = View.GONE
                }
                is WorldCitiesResultState.Loading -> {
                    progressBar.visibility = View.GONE
                }
                else -> {}
            }
        }

        viewSwitch.setOnClickListener {

            if (viewSwitch.isChecked){
                map.visibility = View.VISIBLE
                citiesRecyclerView.visibility = View.GONE
            }
            else{
                map.visibility = View.GONE
                citiesRecyclerView.visibility = View.VISIBLE
            }
        }

    }

    private fun setUpAdapter() {
        citiesAdapter = CitiesAdapter()
        citiesRecyclerView.adapter = citiesAdapter

        citiesViewModel.searchByLiveData.observe(viewLifecycleOwner) {
            citiesAdapter.submitList(it)
            if (it.isNotEmpty()) {
                setUpMapMarkers(it)
                emptyState.visibility = View.GONE
            } else {
                emptyState.visibility = View.VISIBLE
            }

        }

    }

    private fun setupScrollListener() {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        citiesRecyclerView.layoutManager = layoutManager
        citiesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                citiesViewModel.cityListScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }

    private fun setUpMapMarkers(cities: MutableList<CityAndCountry>){
        Timber.tag("MARKERSIZE").d("size is ${cities.size}")
        googleMapObject.clear()
        for(cityAndCountry in cities){

            cityAndCountry.city.lat?.let { lat ->
                cityAndCountry.city.lng?.let { lng ->
                LatLng(lat.toDouble(),
                    lng.toDouble())
            } }
                ?.let { latLng ->
                    MarkerOptions()
                        .position(latLng)
                        .title(cityAndCountry.city.name)
                        .snippet(cityAndCountry.city.localName)
                        //                    .icon(icon)
                        .infoWindowAnchor(0.5f, 0.5f)
                        .draggable(false)
                        .zIndex(0F)
                }?.let { markerOptions ->
                    googleMapObject.addMarker(
                        markerOptions
                    )
                }
        }
    }

    /**
     * Show all the specified markers on the map
     */
   /*

    private fun addMarkersToMap() {

        val placeDetailsMap = mutableMapOf(
            // Uses a coloured icon
            "BRISBANE" to PlaceDetails(
                position = places.getValue("BRISBANE"),
                title = "Brisbane",
                snippet = "Population: 2,074,200",
                icon = BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            ),

            // Uses a custom icon with the info window popping out of the center of the icon.
            "SYDNEY" to PlaceDetails(
                position = places.getValue("SYDNEY"),
                title = "Sydney",
                snippet = "Population: 4,627,300",
//                icon = BitmapDescriptorFactory.fromResource(R.drawable.arrow),
                infoWindowAnchorX = 0.5f,
                infoWindowAnchorY = 0.5f
            ),

            // Will create a draggable marker. Long press to drag.
            "MELBOURNE" to PlaceDetails(
                position = places.getValue("MELBOURNE"),
                title = "Melbourne",
                snippet = "Population: 4,137,400",
                draggable = true
            ),

            // Use a vector drawable resource as a marker icon.
            "ALICE_SPRINGS" to PlaceDetails(
                position = places.getValue("ALICE_SPRINGS"),
                title = "Alice Springs",
//                icon = vectorToBitmap(
//                    R.drawable.ic_android, Color.parseColor("#A4C639"))
            ),

            // More markers for good measure
            "PERTH" to PlaceDetails(
                position = places.getValue("PERTH"),
                title = "Perth",
                snippet = "Population: 1,738,800"
            ),

            "ADELAIDE" to PlaceDetails(
                position = places.getValue("ADELAIDE"),
                title = "Adelaide",
                snippet = "Population: 1,213,000"
            )

        )

        // add 4 markers on top of each other in Darwin with varying z-indexes
        (0 until 4).map {
            placeDetailsMap.put(
                "DARWIN ${it + 1}", PlaceDetails(
                    position = places.getValue("DARWIN"),
                    title = "Darwin Marker ${it + 1}",
                    snippet = "z-index initially ${it + 1}",
                    zIndex = it.toFloat()
                )
            )
        }

        // place markers for each of the defined locations
        placeDetailsMap.keys.map {
            with(placeDetailsMap.getValue(it)) {
                googleMapObject.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(title)
                        .snippet(snippet)
                        .icon(icon)
                        .infoWindowAnchor(infoWindowAnchorX, infoWindowAnchorY)
                        .draggable(draggable)
                        .zIndex(zIndex))

            }
        }


    }

    */

    override fun onMarkerClick(p0: Marker): Boolean {
        return false
    }

    override fun onInfoWindowClick(p0: Marker) {

    }

    override fun onMarkerDrag(p0: Marker) {

    }

    override fun onMarkerDragEnd(p0: Marker) {

    }

    override fun onMarkerDragStart(p0: Marker) {

    }

    override fun onInfoWindowLongClick(p0: Marker) {

    }

    override fun onInfoWindowClose(p0: Marker) {

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        // return early if the map was not initialised properly
        googleMapObject = googleMap ?: return

        // create bounds that encompass every location we reference
        val boundsBuilder = LatLngBounds.Builder()
        // include all places we have markers for on the map
        places.keys.map { place -> boundsBuilder.include(places.getValue(place)) }
        val bounds = boundsBuilder.build()

        with(googleMapObject) {
            // Hide the zoom controls as the button panel will cover it.
            uiSettings.isZoomControlsEnabled = false

            // Override the default content description on the view, for accessibility mode.
            // Ideally this string would be localised.
            setContentDescription("Map with lots of markers.")

//            moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
//            moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,100,100,50))
            moveCamera(CameraUpdateFactory.newLatLng(LatLng(-27.47093, 153.0235)))
        }

        // Add lots of markers to the googleMap.
//        addMarkersToMap()
    }
}
