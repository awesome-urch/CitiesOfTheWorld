package com.example.citiesoftheworld.view.city

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citiesoftheworld.R
import com.example.citiesoftheworld.network.model.WorldCitiesResultState
import com.example.citiesoftheworld.utils.OnMapAndViewReadyListener
import com.example.citiesoftheworld.view.MainActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.fragment_cities.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


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
    private lateinit var googleMap1: GoogleMap
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

//        citiesViewModel.observeSearchEvent.observe(viewLifecycleOwner) {
//            citiesViewModel.getWorldCitiesResultMutableLiveData.postValue(citiesViewModel.worldCitiesApiParams)
////
//        }

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

            citiesRecyclerView.isVisible = viewSwitch.isChecked
            map.isVisible = !viewSwitch.isChecked

//            if (viewSwitch.isChecked){
//
////                main.setBackgroundColor(Color.DKGRAY)
////                switch1.setTextColor(Color.WHITE)
//            }
//            else{
//
////                main.setBackgroundColor(Color.WHITE)
////                switch1.setTextColor(Color.BLACK)
//            }
        }

    }

    private fun setUpAdapter() {
        citiesAdapter = CitiesAdapter()
        citiesRecyclerView.adapter = citiesAdapter

        citiesViewModel.searchByLiveData.observe(viewLifecycleOwner) {
            citiesAdapter.submitList(it)
            if (it.isNotEmpty()) {
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

    override fun onMarkerClick(p0: Marker): Boolean {
        TODO("Not yet implemented")
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
        googleMap1 = googleMap ?: return

        // create bounds that encompass every location we reference
        val boundsBuilder = LatLngBounds.Builder()
        // include all places we have markers for on the map
        places.keys.map { place -> boundsBuilder.include(places.getValue(place)) }
        val bounds = boundsBuilder.build()

        with(googleMap1) {
            // Hide the zoom controls as the button panel will cover it.
            uiSettings.isZoomControlsEnabled = false

            // Setting an info window adapter allows us to change the both the contents and
            // look of the info window.
//            setInfoWindowAdapter(CustomInfoWindowAdapter())
//
//            // Set listeners for marker events.  See the bottom of this class for their behavior.
//            setOnMarkerClickListener(this@MarkerDemoActivity)
//            setOnInfoWindowClickListener(this@MarkerDemoActivity)
//            setOnMarkerDragListener(this@MarkerDemoActivity)
//            setOnInfoWindowCloseListener(this@MarkerDemoActivity)
//            setOnInfoWindowLongClickListener(this)

            // Override the default content description on the view, for accessibility mode.
            // Ideally this string would be localised.
//            setContentDescription("Map with lots of markers.")

//            moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
        }

        // Add lots of markers to the googleMap.
//        addMarkersToMap()
    }

}