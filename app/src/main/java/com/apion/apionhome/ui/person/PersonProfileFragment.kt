package com.apion.apionhome.ui.person

import android.app.AlertDialog
import android.os.Build
import android.view.MenuInflater
import android.view.View
import android.view.WindowInsetsController
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apion.apionhome.MobileNavigationDirections
import com.apion.apionhome.MyApplication
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.data.model.House
import com.apion.apionhome.databinding.FragmentUserProfileBinding
import com.apion.apionhome.ui.adapter.UserHouseAdapter
import com.apion.apionhome.utils.*
import com.apion.apionhome.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.reflect.Method


class PersonProfileFragment :
    BindingFragment<FragmentUserProfileBinding>(FragmentUserProfileBinding::inflate) {

    override val viewModel by sharedViewModel<UserProfileViewModel>()
    private val args: PersonProfileFragmentArgs by navArgs()
    private val adapter = UserHouseAdapter(::onItemHouseClick)
    private val userProfile by lazy {
        args.userProfile
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.user = args.userProfile
        binding.recyclerViewMyhouse.adapter = adapter
        println("USER DETAIL HOUSE -> PROFILE: ${args.userProfile}")
        setupListener()
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

    fun showPopup(v: View) {
        val popup = PopupMenu(requireContext(), v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_messenger, popup.menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        } else {
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field[popup]
                        val classPopupHelper =
                            Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons: Method = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_phone -> {
                    requireContext().toPhone(args.userProfile.phone?:"")
                }
                R.id.item_messages -> {
                    requireContext().toMessage(args.userProfile.phone?:"")
                }
                R.id.item_messenger -> {
                    requireContext().toMessenger(args.userProfile.facebook_id?:"")
                }
                R.id.item_zalo -> {
                   requireContext().toZalo(args.userProfile.phone?:"")
                }
            }
            true
        }
        popup.show()
    }

    private fun setupListener() {
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imageButtonMore.setOnClickListener {
            showPopup(it)
        }
        binding.textViewMore.setOnClickListener {
//            val action = PersonProfileFragmentDirections.actionToMyHouses(args.userProfile)
//            findNavController().navigate(action)
        }
        binding.buttonFollow.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle("Yêu cầu đăng nhập!")
            dialog.setMessage("Vui lòng đăng nhập để sử dụng tính năng này!")
            dialog.setPositiveButton("Đăng nhập") { _, _ ->
                findNavController().navigate(MobileNavigationDirections.actionToLogin())
                MyApplication.tabToNavigate.value = TabApp.PROFILE_PERSON
                MyApplication.profileUserNavigate.value = userProfile

            }
            dialog.setNegativeButton(getString(R.string.tittle_button_cancel)) { dialogShow, _ ->
                MyApplication.tabToNavigate.value = null
                MyApplication.profileUserNavigate.value = null
                dialogShow.dismiss()
            }
            if (MyApplication.sessionUser.value != null) {
                val isFollow =
                    MyApplication.sessionUser.value!!.isFollowing(userProfile.id.toString())
                if (MyApplication.sessionUser.value!!.id == userProfile.id) {
                    findNavController().navigate(R.id.actionToAdd)
                } else if (isFollow) {
                    viewModel.unFollow(MyApplication.sessionUser.value?.id!!, userProfile?.id ?: -1)
                } else {
                    viewModel.follow(MyApplication.sessionUser.value?.id!!, userProfile?.id ?: -1)
                }
            } else {
                dialog.show()
            }
        }

    }

    private fun onItemHouseClick(house: House) {
        val action = PersonProfileFragmentDirections.actionProfileToDetail(house)
      findNavController().navigate(action)
    }
}
