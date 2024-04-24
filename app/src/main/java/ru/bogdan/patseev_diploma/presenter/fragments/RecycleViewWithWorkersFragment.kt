package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.databinding.FragmentRecycleViewWithWorkersBinding
import ru.bogdan.patseev_diploma.presenter.recycleViews.WorkersAdapter
import ru.bogdan.patseev_diploma.presenter.states.RecycleViewWorkerState
import ru.bogdan.patseev_diploma.presenter.viewModels.RecycleViewWorkersViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactoryWithApplication
import java.lang.RuntimeException

class RecycleViewWithWorkersFragment : Fragment() {
    private var _binding: FragmentRecycleViewWithWorkersBinding? = null
    private val binding get() = _binding!!
    private var mode = UNKNOWN_MODE

    private lateinit var viewModelFactory: ViewModelFactoryWithApplication
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[RecycleViewWorkersViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ViewModelFactoryWithApplication(
            requireActivity().application as MyApplication
        )
        parseArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecycleViewWithWorkersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAfterTextChange(binding, viewModel)
        observeViewModel(binding, viewModel)
    }

    private fun observeViewModel(
        binding: FragmentRecycleViewWithWorkersBinding,
        viewModel: RecycleViewWorkersViewModel
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is RecycleViewWorkerState.Loading -> {
                            binding.progressBarRecycleViewWorkers.visibility = View.VISIBLE
                        }

                        is RecycleViewWorkerState.Result -> {
                            val adapter = WorkersAdapter {
                                val kkk = when (mode) {
                                    SENDER_MODE -> {
                                        val sender = it
                                        setFragmentResult(
                                            TransactionFragment.REQUEST_KEY_SENDER ,
                                            bundleOf(TransactionFragment.BUNDLE_KEY_SENDER to sender)
                                        )
                                        requireActivity().onBackPressedDispatcher.onBackPressed()
                                    }

                                    RECEIVER_MODE -> {
                                        val receiver = it
                                        setFragmentResult(
                                            TransactionFragment.REQUEST_KEY_RECEIVER ,
                                            bundleOf(TransactionFragment.BUNDLE_KEY_RECEIVER to receiver)
                                        )
                                        requireActivity().onBackPressedDispatcher.onBackPressed()
                                    }

                                    STORAGE_WORKER_MODE -> {

                                    }
                                    else -> throw RuntimeException("Unknown mode")
                                }
                            }
                            adapter.submitList(state.workers)
                            binding.workersListRecycleViewWorkers.adapter = adapter
                            binding.progressBarRecycleViewWorkers.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun setAfterTextChange(
        binding: FragmentRecycleViewWithWorkersBinding,
        viewWorkersViewModel: RecycleViewWorkersViewModel
    ) {
        binding.inEditTextRecycleViewWorkers.doAfterTextChanged {
            viewWorkersViewModel.searchString.value = it.toString()
        }
    }

    private fun parseArguments() {
        mode = RecycleViewWithWorkersFragmentArgs.fromBundle(requireArguments()).mode
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val STORAGE_WORKER_MODE = 101
        const val SENDER_MODE = 102
        const val RECEIVER_MODE = 103
        private const val UNKNOWN_MODE = -1
    }
}