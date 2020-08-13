package cn.zhengshang.namepic.fragments

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cn.zhengshang.namepic.BaseFragment
import cn.zhengshang.namepic.R
import cn.zhengshang.namepic.databinding.FragMainBinding
import kotlinx.android.synthetic.main.app_bar_main.*


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
        ).apply {
            toolbar.setOnMenuItemClickListener {
                println("click menu : ${it.title}")
                true
            }
        }
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share ->{
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

    private fun shareImage() {
        // TODO: 2020/8/13 share
    }
}
