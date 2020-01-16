package com.psob96.waismprojekt

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.provider.MediaStore
import java.io.File

class MainActivity : AppCompatActivity(), OptionsDialog.OptionsDialogCallback, EditTextDialog.EditTextDialogCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resetButton.setOnClickListener { drawingView.clear() }
        colorPickerButton.setOnClickListener { openOptionsDialog() }
        saveButton.setOnClickListener { showSaveDialog() }
        loadButton.setOnClickListener { pickFromGallery() }
    }

    private fun openOptionsDialog() {

        val currentColor = drawingView.getCurrentColor()
        val currentStrokeWidth = drawingView.getStrokeWidth()
        val fragment = OptionsDialog.create(currentColor, currentStrokeWidth)
        fragment.show(supportFragmentManager, OptionsDialog.TAG)
    }

    private fun pickFromGallery() {

        if (!hasStoragePermission()) {
            requestStoragePermission()
            return
        }

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE){

            val uri = data?.data

            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                drawingView.drawBitmap(bitmap)
            }
        }
    }

    private fun showSaveDialog() {

        if (!hasStoragePermission())
            requestStoragePermission()
        else
            EditTextDialog().show(supportFragmentManager, EditTextDialog::class.java.simpleName)
    }

    override fun onSaveClick(fileName: String): Boolean {

        val path = Environment.getExternalStorageDirectory().path.toString() + "/$fileName.png"

        if (File(path).exists()){
            toast("File already exists")
            return false
        }

        val success = drawingView.toBitmap().saveToFile(path)
        toast(if (success) "Saved at $path" else "An error occurred")

        return success
    }

    private fun hasStoragePermission(): Boolean {

        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        return checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onColorSelected(color: Int) {
        drawingView.setColor(color)
    }

    override fun onWidthSelected(width: Int) {
        drawingView.setWidth(width.toFloat())
    }

    private fun requestStoragePermission() {
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            when (requestCode){
                REQUEST_CODE_OPEN -> pickFromGallery()
                REQUEST_CODE_SAVE -> showSaveDialog()
            }
        }
    }

    companion object {

        const val REQUEST_CODE_SAVE = 1
        const val REQUEST_CODE_OPEN = 2
        const val GALLERY_REQUEST_CODE = 3
    }
}
