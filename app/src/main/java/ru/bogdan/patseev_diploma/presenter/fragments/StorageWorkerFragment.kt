package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentStorageWorkerBinding
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.presenter.recycleViews.TransactionsAdapter
import ru.bogdan.patseev_diploma.presenter.states.StorageWorkerFragmentState
import ru.bogdan.patseev_diploma.presenter.viewModels.StorageWorkerViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactory
import ru.bogdan.patseev_diploma.util.toNormalName
import javax.inject.Inject


class StorageWorkerFragment : Fragment() {
    private var _binding: FragmentStorageWorkerBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[StorageWorkerViewModel::class.java]
    }
    private val component by lazy {
        (this.activity?.application as MyApplication).component
            .getSubComponentFactory()
            .create(findNavController())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
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
        observeViewModel(binding, viewModel)
    }


    private fun setOnClickListeners(
        binding: FragmentStorageWorkerBinding,
        viewModel: StorageWorkerViewModel,
        worker:Worker,
        sharpen:Worker,
        storageOfDecommissionedTools:Worker
    ) {
        binding.updateTransactionsStorageWorkerFragment.setOnClickListener {
            viewModel.updateTransactions()
        }

        binding.navigateView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_camera -> {
                    findNavController().navigate(R.id.action_storageWorkerFragment_to_cameraFragment)
                }

                R.id.item_storage_wealth -> {
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToTabLayoutFragment(worker)
                    findNavController().navigate(action)
                }

                R.id.item_take_tool -> {
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToTransactionFragment(receiver = worker)
                    findNavController().navigate(action)
                }

                R.id.item_to_sharpen -> {
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToTransactionFragment(
                            receiver = sharpen,
                            sender = worker
                        )
                    findNavController().navigate(action)
                }

                R.id.item_from_sharpen -> {
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToTransactionFragment(
                            receiver = worker,
                            sender = sharpen
                        )
                    findNavController().navigate(action)
                }

                R.id.item_storage_of_decommissioned_tools -> {
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToTransactionFragment(
                            receiver = storageOfDecommissionedTools,
                            sender = worker
                        )
                    findNavController().navigate(action)
                }

                R.id.item_give_tool -> {
                    val action = StorageWorkerFragmentDirections
                        .actionStorageWorkerFragmentToTransactionFragment(sender = worker)
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

    private fun initViews(binding: FragmentStorageWorkerBinding, worker:Worker) {
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

                            setOnClickListeners(
                                binding =  binding,
                                viewModel =  viewModel,
                                worker = it.worker,
                                sharpen = it.sharpen,
                                storageOfDecommissionedTools = it.storageOfDecommissionedTools
                            )
                            initViews(
                               binding = binding,
                                worker = it.worker
                            )
                        }

                        is StorageWorkerFragmentState.Loading -> {
                            binding.progressBarStorageWorkerFragment.visibility = View.VISIBLE
                        }

                        is StorageWorkerFragmentState.ConnectionProblem -> {
                            binding.progressBarStorageWorkerFragment.visibility = View.GONE
                            Toast.makeText(
                                this@StorageWorkerFragment.context,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
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