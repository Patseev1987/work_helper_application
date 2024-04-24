package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentTransactionBinding
import ru.bogdan.patseev_diploma.presenter.states.TransactionState
import ru.bogdan.patseev_diploma.presenter.viewModels.TransactionViewModel

class TransactionFragment : Fragment() {
    private var _binding:FragmentTransactionBinding? = null
    private val binding get() = _binding !!
    private val viewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
    _binding = FragmentTransactionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState(binding,viewModel)
        viewModel.test()
        binding.bCommitTransactionFragment.setOnClickListener{
            viewModel.doTransaction(
                binding.inEditTextTransaction.text.toString().toInt()
            )
        }

        binding.bSetTool.setOnClickListener{
            findNavController().navigate(R.id.action_transactionFragment_to_toolsFragmentForSearchFragment)
        }
    }


    private fun observeState(binding: FragmentTransactionBinding, viewModel: TransactionViewModel){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.state.collect{
                    when (it) {
                        is TransactionState.Error -> {
                            Log.d("Transaction","ErrorState")
                            Toast.makeText(
                                this@TransactionFragment.context,
                                it.errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBarTransactionFragment.visibility = View.GONE
                        }
                        is TransactionState.Waiting ->{
                            Log.d("Transaction","WaitingState")
                            binding.progressBarTransactionFragment.visibility = View.GONE
                        }
                        is TransactionState.Result -> {
                            Log.d("Transaction","ResultState")
                            Toast.makeText(
                                this@TransactionFragment.context,
                                "${it.sender.secondName} send ${it.amount} ${it.tool.code}" +
                                        " to ${it.receiver.secondName}.",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBarTransactionFragment.visibility = View.GONE
                        }
                        is TransactionState.ReceiverState -> {
                            Log.d("Transaction","ReceiverState")
                            binding.twReceiverTransactionFragment.text = it.receiver.secondName
                            binding.progressBarTransactionFragment.visibility = View.GONE
                        }
                        is TransactionState.SenderState -> {
                            Log.d("Transaction","SenderState")
                            binding.twSenderTransactionFragment.text = it.sender.secondName
                            binding.progressBarTransactionFragment.visibility = View.GONE
                        }

                        is TransactionState.ToolState -> {
                            Log.d("Transaction","ToolState")
                            binding.twToolCodeTransactionFragment.text = it.tool.code

                            Glide.with(this@TransactionFragment)
                                .load(it.tool.icon)
                                .circleCrop()
                                .into(binding.iwIconTransactionFragment)
                            binding.progressBarTransactionFragment.visibility = View.GONE
                        }
                        is TransactionState.Loading ->{
                            Log.d("Transaction","Loading")
                            binding.progressBarTransactionFragment.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
    private fun parseArgs(viewModel: TransactionViewModel){
            TransactionFragmentArgs.fromBundle(requireArguments()).apply{
                receiver?.let {receiver ->
                    Log.d("Transaction","receiver - > $receiver")
                    viewModel.setReceiver(receiver)
                }
                sender?.let{sender ->
                    Log.d("Transaction","sender - > $sender")
                    viewModel.setSender(sender)
                }
                tool?.let { tool ->
                    viewModel.setTool(tool)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}