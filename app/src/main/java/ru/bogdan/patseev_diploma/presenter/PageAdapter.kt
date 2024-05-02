package ru.bogdan.patseev_diploma.presenter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewStorageRecordsFragment

class PageAdapter(private val worker: Worker, fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return RecycleViewStorageRecordsFragment.newInstance(position, worker)
    }
}