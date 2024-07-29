package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.databinding.FragmentRecycleViewWithWorkersBinding
import ru.bogdan.patseev_diploma.presenter.recycleViews.WorkersAdapter
import ru.bogdan.patseev_diploma.presenter.states.RecycleViewWorkerState
import ru.bogdan.patseev_diploma.presenter.viewModels.RecycleViewWorkersViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactory
import java.lang.RuntimeException
import javax.inject.Inject

class RecycleViewWithWorkersFragment : Fragment() {
    private var _binding: FragmentRecycleViewWithWorkersBinding? = null
    private val binding get() = _binding!!
    private var mode = UNKNOWN_MODE

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[RecycleViewWorkersViewModel::class.java]
    }
    private val component by lazy {
        (this.activity?.application as MyApplication).component
            .getSubComponentFactory()
            .create(findNavController())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
        component.inject(this)
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
                                when (mode) {
                                    SENDER_MODE -> {
                                        val sender = it
                                        setFragmentResult(
                                            TransactionFragment.REQUEST_KEY_SENDER,
                                            bundleOf(TransactionFragment.BUNDLE_KEY_SENDER to sender)
                                        )
                                        requireActivity().onBackPressedDispatcher.onBackPressed()
                                    }

                                    RECEIVER_MODE -> {
                                        val receiver = it
                                        setFragmentResult(
                                            TransactionFragment.REQUEST_KEY_RECEIVER,
                                            bundleOf(TransactionFragment.BUNDLE_KEY_RECEIVER to receiver)
                                        )
                                        requireActivity().onBackPressedDispatcher.onBackPressed()
                                    }

                                    STORAGE_WORKER_MODE -> {
                                        val worker = it
                                        val action = RecycleViewWithWorkersFragmentDirections
                                            .actionRecycleViewWithWorkersFragmentToTabLayoutFragment(
                                                worker
                                            )
                                        findNavController().navigate(action)
                                    }

                                    else -> throw RuntimeException("Unknown mode")
                                }
                            }
                            adapter.submitList(state.workers)
                            binding.workersListRecycleViewWorkers.adapter = adapter
                            binding.progressBarRecycleViewWorkers.visibility = View.GONE
                        }

                        is RecycleViewWorkerState.ConnectionProblem -> {
                            binding.progressBarRecycleViewWorkers.visibility = View.GONE
                            Toast.makeText(
                                this@RecycleViewWithWorkersFragment.context,
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
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