package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import ru.bogdan.patseev_diploma.databinding.FragmentRecycleViewCuttingToolsBinding
import ru.bogdan.patseev_diploma.databinding.FragmentToolBinding
import ru.bogdan.patseev_diploma.domain.models.Tool

class ToolFragment : Fragment() {
    private var _binding: FragmentToolBinding? = null
    private val binding get() = _binding!!

    private lateinit var tool:Tool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tool = ToolFragmentArgs.fromBundle(requireArguments()).tool
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToolBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(binding,tool)
    }

    private fun initView(binding: FragmentToolBinding, tool: Tool){
        Glide.with(this)
            .load(tool.icon)
            .into(binding.icon)

        binding.twCode.text = tool.code
        val newName = tool.name.replace(" ","\n")
        binding.twName.text = newName
        binding.twNote.text = tool.notes
        binding.twDescriptions.text = tool.description
        binding.twColumn.text = tool.place.column
        binding.twRow.text = tool.place.row
        binding.twShelf.text = tool.place.shelf
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}