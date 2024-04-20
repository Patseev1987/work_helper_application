package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.databinding.FragmentTabLayoutBinding
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.presenter.PageAdapter
import ru.bogdan.patseev_diploma.presenter.viewModels.TabLayoutVieModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactoryWithApplication


class TabLayoutFragment : Fragment() {
    private var _binding: FragmentTabLayoutBinding? = null
    private val binding get() = _binding!!

    private val worker: Worker by lazy {
        TabLayoutFragmentArgs.fromBundle(requireArguments()).worker
    }

    private lateinit var viewModelFactory:ViewModelFactoryWithApplication

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[TabLayoutVieModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ViewModelFactoryWithApplication(requireActivity().application as MyApplication)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PageAdapter(worker,this.requireActivity())
        binding.viewPager.adapter = adapter

        binding.apply {
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = viewModel.tabNames[position]
            }.attach()
        }
    }


}