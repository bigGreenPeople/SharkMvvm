---
description: SharkMvvm网络请求使用了retrofit2 + rxjava，您不必处理每一个请求的错误，当请求失败时会自动交给上层架构处理
---

# Request

### 测试API接口

下面是我在本地搭建的一个Java编写的web项目，提供了一个测试使用的Api

{% swagger method="get" path="" baseUrl="http://localhost:8081/android/test/index" summary="本地测试接口" %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="query" name="type" required="true" type="0" %}
0 | 1 填0请求成功，1请求失败
{% endswagger-parameter %}

{% swagger-response status="200: OK" description="成功时返回的json type为200" %}
```javascript
{
    "type": 200,
    "msg": "",
    "data": {
        "id": 0,
        "username": "SharkChilli",
        "password": "123456"
    }
}
```
{% endswagger-response %}

{% swagger-response status="400: Bad Request" description="识别时返回的json type为400" %}
```javascript
{
    "type": 400,
    "msg": "类型错误",
    "data": null
}
```
{% endswagger-response %}
{% endswagger %}

### 创建Model类

根据成功方法的Json字符串，我们需要创建对应的Model类

{% code title="User.kt" %}
```kotlin
data class User(
    var id: Int? = null,
    var username: String? = null,
    var password: String? = null
)
```
{% endcode %}

最外部的Request Model已经帮定义好，直接使用即可

```kotlin
package com.shark.mvvm.retrofit.model

data class RequestModel<T>(
    override val type: String,
    override val msg: String,
    override val data: T
) : BaseRequestModel<T> {
}
```

默认情况下type返回200时SharkMvvm认为本次请求为成功，否则为失败.

### 指定Request公共信息

我们需要早Application中，指定公共url的信息。

```kotlin
 HttpConfig.BASE_URL_WEATHER = "http://192.168.1.123:8081/android/"
```

如果你和服务端定义的json type类型不一致，你可以手动修改成功的定义code

```
HttpCode.CODE_SUCCESS = "200"
```

### 定义retrofit接口

retrofit接口的定义没有什么不同的地方

```kotlin
/**
 * 测试相关的接口
 */
interface TestService {
    @GET("test/index")
    fun test(
        @Query("type") type: String
    ): Observable<RequestModel<User>>
}
```

### 初始化Service接口

初始化Service接口操作应该都在ViewModel中完成，这里我们依旧使用注解自动注入的方式

```kotlin
@Service
lateinit var service: TestService
```

### 调用api接口

```kotlin
fun test(type: String) {
    call(service.test(type)) {
        Log.i(TAG, "test: $it")
    }
}
```

{% hint style="info" %}
这里使用了call方法来调用接口方法的，这是BaseViewModel中提供的一个简化调用的操作。

当接口调用成功时则会进入到回调的方法中，打印出相关数据

接口调用失败时会自动弹出错误提示
{% endhint %}
