package ru.bogdan.patseev_diploma.presenter

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.bogdan.patseev_diploma.R


class DialogForQRCode: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(resources.getString(R.string.title_for_dialog_qr_code))
                .setMessage(getString(R.string.you_should_choose_the_action))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.take_tool_back)) { _, _ ->

                }
                .setNegativeButton(
                    getString(R.string.give_tool_to_worker)
                ) { _, _ ->

                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}