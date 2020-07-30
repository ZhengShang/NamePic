package cn.zhengshang.namepic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import cn.zhengshang.namepic.BaseFragment
import cn.zhengshang.namepic.R
import cn.zhengshang.namepic.databinding.FragMainBinding


/**
 * Created by shangzheng on 2020/6/22.
 *            🐳🐳🐳🍒           15:15 🥥
 */
class MainFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        val inflate = DataBindingUtil.inflate<FragMainBinding>(inflater, R.layout.frag_main, container, false)
        return inflate.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}
