# 简介
- 使用纯 Kotlin 和 Androidx 搭建。
- 上手特别容易，不会 MVVM 开发模式的都可以快速上手。
- 基于 Android Architecture Components(AAC)。

## 优点

- 简单、浅封装、扩展性强
 
    代码简单，没有过度的封装，上手非常容易，Lib 只有十几个类。
    
    除了 Android 原生的依赖以外，
    只集成总线 `live-event-bus`，页面状态管理 `loadsir`，
    状态栏沉浸式工具 `immersionbar`，并且都是可以插拔的，不需要替换即可。
    
    图片加载库，网络请求库等其他第三方 Lib 可根据个人兴趣自己选择集成即可。
    

- 快速开发

    通过泛型可以减少很多不必要的重复代码，以前每个 Activity 都需要写如下重复代码，例如:
    ```
    ActivityMainBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    
    MainVm mainVm = ViewModelProviders.of(this).get(MainVm.class);
    ```
    现在只需要在类名上面写上泛型即可，自动解析：
    ```
    public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding> {}
    ```
    
- 自动页面状态管理

    日常开发 Activity 的页面状态有：有网、无网络、空页面、loading加载页面等各种页面。
    
    使用 LiveData 的监听集成在 BaseActivity 中，根据 BaseResult 返回的 code 来自动管理页面显示状态。
    ```
    private fun initPageStates() {
        mViewModel.stateActionEvent.observe(this, Observer { stateActionState ->
            when (stateActionState) {
                LoadState -> showLoading()
                EmptyState -> loadService.showCallback(EmptyCallback::class.java)
                SuccessState -> {
                    dismissLoading()
                    loadService.showSuccess()
                }
                is ToastState -> ToastUtils.showShort(stateActionState.message)
                is ErrorState -> {
                    dismissLoading()
                    stateActionState.message?.apply {
                        ToastUtils.showShort(this)
                        handleError()
                    }
                }
            }
        })
    }
    ```
    
    如果想手动改变页面状态，只需在 ViewModel 中一行代码，例如：
    Loading状态：
    ```
    stateActionEvent.postValue(LoadState)
    ```
    
    无网络时：
    ```
    stateActionEvent.postValue(ErrorState("没有网络"))
    
    ```
    
    数据为空时：
    ```
    stateActionEvent.postValue(EmptyState)
    ```
 
## 具体使用
见 demo 中的 MainActivity。   

## END
大家有什么更好的建议请提出，一起学习进步。

既然来了，麻烦动动手指，点个star，非常感谢。
 
## License

Copyright 2019 Wutao

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.



