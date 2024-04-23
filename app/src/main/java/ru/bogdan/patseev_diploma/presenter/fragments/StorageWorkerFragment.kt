package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentStorageWorkerBinding
import ru.bogdan.patseev_diploma.databinding.FragmentWorkerBinding
import ru.bogdan.patseev_diploma.presenter.recycleViews.TransactionsAdapter
import ru.bogdan.patseev_diploma.presenter.states.StorageWorkerFragmentState
import ru.bogdan.patseev_diploma.presenter.viewModels.StorageWorkerViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactoryWithApplication
import ru.bogdan.patseev_diploma.util.toNormalName


class StorageWorkerFragment : Fragment() {
    private var _binding:FragmentStorageWorkerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFactory:ViewModelFactoryWithApplication
    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[StorageWorkerViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ViewModelFactoryWithApplication(requireActivity().application as MyApplication)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStorageWorkerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener(binding)
        initViews(binding)
        observeViewModel(binding,viewModel)
    }


    private fun setOnClickListener(binding: FragmentStorageWorkerBinding) {
        binding.navigateView.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.item_camera -> {
                    findNavController().navigate(R.id.action_storageWorkerFragment_to_cameraFragment)
                }
                R.id.item_give_tool -> {

                }
                R.id.item_sharpen -> {

                }
                R.id.item_write_off -> {

                }
                R.id.item_take_tool -> {

                }
                R.id.item_workers -> {
                    findNavController().navigate(R.id.action_storageWorkerFragment_to_recycleViewWithWorkersFragment)
                }
            }
            true
        }
    }

    private fun initViews(binding: FragmentStorageWorkerBinding){
        val worker = (this.requireActivity().application as MyApplication).worker

        binding.twNameStorageWorker.text = worker.firstName
        binding.twDepartmentStorageWorker.text = worker.department.toNormalName()

    }
    private fun observeViewModel(
        binding: FragmentStorageWorkerBinding,
        viewModel: StorageWorkerViewModel
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    when (it) {
                        is StorageWorkerFragmentState.ResultsTransaction -> {
                            val adapter = TransactionsAdapter()
                            binding.storageWorkerTransactions.adapter = adapter
                            adapter.submitList(it.transactions)
                            binding.progressBarStorageWorkerFragment.visibility = View.GONE
                        }
                        is StorageWorkerFragmentState.Loading -> {
                            binding.progressBarStorageWorkerFragment.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}