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
import ru.bogdan.patseev_diploma.databinding.FragmentRecycleViewCuttingToolsBinding
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.presenter.recycleViews.StorageRecordsAdapter
import ru.bogdan.patseev_diploma.presenter.states.RecycleViewState
import ru.bogdan.patseev_diploma.presenter.viewModels.RecycleViewViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactoryWithWorker




class RecycleViewStorageRecordsFragment : Fragment() {
    private var _binding: FragmentRecycleViewCuttingToolsBinding? = null
    private val binding get() = _binding!!

    private var position: Int = 0
    @Suppress("DEPRECATION")
    // min SDK lower than 33 (TIRAMISU)
    private val worker:Worker by lazy {
        requireArguments().getParcelable(WORKER)!!
    }
    private val adapter by lazy {
        StorageRecordsAdapter { storageRecord ->
            val tool = storageRecord.tool
            val action =
                TabLayoutFragmentDirections.actionTabLayoutFragmentToToolFragment2(tool)
            findNavController().navigate(action)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt(POSITION) ?: -1
        viewModelFactory = ViewModelFactoryWithWorker(worker)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecycleViewCuttingToolsBinding.inflate(inflater, container, false)
        return binding.root
    }
    private lateinit var viewModelFactory:ViewModelFactoryWithWorker

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[RecycleViewViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel(binding, viewModel)
        }





    private fun observeViewModel(
        binding: FragmentRecycleViewCuttingToolsBinding,
        viewViewModel: RecycleViewViewModel
    ){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewViewModel.state.collect{state ->
                    when (state) {
                        is RecycleViewState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is RecycleViewState.Result -> {
                            binding.progressBar.visibility = View.GONE
                            adapter.submitList(viewModel.getList(state.records,position))
                            binding.cuttingTools.adapter = adapter
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



    companion object {
        private const val POSITION = "position"
        private const val WORKER = "worker"

        @JvmStatic
        fun newInstance(position: Int, worker: Worker) =
            RecycleViewStorageRecordsFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                    putParcelable(WORKER,worker)
                }
            }
    }
}