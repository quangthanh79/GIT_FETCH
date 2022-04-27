package com.apion.apionhome.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.apion.apionhome.R
import com.apion.apionhome.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BindingFragmentBottomSheet<T : ViewBinding>
    (private val inflate: Inflate<T>) : BottomSheetDialogFragment() {

    abstract val viewModel: RxViewModel

    private var _binding: T? = null

    protected val binding: T
        get() = _binding ?: throw IllegalStateException(EXCEPTION)

    open var isBackgroundTrans = false

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            onPermissionResult(permissions)
        }

    private val requestActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(result)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        binding.root.apply {
            backgroundTintMode = PorterDuff.Mode.CLEAR
            backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            setBackgroundColor(Color.TRANSPARENT)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = binding.root
            bottomSheet.let { sheet ->
                dialog.behavior.peekHeight = sheet.height
                sheet.parent.requestLayout()
            }
        }
        viewModel.errorException.observe(viewLifecycleOwner) {
            showToast(getString(R.string.default_error))
        }
        setupView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    open fun onPermissionResult(permissions: MutableMap<String, Boolean>) {
        permissions.entries.forEach {
            Log.e("DEBUG", "${it.key} = ${it.value}")
        }
    }

    open fun onActivityResult(result: ActivityResult) {
        Log.e("DEBUG", "${result.resultCode} = ${result.data}")
    }

    fun startActivityForResultSafely(intent: Intent) {
        requestActivityForResult.launch(intent)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: List<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.filter {
                !hasPermission(it)
            }.toTypedArray().apply {
                requestMultiplePermissions.launch(this)
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun showToast(msg: String) = requireContext().showToast(msg)

    fun showDialog(
        tittle: String,
        content: String,
        onPositive: (DialogInterface) -> Unit,
        onNegative: (DialogInterface) -> Unit,
    ) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(tittle)
        dialog.setMessage(content)
        dialog.setPositiveButton(getString(R.string.tittle_button_agree)) { dialogShow, _ ->
            onPositive(dialogShow)
        }
        dialog.setNegativeButton(getString(R.string.tittle_button_cancel)) { dialogShow, _ ->
            onNegative(dialogShow)
        }
        dialog.show()
    }

    fun showCancelDialog(
        tittle: String?,
        content: String,
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(tittle)
            .setMessage(content)
            .setNegativeButton(getString(R.string.tittle_button_cancel)) { dialogShow, _ ->
                dialogShow.dismiss()
            }
            .show()
    }

    abstract fun setupView()

    companion object {
        private const val EXCEPTION = "Binding only is valid after onCreateView"
    }
}
