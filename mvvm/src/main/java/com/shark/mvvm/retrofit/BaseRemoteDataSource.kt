package com.shark.mvvm.retrofit

import com.shark.mvvm.exception.BaseException
import com.shark.mvvm.viewmodel.BaseViewModel
import com.shark.mvvm.retrofit.callback.RequestCallback
import com.shark.mvvm.retrofit.callback.RequestMultiplyCallback
import com.shark.mvvm.retrofit.model.RequestModel
import com.shark.mvvm.retrofit.subscriber.BaseRemoteSubscriber
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.disposables.Disposable

import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * 网络远程数据源 这里封装了获得Retrofit接口和执行的一系列方法
 * 子类就相当于数据源Repo调用时不用关心数据的获取方式
 * @property baseViewModel BaseViewModel
 * @property compositeDisposable CompositeDisposable
 * @constructor
 */
abstract class BaseRemoteDataSource(private val baseViewModel: BaseViewModel) {
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    /**
     * getService就是拿到Retrofit接口
     * @param clz Class<T>
     * @return T
     */
    open fun <T> getService(clz: Class<T>): T {
        return RetrofitManagement.getService(clz)
    }

    open fun <T> getService(clz: Class<T>, host: String): T {
        return RetrofitManagement.getService(clz, host)
    }

    /**
     * 实际调用请求
     * @return ObservableTransformer<RequestModel<T>, T>?
     */
    open fun <T> applySchedulers(): ObservableTransformer<RequestModel<T>, T>? {
        return RetrofitManagement.applySchedulers()
    }

    /**
     * 调用完网络接口后会获得Observable对象
     * 这里我们将baseViewModel 和 callback封装为BaseRemoteSubscriber订阅者
     * @param observable Observable<RequestModel<T>>
     * @param callback RequestCallback<T>?
     */
    protected open fun <T> execute(
        observable: Observable<RequestModel<T>>,
        callback: RequestCallback<T>?
    ) {
        execute(observable, BaseRemoteSubscriber(baseViewModel, callback), true)
    }

    /**
     * 表达式支持
     * @param observable Observable<RequestModel<T>>
     * @param callback Function1<[@kotlin.ParameterName] T, Unit>?
     */
    protected open fun <T> execute(
        observable: Observable<RequestModel<T>>,
        failCallback: ((e: BaseException?) -> Unit)? = null,
        successCallback: ((result: T) -> Unit)? = null
    ) {
        var callback: RequestCallback<T>? = null
        if (failCallback == null) {
            callback = object : RequestCallback<T> {
                override fun onSuccess(t: T) {
                    successCallback?.invoke(t)
                }
            }
        } else {
            callback = object : RequestMultiplyCallback<T> {
                override fun onSuccess(t: T) {
                    successCallback?.invoke(t)
                }

                override fun onFail(e: BaseException?) {
                    failCallback?.invoke(e)
                }
            }
        }


        execute(
            observable,
            BaseRemoteSubscriber(baseViewModel, callback),
            true
        )
    }

    protected open fun <T> execute(
        observable: Observable<RequestModel<T>>,
        callback: RequestMultiplyCallback<T>?
    ) {
        execute(observable, BaseRemoteSubscriber(baseViewModel, callback), true)
    }

    open fun <T> executeWithoutDismiss(
        observable: Observable<RequestModel<T>>,
        observer: Observer<T>
    ) {
        execute(observable, observer, false)
    }

    /**
     * 让获取数据的操作在IO线程上执行
     * 更新ui的操作在ui线程上执行
     * @param observable Observable<RequestModel<T>>
     * @param observer Observer<T>
     * @param isDismiss Boolean
     */
    open fun <T> execute(
        observable: Observable<RequestModel<T>>,
        observer: Observer<T>,
        isDismiss: Boolean
    ) {
        addDisposable(
            observable
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(applySchedulers())
                .compose(if (isDismiss) loadingTransformer<T>() else loadingTransformerWithoutDismiss<T>())
                .subscribeWith(observer) as Disposable
        )
    }

    open fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    open fun dispose() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    open fun startLoading() {
        baseViewModel.startLoading()
    }

    open fun dismissLoading() {
        baseViewModel.dismissLoading()
    }

    /**
     * 在订阅时加载 订阅后关闭
     * @return ObservableTransformer<T, T>?
     */
    open fun <T> loadingTransformer(): ObservableTransformer<T, T>? {
        return ObservableTransformer { observable: Observable<T> ->
            observable
                .subscribeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { disposable: Disposable? -> startLoading() }
                .doFinally { dismissLoading() }
        }
    }

    open fun <T> loadingTransformerWithoutDismiss(): ObservableTransformer<T, T>? {
        return ObservableTransformer { observable: Observable<T> ->
            observable
                .subscribeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { disposable: Disposable? -> startLoading() }
        }
    }
}