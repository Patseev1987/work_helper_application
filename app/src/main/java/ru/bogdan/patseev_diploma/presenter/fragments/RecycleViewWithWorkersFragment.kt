package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
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

class RecycleViewWithWorkersFragment : Fragment() {
    private var _binding: FragmentRecycleViewWithWorkersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFactory:ViewModelFactoryWithApplication
    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[RecycleViewWorkersViewModel::class.java]
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
        _binding = FragmentRecycleViewWithWorkersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAfterTextChange(binding,viewModel)
        observeViewModel(binding,viewModel)
    }

    private fun observeViewModel(
        binding: FragmentRecycleViewWithWorkersBinding,
        viewModel: RecycleViewWorkersViewModel
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.state.collect{state ->
                    when (state) {
                        is RecycleViewWorkerState.Loading ->{
                            binding.progressBarRecycleViewWorkers.visibility = View.VISIBLE
                        }

                        is RecycleViewWorkerState.Result -> {
                            val adapter = WorkersAdapter(){

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

    private fun setAfterTextChange(binding: FragmentRecycleViewWithWorkersBinding, viewWorkersViewModel: RecycleViewWorkersViewModel){
        binding.inEditTextRecycleViewWorkers.doAfterTextChanged {
            viewWorkersViewModel.searchString.value = it.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}