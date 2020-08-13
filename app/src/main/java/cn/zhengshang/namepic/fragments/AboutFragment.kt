package cn.zhengshang.namepic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import cn.zhengshang.namepic.BaseFragment
import cn.zhengshang.namepic.R
import cn.zhengshang.namepic.tools.navClickBack

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
        view.findViewById<Toolbar>(R.id.toolbar).navClickBack()
    }
}