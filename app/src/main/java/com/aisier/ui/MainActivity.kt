package com.aisier.ui

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aisier.R
import com.aisier.architecture.base.BaseActivity
import com.aisier.architecture.util.startActivity
import com.aisier.architecture.util.toast
import com.aisier.databinding.ActivityMainBinding
import com.aisier.util.TimerShareLiveData
import com.aisier.vm.ShareViewModel

/**
 * 这种封装方式不支持Loading状态，需要自己手动书写Loading
 */
class MainActivity : BaseActivity(R.layout.activity_main) {

    private val mBinding by viewBinding(ActivityMainBinding::bind)

    private val activityResultLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult: ActivityResult ->
                if (activityResult.resultCode == Activity.RESULT_OK) {
                    toast(activityResult.data?.getStringExtra("key") ?: "")
                }
            }

    override fun init() {
        initData()
        initGlobalObserver()
    }

    private fun initGlobalObserver() {
        getAppViewModelProvider().get(ShareViewModel::class.java).msgLiveData.observe(this) {
            toast("我是第二个页面的消息")
        }

        TimerShareLiveData.get().observe(this) {
            Log.i(TAG, "TimerShareLiveData value: $it")
        }
    }

    private fun initData() {
        mBinding.goSecondActivity.setOnClickListener {
            activityResultLauncher.launch(Intent(this, SecondActivity::class.java))
        }

        mBinding.goSaveStateActivity.setOnClickListener { startActivity<SavedStateActivity>() }

        mBinding.btApiActivity.setOnClickListener { startActivity<ApiActivity>() }

    }

    companion object {
        private const val TAG = "MainActivity-->"
    }
}
