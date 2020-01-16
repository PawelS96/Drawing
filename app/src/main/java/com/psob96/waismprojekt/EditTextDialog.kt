package com.psob96.waismprojekt

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_edit_text.view.*

class EditTextDialog : DialogFragment() {

    private var callback: EditTextDialogCallback? = null
    private var editText: EditText? = null

    interface EditTextDialogCallback {
        fun onSaveClick(fileName: String): Boolean
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is EditTextDialogCallback)
            callback = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogBuilder = AlertDialog.Builder(context!!)

        val customView = View.inflate(context!!, R.layout.dialog_edit_text, null)
        dialogBuilder.setView(customView)

        editText = customView.editText

        dialogBuilder.setTitle("Enter filename")
        dialogBuilder.setPositiveButton("Save", null)
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        val nameDialog = dialogBuilder.create()

        nameDialog.setOnShowListener {

            val b = nameDialog.getButton(AlertDialog.BUTTON_POSITIVE)

            b.setOnClickListener {

                val input = editText!!.text.toString().trim()

                if (input.isEmpty()) {
                    activity?.toast("Name cannot be empty")
                    return@setOnClickListener
                }

                if (callback?.onSaveClick(input) == true)
                    dismiss()
            }
        }

        editText?.requestFocus()
        nameDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        return nameDialog
    }

}