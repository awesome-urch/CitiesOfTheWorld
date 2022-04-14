package com.example.citiesoftheworld.view.city

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citiesoftheworld.R
import com.example.citiesoftheworld.network.model.WorldCitiesResultState
import com.example.citiesoftheworld.view.MainActivity
import kotlinx.android.synthetic.main.fragment_cities.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
class CitiesFragment : Fragment() {

    private val citiesViewModel : CitiesViewModel by viewModel()

    private lateinit var citiesAdapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cities, container, false)
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

}