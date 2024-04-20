package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarMenu
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentStorageWorkerBinding
import ru.bogdan.patseev_diploma.databinding.FragmentWorkerBinding
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactoryWithApplication
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactoryWithWorker
import ru.bogdan.patseev_diploma.presenter.viewModels.WorkerFragmentViewModel

class WorkerFragment : Fragment() {
    private var _binding: FragmentWorkerBinding? = null
    private val binding get() = _binding!!






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNavMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.wealth -> {
                    val action = WorkerFragmentDirections.actionWorkerFragmentToTabLayoutFragment(
                        (requireActivity().application as MyApplication).worker
                    )
                    findNavController().navigate(action)
                    true
                }
                R.id.storage_wealth ->{
                    val action = WorkerFragmentDirections.actionWorkerFragmentToTabLayoutFragment(
                        (requireActivity().application as MyApplication).storageWorker
                    )
                    findNavController().navigate(action)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}