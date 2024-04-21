package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentWorkerBinding
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.presenter.recycleViews.TransactionsAdapter
import ru.bogdan.patseev_diploma.presenter.states.WorkerFragmentState
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactoryWithApplication
import ru.bogdan.patseev_diploma.presenter.viewModels.WorkerFragmentViewModel
import ru.bogdan.patseev_diploma.util.toNormalName


class WorkerFragment : Fragment() {
    private var _binding: FragmentWorkerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: ViewModelFactoryWithApplication

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[WorkerFragmentViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ViewModelFactoryWithApplication(
            requireActivity().application as MyApplication
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(binding, (requireActivity().application as MyApplication).worker)
        observeViewModel(binding,viewModel)
        setListener(binding)
    }

    //set listener for bottom bar menu
    private fun setListener(binding: FragmentWorkerBinding) {
        binding.bottomNavMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.wealth -> {
                    val action = WorkerFragmentDirections.actionWorkerFragmentToTabLayoutFragment(
                        (requireActivity().application as MyApplication).worker
                    )
                    findNavController().navigate(action)
                    true
                }

                R.id.storage_wealth -> {
                    val action = WorkerFragmentDirections.actionWorkerFragmentToTabLayoutFragment(
                        (requireActivity().application as MyApplication).storageWorker
                    )
                    findNavController().navigate(action)
                    true
                }

                R.id.cameraFragment -> {
                    findNavController().navigate(R.id.action_workerFragment_to_cameraFragment)
                    true
                }

                else -> {
                    false
                }
            }
        }
    }


    private fun observeViewModel(
        binding: FragmentWorkerBinding,
        viewModel: WorkerFragmentViewModel
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    when (it) {
                        is WorkerFragmentState.ResultsTransaction -> {
                            Log.d("apiService",it.transactions.toString())
                            val adapter = TransactionsAdapter()
                            binding.workerTransactions.adapter = adapter
                          adapter.submitList(it.transactions)
                            binding.progressBarWorkerFragment.visibility = View.GONE
                        }
                        is WorkerFragmentState.Loading -> {
                            binding.progressBarWorkerFragment.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    //init all views
    private fun initView(binding: FragmentWorkerBinding, worker: Worker) {
        binding.twName.text = worker.firstName
        binding.twSurname.text = worker.secondName
        binding.twPatronymic.text = worker.patronymic
        binding.twDepartment.text = worker.department.toNormalName()
        binding.twJoinDate.text = worker.joinDate.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}