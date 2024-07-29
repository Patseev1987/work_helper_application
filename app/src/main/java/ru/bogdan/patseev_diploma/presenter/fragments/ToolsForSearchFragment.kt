package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentToolsForSearchBinding
import ru.bogdan.patseev_diploma.presenter.recycleViews.ToolsAdapter
import ru.bogdan.patseev_diploma.presenter.states.FragmentSearchToolsState
import ru.bogdan.patseev_diploma.presenter.viewModels.ToolsSearchFragmentViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactory
import javax.inject.Inject

class ToolsForSearchFragment : Fragment() {
    private var _binding: FragmentToolsForSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ToolsSearchFragmentViewModel::class.java]
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
        _binding = FragmentToolsForSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel(binding, viewModel)
        setListeners(binding, viewModel)

    }


    private fun setListeners(
        binding: FragmentToolsForSearchBinding,
        viewModel: ToolsSearchFragmentViewModel
    ) {
        binding.inEditTextSearchTools.doAfterTextChanged {
            viewModel.searchString.value = it.toString()
        }
    }

    private fun observeViewModel(
        binding: FragmentToolsForSearchBinding,
        viewModel: ToolsSearchFragmentViewModel
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is FragmentSearchToolsState.Loading -> {
                            binding.progressBarSearchTools.visibility = View.VISIBLE
                        }

                        is FragmentSearchToolsState.Result -> {
                            val adapter = ToolsAdapter { tool ->
                                setFragmentResult(
                                    TransactionFragment.REQUEST_KEY_TOOL,
                                    bundleOf(TransactionFragment.BUNDLE_KEY_TOOL to tool)
                                )
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                            }
                            binding.toolsSearchTools.adapter = adapter
                            adapter.submitList(state.tools)
                            binding.progressBarSearchTools.visibility = View.GONE
                        }

                        is FragmentSearchToolsState.Waiting -> {
                            Toast.makeText(
                                this@ToolsForSearchFragment.context,
                                getString(R.string.you_should_enter_4_number_of_tool_code),
                                Toast.LENGTH_LONG
                            ).show()
                            binding.progressBarSearchTools.visibility = View.GONE

                        }

                        is FragmentSearchToolsState.ConnectionProblem -> {
                            Toast.makeText(
                                this@ToolsForSearchFragment.context,
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBarSearchTools.visibility = View.GONE
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