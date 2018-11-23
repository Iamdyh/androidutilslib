# AndroidUtils
## Android工具类
##使用方法  
在root build.gradle下添加maven 
```
allprojects{
    repositores{
        ...
        maven{url 'https://jitpack.io'}
    }
}
``` 
然后在module的build.gradle下添加 
```
dependencies{
    implemation 'com.github.Iamdyh:androidutilslib:1.0.0'
}
```
