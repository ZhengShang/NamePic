package cn.zhengshang.namepic.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cn.zhengshang.namepic.BaseFragment
import cn.zhengshang.namepic.R
import cn.zhengshang.namepic.databinding.FragMainBinding
import cn.zhengshang.namepic.tools.getUri
import cn.zhengshang.namepic.tools.toBitmap
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.frag_main.*


/**
 * Created by shangzheng on 2020/6/22.
 *            🐳🐳🐳🍒           15:15 🥥
 */
class MainFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragMainBinding>(inflater,
                R.layout.frag_main, container, false
        )

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        initClicks()
    }

    private fun initClicks() {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.share -> {
                    shareImage()
                    true
                }
                R.id.about_me -> {
                    findNavController().navigate(R.id.action_mainFragment_to_aboutFragment)
                    true
                }
                R.id.feedback -> {
                    findNavController().navigate(R.id.action_mainFragment_to_feedbackFragment)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

        toolbar.setNavigationOnClickListener { drawer_layout.open() }

        fab.setOnClickListener {
            pic_view.toBitmap().getUri(context)
            Snackbar.make(fab, R.string.save_success, BaseTransientBottomBar.LENGTH_SHORT).show()
        }
    }

    private fun shareImage() {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM,
            pic_view.toBitmap().getUri(context))
            type = "image/jpeg"
        }
        startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.share)))
    }
}
