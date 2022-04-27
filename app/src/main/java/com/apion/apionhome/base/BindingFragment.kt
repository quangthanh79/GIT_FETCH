package com.apion.apionhome.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.viewbinding.ViewBinding
import com.apion.apionhome.R
import com.apion.apionhome.utils.createProgressDialog
import com.apion.apionhome.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BindingFragment<T : ViewBinding>(private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> T) :
    Fragment() {

    abstract val viewModel: RxViewModel

    private var _binding: T? = null

    protected val binding: T
        get() = _binding ?: throw IllegalStateException(EXCEPTION)

    private var exceptionDialog: AlertDialog? = null

    val dialog by lazy {
        requireContext().createProgressDialog()
    }

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
        hideKeyboard(requireActivity())
        return binding.root
    }
    fun setKeyboard(){
        binding.root.setOnClickListener {

        }
    }
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = inflate.invoke(inflater, container, false)
//        return binding.root
//    }


    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            onConnection()
        }

        override fun onLost(network: Network) {
            onDisConnection()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.errorException.observe(viewLifecycleOwner) {
            if (it != null) {
                showToast(getString(R.string.default_error))
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) dialog.show() else dialog.dismiss()
        }
        setupView()
        try {
            val builder = NetworkRequest.Builder()
            (requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .registerNetworkCallback(
                    builder.build(),
                    networkCallback
                )
        } catch (e: Exception) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        (requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .unregisterNetworkCallback(networkCallback)
    }

    private fun onConnection() {
//        isInternetAvailable()
        onConnectionAvailable()
        exceptionDialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }

    private fun isInternetAvailable() {
        runBlocking {
            launch(Dispatchers.IO) {
                val sock = Socket()
                try {
                    val socketAddress = InetSocketAddress("8.8.8.8", 53)
                    sock.connect(socketAddress, 2000) // This will block no more than timeoutMs
                    launch(Dispatchers.Main) {
                        onConnectionAvailable()
                    }
                } catch (e: IOException) {
                    onDisConnection()
                } finally {
                    sock.close()
                }
            }
        }
    }

    open fun onDisConnection() {
        requireActivity().runOnUiThread {
            println("connection fail")
            if (exceptionDialog == null) {
                exceptionDialog = AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.tittle_exception_connect))
                    .setMessage(getString(R.string.message_exception_connect))
                    .setNegativeButton(getString(R.string.tittle_close)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .create()
            }
            println("current thread ${Thread.currentThread().name}")
            exceptionDialog?.let {
                println("show dialog")
                it.show()
            }
        }
    }

    open fun onPermissionResult(permissions: MutableMap<String, Boolean>) {
        permissions.entries.forEach {
            Log.e("DEBUG", "${it.key} = ${it.value}")
        }
    }

    open fun onActivityResult(result: ActivityResult) {
        Log.d("DEBUG", "${result.resultCode} = ${result.data}")
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
        onNegative: (DialogInterface) -> Unit
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
    fun updateShare(){

    }
    fun showDialog2(
        tittle: String,
        content: String,

    ) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(tittle)
        dialog.setMessage(content)
        dialog.setPositiveButton(getString(R.string.tittle_button_agree)) { _ , _ ->
        }

        dialog.show()
    }
    open fun hideKeyboard(activity: Activity) {
        val inputManager = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus:
        val currentFocusedView = activity.currentFocus
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun showDialogCustom(
        tittle: String?,
        content: String?,
        onPositive: ((DialogInterface) -> Unit)?,
        onNegative: (DialogInterface) -> Unit
    ) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(tittle)
        dialog.setMessage(content)

        onPositive?.let {
            dialog.setPositiveButton(getString(R.string.tittle_button_agree)) { dialogShow, _ ->
                it(dialogShow)
            }
        }

        dialog.setNegativeButton(getString(R.string.tittle_button_cancel)) { dialogShow, _ ->
            dialogShow.dismiss()
            onNegative(dialogShow)
        }
        dialog.show()
    }

    abstract fun setupView()

    open fun onConnectionAvailable() {}

    companion object {
        const val EXCEPTION = "Binding only is valid after onCreateView"
    }
}
