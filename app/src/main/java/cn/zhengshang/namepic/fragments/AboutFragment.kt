package cn.zhengshang.namepic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.navigation.fragment.findNavController
import cn.zhengshang.namepic.BaseFragment
import cn.zhengshang.namepic.R

/**
 * Created by shangzheng on 2020/7/28.
 *            🐳🐳🐳🍒           16:35 🥥
 */
class AboutFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.frag_about_me, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}