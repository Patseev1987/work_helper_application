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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.databinding.FragmentRecycleViewTransactionBinding
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.presenter.recycleViews.TransactionsAdapter
import ru.bogdan.patseev_diploma.presenter.states.RecycleVIewTransactionState
import ru.bogdan.patseev_diploma.presenter.viewModels.RecycleViewTransactionsViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactoryWithApplication

class RecycleViewTransactionFragment : Fragment() {
    private var _binding: FragmentRecycleViewTransactionBinding? = null
    private val binding get() = _binding!!

    private var anotherDepartment = Department.DEPARTMENT_19

    private lateinit var viewModelFactory:ViewModelFactoryWithApplication
    private val viewModel:RecycleViewTransactionsViewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[RecycleViewTransactionsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseMode()
        viewModelFactory = ViewModelFactoryWithApplication(
            requireActivity().application as MyApplication
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecycleViewTransactionBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState(binding,viewModel)
        viewModel.loadTransactions(anotherDepartment)
        setOnChangeText(binding,viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setOnChangeText(
        binding: FragmentRecycleViewTransactionBinding,
        viewModel: RecycleViewTransactionsViewModel
    ){
        binding.inEditTextRecycleViewTransactions.doAfterTextChanged {
            viewModel.updateTransactionWithFilter(anotherDepartment,it.toString())
        }
    }



    private fun observeState(
        binding: FragmentRecycleViewTransactionBinding,
        viewModel: RecycleViewTransactionsViewModel
    ){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.state.collectLatest{state ->
                        when (state) {
                            is RecycleVIewTransactionState.Loading -> {
                                binding.progressBarRecycleViewTransactions.visibility = View.VISIBLE
                            }
                            is RecycleVIewTransactionState.Result -> {
                                binding.twRecycleViewTransactionsLabel.text  = state.message
                                val adapter = TransactionsAdapter()
                                binding.recycleViewTransactions.adapter = adapter
                                adapter.submitList(state.transactions)
                                binding.progressBarRecycleViewTransactions.visibility = View.GONE
                            }
                        }
                }
            }
        }
    }

    private fun parseMode(){
      anotherDepartment = RecycleViewTransactionFragmentArgs.fromBundle(requireArguments()).anotherDepartment
    }

}