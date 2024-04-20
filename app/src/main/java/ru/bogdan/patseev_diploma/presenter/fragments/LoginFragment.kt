package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentLoginBinding
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import ru.bogdan.patseev_diploma.presenter.states.LoginState
import ru.bogdan.patseev_diploma.presenter.viewModels.LoginViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactory


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ViewModelFactory(requireActivity().application as MyApplication)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoginState(binding, viewModel)
        binding.bSingIn.setOnClickListener {
            viewModel.checkLogin(
                binding.ietLogin.text.toString(),
                binding.ietPassword.text.toString()
            )
        }
    }

    private fun observeLoginState(binding: FragmentLoginBinding, viewModel: LoginViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    when (it) {
                        is LoginState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.ilLogin.error = "wrong login or password"
                            binding.ilPassword.error = "wrong login or password"
                        }

                        is LoginState.Waiting -> {
                            binding.progressBar.visibility = View.GONE
                        }

                        is LoginState.LoginResult -> {
                            binding.progressBar.visibility = View.GONE
                            when (it.worker.type) {
                                WorkerType.WORKER -> {
                                    val action = LoginFragmentDirections.actionLoginFragmentToWorkerFragment(it.worker)
                                    findNavController().navigate(action)
                                }

                                WorkerType.STORAGE_WORKER -> {
                                    val action =
                                        LoginFragmentDirections.actionLoginFragmentToStorageWorkerFragment(it.worker)
                                    findNavController().navigate(action)
                                }
                            }
                        }
                        is LoginState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
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