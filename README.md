# py3dtiles_java
支持Window和Linux下点云(las)转换成3dTiles。

### 依赖
py3dtiles源码
```html
https://github.com/hejian3/py3dtiles
```

### 环境
* JDK11
* CentOS 7
* 拷贝py3dtiles-1.0-SNAPSHOT.jar 到 /root/las
* 拷贝202305161756050332_202305161756110548.las 到目录  /root/las

### 运行
```bash
java -jar py3dtiles-1.0-SNAPSHOT.jar
```

### 测试
```bash
curl http://localhost:8080/test
```
在/root/las1目录下查看有(tileset.json)代表转换成功