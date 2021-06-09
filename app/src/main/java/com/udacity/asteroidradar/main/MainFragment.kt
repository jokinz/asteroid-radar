package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.asteroid.AsteroidAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.asteroid.AsteroidListener
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidViewModelFactory
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val dataSource = AsteroidDatabase.getInstance(application).asteroidDatabaseDao
        val viewModelFactory = AsteroidViewModelFactory(dataSource, application)

        val asteroidViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(MainViewModel::class.java)

        binding.viewModel = asteroidViewModel

        val adapter = AsteroidAdapter(AsteroidListener { asteroid ->
            asteroidViewModel.onAsteroidClick(asteroid)
        })

        asteroidViewModel.triggerDetails.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                asteroidViewModel.onAsteroidClicked()
            }
        })
        binding.asteroidRecycler.adapter = adapter


//        binding.activityMainImageOfTheDay = pic
//        asteroidViewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
//            it?.let {
//
//                asteroidViewModel.onAsteroidClicked()
//            }
//        })

        asteroidViewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
