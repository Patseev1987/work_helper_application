package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentTransactionBinding
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.presenter.states.TransactionState
import ru.bogdan.patseev_diploma.presenter.viewModels.TransactionViewModel
import java.lang.RuntimeException

class TransactionFragment : Fragment() {
    private var _binding:FragmentTransactionBinding? = null
    private val binding get() = _binding !!
    private val viewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs(viewModel)
        setFragmentResultListener(REQUEST_KEY_TOOL) { requestKey, bundle ->
            val tool = bundle.getParcelable<Tool>(BUNDLE_KEY_TOOL)
           tool?.let{t ->
               viewModel.setTool(t)
           }
        }
        setFragmentResultListener(REQUEST_KEY_RECEIVER) {requestKey, bundle ->
            val receiver = bundle.getParcelable<Worker>(BUNDLE_KEY_RECEIVER)
            receiver?.let{
                viewModel.setReceiver(receiver)
            }
        }
        setFragmentResultListener(REQUEST_KEY_SENDER) { requestKey, bundle ->
            val sender = bundle.getParcelable<Worker>(BUNDLE_KEY_SENDER)
            sender?.let{
                viewModel.setSender(sender)
            }
        }

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
        binding.bCommitTransactionFragment.setOnClickListener{
            binding.inLayoutTransactionFragment.error = ""
            viewModel.doTransaction(
                try {
                    binding.inEditTextTransaction.text.toString().toInt()
                }catch (e:RuntimeException) {
                        binding.inLayoutTransactionFragment.error =
                            getString(R.string.enter_the_number)
                    return@setOnClickListener
                }
            )
        }
        binding.bSetTool.setOnClickListener{
            findNavController().navigate(R.id.action_transactionFragment_to_toolsFragmentForSearchFragment)
        }
        binding.bSetReceiver.setOnClickListener{
            val action = TransactionFragmentDirections
                .actionTransactionFragmentToRecycleViewWithWorkersFragment(
                    RecycleViewWithWorkersFragment.RECEIVER_MODE
                )
            findNavController().navigate(action)
        }
           binding.bSetSender.setOnClickListener{
               val action = TransactionFragmentDirections
                   .actionTransactionFragmentToRecycleViewWithWorkersFragment(
                       RecycleViewWithWorkersFragment.SENDER_MODE
                   )
               findNavController().navigate(action)
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

    companion object{
        const val REQUEST_KEY_SENDER = "sender"
        const val REQUEST_KEY_RECEIVER = "receiver"
        const val REQUEST_KEY_TOOL = "tool"
        const val BUNDLE_KEY_TOOL = "tool"
        const val BUNDLE_KEY_SENDER = "sender"
        const val BUNDLE_KEY_RECEIVER = "receiver"
    }

}