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
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.databinding.FragmentRecycleViewToolsBinding
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.presenter.recycleViews.StorageRecordsAdapter
import ru.bogdan.patseev_diploma.presenter.states.RecycleViewState
import ru.bogdan.patseev_diploma.presenter.viewModels.RecycleViewStorageRecordsViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactory
import javax.inject.Inject


class RecycleViewStorageRecordsFragment : Fragment() {
    private var _binding: FragmentRecycleViewToolsBinding? = null
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

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[RecycleViewStorageRecordsViewModel::class.java]
    }

    private val component by lazy {
        (this.activity?.application as MyApplication).component
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt(POSITION) ?: -1
       component.inject(this)
        viewModel.setWorker(worker)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecycleViewToolsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel(binding, viewModel)
        viewModel.loadTools(position)
        setTextChangeListener(binding,viewModel)
        println(worker.toString())
        }

    private fun setTextChangeListener(
        binding: FragmentRecycleViewToolsBinding,
        viewViewModel: RecycleViewStorageRecordsViewModel
    ){
        binding.inEditTextRecycleViewTool.doAfterTextChanged {
            viewViewModel.updateToolsWithFilter(position, it.toString())
        }
    }

    private fun observeViewModel(
        binding: FragmentRecycleViewToolsBinding,
        viewViewModel: RecycleViewStorageRecordsViewModel
    ){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewViewModel.state.collect{state ->
                    when (state) {
                        is RecycleViewState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is RecycleViewState.Result -> {
                            adapter.submitList(state.records)
                            binding.toolsRecycleViewTools.adapter = adapter
                            binding.progressBar.visibility = View.GONE
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
        const val WORKER = "worker"

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