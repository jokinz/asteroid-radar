package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.asteroid.AsteroidAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.asteroid.Asteroid
import com.udacity.asteroidradar.asteroid.AsteroidListener
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidViewModelFactory
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, AsteroidViewModelFactory(activity.application)).get(MainViewModel::class.java)

    }

    private var viewModelAdapter: AsteroidAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

//        val application = requireNotNull(this.activity).application
//        val viewModelFactory = AsteroidViewModelFactory(application)
//
//        val asteroidViewModel =
//            ViewModelProvider(
//                this, viewModelFactory
//            ).get(MainViewModel::class.java)


        val adapter = AsteroidAdapter(AsteroidListener { asteroid ->
            viewModel.onAsteroidClick(asteroid)
        })
//
        viewModel.triggerDetails.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.onAsteroidClicked()
            }
        })
        binding.asteroidRecycler.adapter = adapter

//        asteroidViewModel.asteroids.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                adapter.submitList(it)
//            }
//        })
        viewModel.asteroidList.observe(viewLifecycleOwner, Observer<List<Asteroid>> {
            it?.let {
                adapter.submitList(it)
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }

//    class MainFragment : Fragment() {
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val binding = FragmentMainBinding.inflate(inflater)
//        binding.lifecycleOwner = this
//
//        val application = requireNotNull(this.activity).application
//        val dataSource = AsteroidDatabase.getInstance(application).asteroidDatabaseDao
//        val viewModelFactory = AsteroidViewModelFactory(dataSource, application)
//
//        val asteroidViewModel =
//            ViewModelProvider(
//                this, viewModelFactory
//            ).get(MainViewModel::class.java)
//
//        binding.viewModel = asteroidViewModel
//
//        val adapter = AsteroidAdapter(AsteroidListener { asteroid ->
//            asteroidViewModel.onAsteroidClick(asteroid)
//        })
//
//        asteroidViewModel.triggerDetails.observe(viewLifecycleOwner, Observer { asteroid ->
//            asteroid?.let {
//                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
//                asteroidViewModel.onAsteroidClicked()
//            }
//        })
//        binding.asteroidRecycler.adapter = adapter
//
//        asteroidViewModel.asteroids.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                adapter.submitList(it)
//            }
//        })
//
//        setHasOptionsMenu(true)
//        return binding.root
//    }
//
//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
