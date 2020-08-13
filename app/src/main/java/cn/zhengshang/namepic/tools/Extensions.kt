package cn.zhengshang.namepic.tools

import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController

/**
 * Created by shang on 2020/8/13.
 *            🐳🐳🐳🍒           17:34 🥥
 */
fun Toolbar.navClickBack() {
    this.setNavigationOnClickListener {
        findNavController().popBackStack()
    }
}