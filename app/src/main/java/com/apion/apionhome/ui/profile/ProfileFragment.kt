package com.apion.apionhome.ui.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.result.ActivityResult
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.MobileNavigationDirections
import com.apion.apionhome.MyApplication
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.databinding.FragmentProfileBinding
import com.apion.apionhome.ui.MainActivity
import com.apion.apionhome.ui.SplashActivity
import com.apion.apionhome.ui.geting_started.StartActivity
import com.apion.apionhome.utils.*
import com.apion.apionhome.viewmodel.ProfileViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BindingFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    override val viewModel by viewModel<ProfileViewModel>()

    override fun setupView() {
        binding.lifecycleOwner = this
//        binding.profileVM = viewModel
        binding.user = MyApplication.sessionUser.value
        listener()
        MyApplication.sessionUser.observe(this) {
            binding.user = it
        }
        viewModel.isLogout.observe(this) {
            if (it) {
                viewModel.updatePhoneNearst(null)
                requireActivity().finish()
                val intent = Intent(requireActivity(), StartActivity::class.java)
                intent.putExtra("destination","MAIN")
                startActivity(intent)
//                findNavController().popBackStack(R.id.navigation_home, false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val view = requireActivity().window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            requireActivity().window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
            view.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            view.systemUiVisibility =
                view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun onStop() {
        super.onStop()
        val view = requireActivity().window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.windowInsetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            var flags = view.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            view.systemUiVisibility = flags
        }
    }

    fun listener() {
        binding.btnInvite.setOnClickListener {
            requireContext().shareApp()
        }
        binding.btnFanpage.setOnClickListener {
            requireContext().toFanpage("109001047169523")
        }
        binding.btnComunity.setOnClickListener {
            requireContext().toFanpage("109001047169523")
        }
        binding.btnYoutube.setOnClickListener {
            requireContext().toYoutube("saonhapngu-qpvn")
        }
        binding.btnHotline.setOnClickListener {
            requireContext().toPhone(" 1900 29 29 91")
        }
//        binding.btnChat.setOnClickListener {
//            requireContext().toMessage(" 1900 29 29 91")
//        }
        binding.btnLevel.setOnClickListener {
            findNavController().navigate(R.id.actionToLevel)
        }
        binding.circleImageView.setOnClickListener {
            pickImage()
        }
        binding.btnToWeb1.setOnClickListener {
            requireContext().toWeb("http://www.apionhome.com/")
        }
        binding.btnToWeb2.setOnClickListener {
            requireContext().toWeb("http://www.apionhome.com/")
        }
        binding.btnToWeb3.setOnClickListener {
            requireContext().toWeb("http://www.apionhome.com/")
        }
        binding.btnUpdateProfile.setOnClickListener {
            findNavController().navigate(R.id.actionToUpdateProfile)
        }

        binding.btnBoss.setOnClickListener {
            MyApplication.sessionUser.value?.boss?.let {
                viewModel.getUserById(it.id ?: -1) {
                    val action = MobileNavigationDirections.actionToPersonProfile(it)
                    findNavController().navigate(action)
                }
            } ?: showToast("Không tìm thấy cấp trên!")
        }

        binding.btnLogout.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle("Thông báo")
            dialog.setMessage("Bạn có muốn đăng xuất?")
            dialog.setPositiveButton("Đăng xuất") { _ , _ ->
                MyApplication.sessionUser.value?.id?.let {
                    viewModel.logout(it, MyApplication.sessionUser.value?.phone ?: "")
                }
            }
            dialog.setNegativeButton("Đóng"){ _ , _ ->
            }

            dialog.show()
        }

        binding.btnHouseCreate.setOnClickListener {
            MyApplication.sessionUser.value?.let {
                val action = MobileNavigationDirections.actionMainToMyHouses(it)
                findNavController().navigate(action)
            }
        }

        binding.btnHouseSold.setOnClickListener {
            MyApplication.sessionUser.value?.let {
                val action = MobileNavigationDirections.actionMainToMyHousesSold(it)
                findNavController().navigate(action)
            }
        }
        binding.btnHouseBookmark.setOnClickListener {
            MyApplication.sessionUser.value?.let {
                val action = MobileNavigationDirections.actionMainToMyHousesBookmark(it)
                findNavController().navigate(action)
            }
        }
        binding.btnChangePincode.setOnClickListener {
            MyApplication.sessionUser.value?.let {
                val action = MobileNavigationDirections.actionToChangePincode(it.id ?: -1)
                findNavController().navigate(action)
            }
        }
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }

    private fun onImagesSelect(path: String) {
        MyApplication.sessionUser.value?.id?.let {
            viewModel.uploadAvatar(it, path)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri = data?.data!!
//            val path = uri.getRealPath(requireContext())
            val path = FileUtils.getReadablePathFromUri(requireContext(), uri)

            onImagesSelect(path ?: "")
        }
    }
}