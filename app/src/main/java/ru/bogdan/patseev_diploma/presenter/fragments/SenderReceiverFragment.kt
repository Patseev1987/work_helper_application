package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentSenderReceiverBinding
import ru.bogdan.patseev_diploma.databinding.FragmentTabLayoutBinding

class SenderReceiverFragment : Fragment() {
    private var _binding:FragmentSenderReceiverBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
    _binding = FragmentSenderReceiverBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}