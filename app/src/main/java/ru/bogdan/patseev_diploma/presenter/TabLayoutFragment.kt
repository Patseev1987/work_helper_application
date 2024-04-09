package ru.bogdan.patseev_diploma.presenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentTabLayoutBinding


class TabLayoutFragment : Fragment() {
    private var _binding: FragmentTabLayoutBinding? = null
    private val binding get() = _binding!!

    private val fragment = RecycleViewCuttingToolsFragment.newInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PageAdapter(this.requireActivity())
        binding.viewPager.adapter = adapter

        binding.apply {
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = "Tab"
            }.attach()
        }

    }

    override fun onResume() {
        super.onResume()
    }

}