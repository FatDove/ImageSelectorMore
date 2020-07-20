# ImageSelectorMore
图片+视频 选择器+滑动预览，可单选（裁剪），多选 ，原图大小 ，标注功能



### 效果演示

![8SGItU.gif](https://s2.ax1x.com/2020/03/09/8SGItU.gif)

### 接入方式

>  在 progect 的 gect build.gradle  

```css
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

> 在module 的  build.gradle 

```css
  dependencies { 
     implementation 'com.github.fan21024158:ImageSelectorMore:1.1.0'
  }
```

> 相关权限

```xml
    <!--网络权限-->
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <!-- 文件写的权限 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <!--文件读权限-->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

> 示例代码 （调用）

```java
ImageSelector.builder()
        .useCamera(true) // 设置是否使用拍照
        //.setCrop(true)  // 设置是否使用图片剪切功能。
        //.setSingle(false)  //设置是否单选
  			//.isTagging(false)//是否需要标注
        .onlyImage(true)  //只要图片（不要视频）
        .isTagging(false)  // 使用标注
  			.setMaxSelectCount(9)
        .setViewImage(true) //是否点击放大图片查看,，默认为true
        .start(MainActivity.this, requestCode); // 打开相册   requestCode 返回时的标示
```

> 示例代码 （返回值接收）

```java
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    userid = sharedPreferences.getString(Constant.USER_ID, Constant.USER_DEFULT);
    switch (requestCode) {
        case REQUEST_CODE:
            if (data != null) {
                ArrayList<String> images = 			data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                // images 选中的图片（视频）集合
                String path = images.get(0);
                boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
                if (images == null || images.size() <= 0) {
                    Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                }
                dealwithPhoto(path);
            }
            break;
        case Constant.ALTERSHOWNAME_ACTIVITY:
            if (data != null) {
                boolean tag = data.getBooleanExtra("tag", false);
                if (tag) {
                    tvUserInfoNickname.setText(data.getStringExtra("showName"));//设置昵称
                    showName = data.getStringExtra("showName");
                    isAlterName = true;
                }
            }
            break;
    }
    super.onActivityResult(requestCode, resultCode, data);
}
```



