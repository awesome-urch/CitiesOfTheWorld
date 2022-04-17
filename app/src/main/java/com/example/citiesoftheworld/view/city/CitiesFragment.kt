package com.example.citiesoftheworld.view.city

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_cities.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel


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
    private lateinit var googleMapObject: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_cities, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

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
                citiesViewModel.setFilter(cityName)
                return false
            }

            override fun onQueryTextChange(newCityName: String): Boolean {
                citiesViewModel.setFilter(newCityName)
                return false
            }
        })

        val item = menu.findItem(R.id.menu_spinner)
        val spinner = item.actionView as Spinner

        ArrayAdapter.createFromResource(
            context as MainActivity,
            R.array.views,
            R.layout.toolbar_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.toolbar_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                    when (position) {
                        LIST_VIEW_POSITION -> {
                            citiesViewModel.setMapViewVisible(false)
                        }
                        MAP_VIEW_POSITION -> {
                            citiesViewModel.setMapViewVisible(true)
                        }
                    }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // do nothing
            }
        }

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
                    progressBar.visibility = View.VISIBLE
                }
                else -> {}
            }
        }

        citiesViewModel.mapViewVisible.observe(viewLifecycleOwner) {
            if(it){
                map.visibility = View.VISIBLE
                citiesRecyclerView.visibility = View.GONE
            }else{
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

        if(this::googleMapObject.isInitialized){
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
                            .infoWindowAnchor(0.5f, 0.5f)
                            .draggable(false)
                            .zIndex(0F)
                    }?.let { markerOptions ->
                        googleMapObject.addMarker(
                            markerOptions
                        )
                        googleMapObject.moveCamera(CameraUpdateFactory.newLatLng(markerOptions.position))
                    }
            }
        }
    }

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

        with(googleMapObject) {
            // Hide the zoom controls as the button panel will cover it.
            uiSettings.isZoomControlsEnabled = false

            moveCamera(CameraUpdateFactory.newLatLng(citiesViewModel.defaultLatLng))
        }

    }

    companion object {
        private const val LIST_VIEW_POSITION = 0
        private const val MAP_VIEW_POSITION = 1
    }

}
