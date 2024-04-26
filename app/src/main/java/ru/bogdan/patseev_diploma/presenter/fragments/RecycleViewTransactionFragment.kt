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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.databinding.FragmentRecycleViewTransactionBinding
import ru.bogdan.patseev_diploma.presenter.viewModels.RecycleViewTransactionsViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactoryWithApplication
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactoryWithMode

class RecycleViewTransactionFragment : Fragment() {
    private var _binding: FragmentRecycleViewTransactionBinding? = null
    private val binding get() = _binding!!

    private var mode = UNKNOWN_MODE

    private lateinit var viewModelFactory:ViewModelFactoryWithMode
    private val viewModel:RecycleViewTransactionsViewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[RecycleViewTransactionsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseMode()
        viewModelFactory = ViewModelFactoryWithMode(
            requireActivity().application as MyApplication, mode
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

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }





    private fun observeState(
        binding: FragmentRecycleViewTransactionBinding,
        viewTransactionsViewModel: RecycleViewTransactionsViewModel
    ){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.state.collectLatest{

                }
            }
        }
    }

    private fun parseMode(){
      mode = RecycleViewTransactionFragmentArgs.fromBundle(requireArguments()).mode
    }


    companion object{
        const val TO_SHARPEN_MODE = 201
        const val DECOMMISSIONED_TOOLS_MODE = 202
        const val FROM_SHARPEN_MODE = 203
        private const val UNKNOWN_MODE = -1
    }
}