package ru.bogdan.patseev_diploma.presenter.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentTransactionBinding
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.presenter.states.TransactionState
import ru.bogdan.patseev_diploma.presenter.viewModels.TransactionViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactory
import java.lang.RuntimeException
import javax.inject.Inject

class TransactionFragment : Fragment() {
    private var _binding:FragmentTransactionBinding? = null
    private val binding get() = _binding !!
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[TransactionViewModel::class.java]
    }
    private val component by lazy {
        (this.activity?.application as MyApplication).component
    }
    @Suppress("DEPRECATION")
    // min SDK lower than 33 (TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        parseArgs(viewModel)
        setFragmentResultListener(REQUEST_KEY_TOOL) { _, bundle ->
            val tool = bundle.getParcelable<Tool>(BUNDLE_KEY_TOOL)
           tool?.let{t ->
               viewModel.setTool(t)
           }
        }
        setFragmentResultListener(REQUEST_KEY_RECEIVER) {_, bundle ->
            val receiver = bundle.getParcelable<Worker>(BUNDLE_KEY_RECEIVER)
            receiver?.let{
                viewModel.setReceiver(receiver)
            }
        }
        setFragmentResultListener(REQUEST_KEY_SENDER) { _, bundle ->
            val sender = bundle.getParcelable<Worker>(BUNDLE_KEY_SENDER)
            sender?.let{
                viewModel.setSender(sender)
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
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
        setOnClickListeners(binding,viewModel)
    }


    private fun setOnClickListeners(binding: FragmentTransactionBinding, viewModel: TransactionViewModel){
        binding.bCommitTransactionFragment.setOnClickListener{
            binding.inLayoutTransactionFragment.error = WITH_OUT_ERROR
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
                            Toast.makeText(
                                this@TransactionFragment.context,
                                it.errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBarTransactionFragment.visibility = View.GONE
                        }
                        is TransactionState.Waiting ->{
                            binding.progressBarTransactionFragment.visibility = View.GONE
                        }
                        is TransactionState.Result -> {
                            Toast.makeText(
                                this@TransactionFragment.context,
                                "${it.sender.secondName} send ${it.amount} ${it.tool.code}" +
                                        " to ${it.receiver.secondName}.",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBarTransactionFragment.visibility = View.GONE
                                delay(1000)
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                        is TransactionState.ReceiverState -> {
                            binding.twReceiverTransactionFragment.text = it.receiver.secondName
                            binding.progressBarTransactionFragment.visibility = View.GONE
                            binding.bSetReceiver.isEnabled = false
                        }
                        is TransactionState.SenderState -> {
                            binding.twSenderTransactionFragment.text = it.sender.secondName
                            binding.progressBarTransactionFragment.visibility = View.GONE
                            binding.bSetSender.isEnabled = false
                        }
                        is TransactionState.ToolState -> {
                            binding.twToolCodeTransactionFragment.text = it.tool.code
                            Glide.with(this@TransactionFragment)
                                .load(it.tool.icon)
                                .circleCrop()
                                .into(binding.iwIconTransactionFragment)
                            binding.progressBarTransactionFragment.visibility = View.GONE
                            binding.bSetTool.isEnabled = false
                        }
                        is TransactionState.Loading ->{
                            binding.progressBarTransactionFragment.visibility = View.VISIBLE
                        }
                        is TransactionState.ConnectionProblem -> {
                            binding.progressBarTransactionFragment.visibility = View.GONE
                            Toast.makeText(
                                this@TransactionFragment.context,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
    private fun parseArgs(viewModel: TransactionViewModel){
            TransactionFragmentArgs.fromBundle(requireArguments()).apply{
                receiver?.let {receiver ->
                    viewModel.setReceiver(receiver)

                }
                sender?.let{sender ->
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
        private const val WITH_OUT_ERROR = ""
    }

}