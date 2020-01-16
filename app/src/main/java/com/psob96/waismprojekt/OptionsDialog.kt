package com.psob96.waismprojekt

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_options.view.*

class OptionsDialog private constructor() : DialogFragment() {

    private var color: Int = 0
    private var width: Int = 1

    private var callback: OptionsDialogCallback? = null

    interface OptionsDialogCallback {
        fun onColorSelected(color: Int)
        fun onWidthSelected(width: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OptionsDialogCallback)
            callback = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        color = arguments?.getInt(ARG_COLOR)!!
        width = arguments?.getInt(ARG_WIDTH)!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = View.inflate(context, R.layout.dialog_options, null)

        view.colorPicker.callback = object : ColorPickerView.OnColorChangedListener {
            override fun onColorChanged(color: Int) {
                view.selectedColorView.setBackgroundColor(color)
                this@OptionsDialog.color = color
            }
        }

        view.selectedColorView.setBackgroundColor(color)

        view.widthSeekBar.progress = width - 1

        view.widthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                width = Math.min(progress + 1, 100)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        return AlertDialog.Builder(context)
            .setView(view)
            .setPositiveButton("OK", null)
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        callback?.onColorSelected(color)
        callback?.onWidthSelected(width)
    }

    companion object {

        val TAG: String = OptionsDialog::class.java.simpleName

        const val ARG_COLOR = "color"
        const val ARG_WIDTH = "width"

        fun create(currentColor: Int, strokeWidth: Int): OptionsDialog {
            val dialog = OptionsDialog()
            dialog.arguments = Bundle().apply {
                putInt(ARG_COLOR, currentColor)
                putInt(ARG_WIDTH, strokeWidth)
            }
            return dialog
        }
    }
}