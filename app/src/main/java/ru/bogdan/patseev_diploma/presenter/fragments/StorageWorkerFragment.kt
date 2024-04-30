package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
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
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.presenter.recycleViews.TransactionsAdapter
import ru.bogdan.patseev_diploma.presenter.states.StorageWorkerFragmentState
import ru.bogdan.patseev_diploma.presenter.viewModels.StorageWorkerViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactoryWithApplication
import ru.bogdan.patseev_diploma.util.toNormalName


class StorageWorkerFragment : Fragment() {
    private var _binding: FragmentStorageWorkerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: ViewModelFactoryWithApplication
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[StorageWorkerViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory =
            ViewModelFactoryWithApplication(requireActivity().application as MyApplication)
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
        setOnClickListeners(binding, viewModel)
        initViews(binding)
        observeViewModel(binding, viewModel)
    }


    private fun setOnClickListeners(
        binding: FragmentStorageWorkerBinding,
        viewModel: StorageWorkerViewModel
    ) {
        binding.updateTransactionsStorageWorkerFragment.setOnClickListener {
            viewModel.updateTransactions()
        }

        binding.navigateView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_camera -> {
                    findNavController().navigate(R.id.action_storageWorkerFragment_to_cameraFragment)
                }

                R.id.item_take_tool -> {
                    val receiver = (this.requireActivity().application as MyApplication).worker
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToTransactionFragment(receiver = receiver)
                    findNavController().navigate(action)
                }

                R.id.item_to_sharpen -> {
                    val receiver = viewModel.sharpen
                    val sender = (this.requireActivity().application as MyApplication).worker
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToTransactionFragment(
                            receiver = receiver,
                            sender = sender
                        )
                    findNavController().navigate(action)
                }

                R.id.item_from_sharpen -> {
                    val sender = viewModel.sharpen
                    val receiver = (this.requireActivity().application as MyApplication).worker
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToTransactionFragment(
                            receiver = receiver,
                            sender = sender
                        )
                    findNavController().navigate(action)
                }

                R.id.item_storage_of_decommissioned_tools -> {
                    val receiver = viewModel.storageOfDecommissionedTools
                    val sender = (this.requireActivity().application as MyApplication).worker
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToTransactionFragment(
                            receiver = receiver,
                            sender = sender
                        )
                    findNavController().navigate(action)
                }

                R.id.item_give_tool -> {
                    val sender = (this.requireActivity().application as MyApplication).worker
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToTransactionFragment(sender = sender)
                    findNavController().navigate(action)
                }

                R.id.item_workers -> {
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToRecycleViewWithWorkersFragment(
                            RecycleViewWithWorkersFragment.STORAGE_WORKER_MODE
                        )
                    findNavController().navigate(action)
                }

                R.id.item_list_of_decommissioned_tools -> {
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToRecycleViewTransactionFragment(
                            Department.STORAGE_OF_DECOMMISSIONED_TOOLS
                        )
                    findNavController().navigate(action)
                }

                R.id.item_list_of_tools_sharpen -> {
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToRecycleViewTransactionFragment(
                            Department.SHARPENING
                        )
                    findNavController().navigate(action)
                }

                R.id.item_list_of_tools_from_main_storage -> {
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToRecycleViewTransactionFragment(
                            Department.MAIN_STORAGE
                        )
                    findNavController().navigate(action)
                }

            }
            true
        }
    }

    private fun initViews(binding: FragmentStorageWorkerBinding) {
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