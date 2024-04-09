package ru.bogdan.patseev_diploma.presenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentToolBinding

class ToolFragment : Fragment() {
    private var _binding: FragmentToolBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToolBinding.inflate(inflater, container, false)
        binding.uuu.setOnClickListener {
            findNavController().navigate(R.id.action_toolFragment_to_tabLayoutFragment)
        }
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = RecycleViewCuttingToolsFragment()
    }
}