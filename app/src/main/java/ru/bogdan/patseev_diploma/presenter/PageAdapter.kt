package ru.bogdan.patseev_diploma.presenter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapter(fragment:FragmentActivity):FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int {
       return 3
    }

    override fun createFragment(position: Int): Fragment {
            return RecycleViewCuttingToolsFragment.newInstance(position)
    }
}