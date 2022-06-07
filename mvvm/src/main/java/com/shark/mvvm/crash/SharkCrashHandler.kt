package com.shark.mvvm.crash

import android.app.Application
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.util.Log
import android.widget.Toast
import com.shark.mvvm.model.AndroidCrash
import com.shark.mvvm.retrofit.RetrofitManagement
import com.shark.mvvm.retrofit.model.RequestModel
import com.shark.mvvm.service.api.AppService
import com.shark.mvvm.spread.TAG
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.*

object SharkCrashHandler : Thread.UncaughtExceptionHandler {
    lateinit var mContext: Application
    lateinit var url: String

    lateinit var appService: AppService

    //原来的处理handler
    lateinit var mDefaultHandler: Thread.UncaughtExceptionHandler

    fun init(mContext: Application, url: String) {
        this.mContext = mContext
        this.url = url
        appService = RetrofitManagement.getService(AppService::class.java, this.url)

        //处理主线程异常
        Handler(Looper.getMainLooper()).post {
            while (true) {
                try {
                    Looper.loop() //try-catch主线程的所有异常；Looper.loop()内部是一个死循环，出现异常时才会退出，所以这里使用while(true)。
                } catch (e: Throwable) {
                    Log.e(TAG, "Looper.loop(): ", e)
                    //TODO 此次修改为弹出框提示
                    Toast.makeText(
                        mContext,
                        "很抱歉，程序出现异常，为避免数据异常，请勿继续操作，联系开发人员处理相关问题",
                        Toast.LENGTH_LONG
                    ).show()

                    handleExample(e)
                }
            }
        }

        //处理子线程异常
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()!!
        //设置该MyCrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    override fun uncaughtException(t: Thread, e: Throwable) {
        Log.e("SharkChilli", "uncaughtException: ", e)
        if (!handleExample(e) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理 目的是判断异常是否已经被处理
            mDefaultHandler.uncaughtException(t, e)
        } else {
            try { //Sleep 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
                Thread.sleep(3000)
            } catch (e1: InterruptedException) {
                Log.e(TAG, "uncaughtException: ", e1)
            } catch (e2: Exception) {
                Log.e(TAG, "uncaughtException: ", e2)
            }
            /** 关闭App 与下面的restartApp重启App保留一个就行 看你需求  */
            // 如果不关闭程序,会导致程序无法启动,需要完全结束进程才能重新启动
            // android.os.Process.killProcess(android.os.Process.myPid());
            // System.exit(1);
//            restartApp();
        }

    }


    /**
     * 自定义错误处理,收集错误信息 将异常信息保存 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleExample(ex: Throwable?): Boolean {
        // 如果已经处理过这个Exception,则让系统处理器进行后续关闭处理
        if (ex == null) return false

        Thread {
            // Toast 显示需要出现在一个线程的消息队列中
            Looper.prepare()
            Toast.makeText(mContext, "很抱歉，子线程程序出现异常，为避免数据异常，请勿继续操作，联系开发人员处理相关问题", Toast.LENGTH_LONG)
                .show()
            Looper.loop()
        }.start()


        //将异常记录到本地的文件中
        val errorLogPath = saveCrashInfoToFile(ex)

        //发送请求
        val file = File(errorLogPath)
        val requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val requestModelCall: Observable<RequestModel<String>> =
            appService.errorFileUpload(body)

        //先调用文件上传接口 再 调用日志上传接口
        val d = requestModelCall
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { result ->
                Log.i(TAG, "handleExample1: $result")
                if (result.type == "200") {
                    Toast.makeText(mContext, "错误文件上传成功", Toast.LENGTH_LONG).show()
                }
            }
            .observeOn(Schedulers.io())
            .flatMap(object :
                Function<RequestModel<String>, ObservableSource<RequestModel<String>>> {
                override fun apply(result: RequestModel<String>): ObservableSource<RequestModel<String>>? {
                    if (result.type != "200") return Observable.empty()
                    return appService.addCrash(AndroidCrash().getDeviceCrash(mContext, result.msg))
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Log.i(TAG, "handleExample2: $result")
                if (result.type == "200") {
                    Toast.makeText(mContext, "错误日志上传成功", Toast.LENGTH_LONG).show()
                }
            }) { t -> Log.e(TAG, "accept: ", t) }

        return true
    }

    /**
     * 重启应用
     */
    fun restartApp() {
//        Intent intent = new Intent(AppApplication.getContext(), SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(1);

        // 重启应用
        mContext.startActivity(mContext.packageManager.getLaunchIntentForPackage(mContext.packageName))
        //干掉当前的程序
        Process.killProcess(Process.myPid())
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     */
    private fun saveCrashInfoToFile(ex: Throwable): String? {
        //获取错误原因
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var exCause = ex.cause
        while (exCause != null) {
            exCause.printStackTrace(printWriter)
            exCause = exCause.cause
        }
        printWriter.close()

        // 错误日志文件名称
        val fileName = "crash-" + Date(System.currentTimeMillis()).time + ".log"

        // 判断sd卡可正常使用
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            //文件存储位置
            Log.i(
                TAG,
                "saveCrashInfoToFile: " + Environment.getExternalStorageDirectory().path
            )
            val path = mContext.filesDir.toString() + "/crash_logInfo/"
            Log.i(TAG, "path: $path")
            val fl = File(path)
            //创建文件夹
            if (!fl.exists()) {
                fl.mkdirs()
            }
            try {
                val fileOutputStream = FileOutputStream(path + fileName)
                fileOutputStream.write(writer.toString().toByteArray())
                Log.i(TAG, "saveCrashInfoToFile.............: $writer")
                fileOutputStream.close()
                return path + fileName
            } catch (e1: IOException) {
                Log.e(TAG, "saveCrashInfoToFile: ", e1)
            } catch (e2: java.lang.Exception) {
                Log.e(TAG, "saveCrashInfoToFile: ", e2)
            }
            return null
        }
        return null
    }

}