package cn.zhengshang.namepic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import cn.zhengshang.namepic.BaseFragment
import cn.zhengshang.namepic.R


/**
 * Created by shangzheng on 2020/6/22.
 *            🐳🐳🐳🍒           15:15 🥥
 */
class MainFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.frag_main, container, false)
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
