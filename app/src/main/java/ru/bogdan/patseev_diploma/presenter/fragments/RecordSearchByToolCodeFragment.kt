package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import android.util.Log
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
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentRecordSearchByToolCodeBinding
import ru.bogdan.patseev_diploma.databinding.FragmentToolsForSearchBinding
import ru.bogdan.patseev_diploma.presenter.recycleViews.StorageRecordsAdapter
import ru.bogdan.patseev_diploma.presenter.recycleViews.StorageRecordsSearchAdapter
import ru.bogdan.patseev_diploma.presenter.recycleViews.ToolsAdapter
import ru.bogdan.patseev_diploma.presenter.states.FragmentSearchToolsState
import ru.bogdan.patseev_diploma.presenter.states.RecordsSearchByToolCodeState
import ru.bogdan.patseev_diploma.presenter.viewModels.RecordsSearchByToolCodeViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ToolsSearchFragmentViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactory
import javax.inject.Inject


class RecordSearchByToolCodeFragment : Fragment() {

    private var _binding: FragmentRecordSearchByToolCodeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[RecordsSearchByToolCodeViewModel::class.java]
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
        _binding = FragmentRecordSearchByToolCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel(binding, viewModel)
        setListeners(binding, viewModel)

    }


    private fun setListeners(
        binding: FragmentRecordSearchByToolCodeBinding,
        viewModel: RecordsSearchByToolCodeViewModel
    ) {
        binding.inEditTextRecycleViewWorkers.doAfterTextChanged {
            viewModel.searchString.value = it.toString()
        }
    }

    private fun observeViewModel(
        binding: FragmentRecordSearchByToolCodeBinding,
        viewModel: RecordsSearchByToolCodeViewModel
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { state ->
                    Log.d("State_State", state.toString())
                    when (state) {
                        is RecordsSearchByToolCodeState.Loading -> {
                            binding.progressBarRecordsSearch.visibility = View.VISIBLE
                        }

                        is RecordsSearchByToolCodeState.Result -> {
                            val adapter = StorageRecordsSearchAdapter {}
                            binding.recordsSearchRecycleView.adapter = adapter
                            adapter.submitList(state.records)
                            binding.progressBarRecordsSearch.visibility = View.GONE
                        }

                        is RecordsSearchByToolCodeState.Waiting -> {
                            Toast.makeText(
                                this@RecordSearchByToolCodeFragment.context,
                                getString(R.string.you_should_enter_4_number_of_tool_code),
                                Toast.LENGTH_LONG
                            ).show()
                            binding.progressBarRecordsSearch.visibility = View.GONE
                        }

                        is RecordsSearchByToolCodeState.ConnectionProblem -> {
                            Toast.makeText(
                                this@RecordSearchByToolCodeFragment.context,
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBarRecordsSearch.visibility = View.GONE
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