package ru.bogdan.patseev_diploma.presenter

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentStorageWorkerBinding
import ru.bogdan.patseev_diploma.databinding.FragmentWorkerBinding

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
        menu(binding)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun menu(binding: FragmentWorkerBinding) {
//






    }

}