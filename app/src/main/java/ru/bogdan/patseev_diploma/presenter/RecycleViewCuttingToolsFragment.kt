package ru.bogdan.patseev_diploma.presenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.bogdan.m17_recyclerview.presentation.recycleView.CuttingToolsAdapter
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentRecycleViewCuttingToolsBinding
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import ru.bogdan.patseev_diploma.domain.models.tools.CuttingTool
import ru.bogdan.patseev_diploma.domain.models.tools.MeasureTool
import ru.bogdan.patseev_diploma.domain.models.tools.Place
import ru.bogdan.patseev_diploma.domain.models.tools.Tool
import ru.bogdan.patseev_diploma.presenter.viewModels.RecycleViewViewModel
import java.time.LocalDate


class RecycleViewCuttingToolsFragment : Fragment() {
    private var _binding: FragmentRecycleViewCuttingToolsBinding? = null
    private val binding get() = _binding!!

    private var position: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.let {
            it.getInt(POSITION)
        } ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecycleViewCuttingToolsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[RecycleViewViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CuttingToolsAdapter() { storageRecord ->
            val tool = storageRecord.tool
            val action =
                TabLayoutFragmentDirections.actionTabLayoutFragmentToToolFragment2(tool)
            findNavController().navigate(action)


        }
        adapter.submitList(viewModel.getList(position))
        binding.cuttingTools.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val POSITION = "position"

        @JvmStatic
        fun newInstance(position: Int) =
            RecycleViewCuttingToolsFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                }
            }
    }
}