package com.example.citiesoftheworld.view.city

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citiesoftheworld.R
import com.example.citiesoftheworld.network.model.WorldCitiesResultState
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

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Timber.d("oncreated called")
    }*/

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
//                viewModel.clearCompletedTasks()
                true
            }
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.world_cities_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupScrollListener()
        setUpAdapter()

        citiesViewModel.setWorldCitiesApiParams("")
        citiesViewModel.getWorldCitiesResultMutableLiveData.postValue(citiesViewModel.worldCitiesApiParams)
        progressBar.visibility = View.VISIBLE

        citiesViewModel.getWorldCitiesResultLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is WorldCitiesResultState.Success -> {
                    progressBar.visibility = View.GONE
                    Timber.d("SUCCESS!!")
                    citiesViewModel.saveCloseShowrooms(it.itemList)
                }
                is WorldCitiesResultState.Error -> {
                    progressBar.visibility = View.GONE
                    Timber.d("ERROR!!")
                }
                is WorldCitiesResultState.Loading -> {

                    Timber.d("LOADING!!")
                }
                else -> {}
            }
        }
    }

    private fun setUpAdapter() {
        citiesAdapter = CitiesAdapter()
        citiesRecyclerView.adapter = citiesAdapter

//        profileViewModel.getUserContactsLiveData().observe(viewLifecycleOwner, Observer {
//            Timber.d("list are: $it")
//
//            if(it.isEmpty()){
//
//                if(!profileViewModel.isCheckingContacts){
//                    emptyStateLayout.visibility = View.VISIBLE
//                    emptyStateButton.visibility = View.VISIBLE
//                    emptyStateButton.text = getString(R.string.invite_friends)
//                    emptyStateMessage.text = String.format(" ${getString(R.string.none_of_your_contact_is_on_myshowroom)}")
//                }
//
//            }else{
//                emptyStateLayout.visibility = View.GONE
//                userAdapter.submitList(it)
//            }
//
//            var userText = getString(R.string.contacts_lowercase)
//            if(it.size == 1) {
//                userText = getString(R.string.contact_lowercase)
//            }
//
//            contactsToolbar.title = getString(R.string.select_contact)
//            contactsToolbar.subtitle = "${MyShowroomAppUtils.coolNumberFormat(it.size.toLong())} $userText"
//        })
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