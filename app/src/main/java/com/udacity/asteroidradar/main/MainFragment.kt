package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.util.Injection
import com.udacity.asteroidradar.util.Show
import com.udacity.asteroidradar.util.getViewModel
import timber.log.Timber

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        getViewModel {
            MainViewModel(
                requireActivity().application,
                Injection.provideAsteroidRepo(requireContext())
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)

        binding.statusLoadingWheel.visibility = View.VISIBLE

        val adapter = MainFragmentAdapter(AsteroidListener {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })
        binding.asteroidRecycler.adapter = adapter

        Timber.i("++++ recyclerview visible: ${binding.asteroidRecycler.visibility == View.VISIBLE}")
        // Load asteroid list
        viewModel.asteroids.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                binding.statusLoadingWheel.visibility = View.GONE
            adapter.submitList(it)
        }

        // Load asteroid picture of the day
        viewModel.apod.observe(viewLifecycleOwner) {
            it?.let {
                Glide.with(requireContext())
                    .load(it.url)
                    .apply {
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image)
                    }
                    .into(binding.activityMainImageOfTheDay)

                binding.activityMainImageOfTheDay.contentDescription =
                    getString(R.string.nasa_picture_of_day_content_description_format, it.title)
            }
        }

        // Check connection and give a warning if there is no connection
        viewModel.connection.observe(viewLifecycleOwner) {
            it?.let { isConnected ->
                if (!isConnected) {
                    Toast.makeText(
                        requireContext(),
                        R.string.no_internet_connection,
                        Toast.LENGTH_LONG
                    ).show()
                    binding.statusLoadingWheel.visibility = View.GONE
                    viewModel.checkConnectionDone()
                }
            }
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_week_asteroid -> {
                viewModel.search(Show.WEEKS)
                true
            }
            R.id.show_today_asteroid -> {
                viewModel.search(Show.TODAY)
                true
            }
            R.id.show_saved_asteroid -> {
                viewModel.search(Show.SAVED)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
