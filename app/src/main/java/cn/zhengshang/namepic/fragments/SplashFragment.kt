package cn.zhengshang.namepic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import cn.zhengshang.namepic.BaseFragment
import cn.zhengshang.namepic.R

/**
 * Created by shangzheng on 2020/6/22.
 *            🐳🐳🐳🍒           14:39 🥥
 */
class SplashFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.frag_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.postDelayed({ navMain() }, 500)
    }

    private fun navMain() {
        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
    }
}