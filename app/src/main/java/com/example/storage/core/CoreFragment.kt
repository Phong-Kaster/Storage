package com.example.jetpack.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import com.example.jetpack.util.AppUtil
import com.example.storage.StorageApplication
import com.example.storage.ui.theme.StorageTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


val LocalLocale = staticCompositionLocalOf { Locale.getDefault() }
val LocalNavController = staticCompositionLocalOf<NavController?> { null }
val LocalImageLoader = staticCompositionLocalOf<ImageLoader?>{ null }


@AndroidEntryPoint
open class CoreFragment : Fragment(), CoreBehavior {

    protected val TAG = this.javaClass.name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        makeStatusBarTransparent()
        return ComposeView(requireActivity()).apply {
            setContent {
                CompositionLocalProvider(
                    LocalNavController provides findNavController(),
                    LocalLocale provides requireActivity().resources.configuration.locales[0],
                    LocalImageLoader provides (LocalContext.current.applicationContext as StorageApplication).newImageLoader(),
                    *compositionLocalProvider().toTypedArray()
                ) {
                    StorageTheme {
                        ComposeView()
                    }
                }
            }
        }
    }

    @Composable
    protected open fun compositionLocalProvider(): List<ProvidedValue<*>> {
        return listOf()
    }

    @Composable
    open fun ComposeView() {}

    override fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun isInternetConnected(): Boolean {
        return AppUtil.isInternetConnected(context = requireContext())
    }

    override fun hideNavigationBar() {
        AppUtil.hideNavigationBar(window = requireActivity().window)
    }

    override fun trackEvent(name: String) {}

    override fun showLoading() {}
    override fun makeStatusBarTransparent() {
        /*with(requireActivity().window) {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }*/
    }
}