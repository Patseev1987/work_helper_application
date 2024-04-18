package ru.bogdan.patseev_diploma.presenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.bogdan.m17_recyclerview.presentation.recycleView.CuttingToolsAdapter
import ru.bogdan.patseev_diploma.databinding.FragmentRecycleViewToolsBinding
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import ru.bogdan.patseev_diploma.domain.models.Place
import ru.bogdan.patseev_diploma.domain.models.Tool
import java.time.LocalDate


class RecycleViewToolsFragment : Fragment() {
    private var _binding: FragmentRecycleViewToolsBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecycleViewToolsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    val adapter = CuttingToolsAdapter()
       // adapter.submitList()
        binding.tools.adapter = adapter
    }

}