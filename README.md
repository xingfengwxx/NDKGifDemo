### GIF原理

https://www.w3.org/Graphics/GIF/spec-gif89a.txt

https://en.wikipedia.org/wiki/GIF

https://www.androidos.net.cn/android/7.0.0_r31/xref/external/giflib (官方的代码库)

![image-20200508215731710](https://tva1.sinaimg.cn/large/007S8ZIlly1geldz06f6zj312u0datei.jpg)

GIF图像文件以数据块(block)为单位来存储图像的相关信息。一个GIF文件由表示图形/图像的数据块、数据子块以及显示图形/图像的控制信息块组成，称为GIF数据流(Data Stream)。数据流中的所有控制信息块和数据块都必须在文件头(Header)和文件结束块(Trailer)之间。
![image-20200508213223022](https://tva1.sinaimg.cn/large/007S8ZIlgy1geld8wkrj5j31hp0u0h0y.jpg)

### giflib中gif解码方式

giflib中对gif的打开方式有三种：

DGifOpenFileName(const char *GifFileName, int *Error)
DGifOpenFileHandle(int GifFileHandle, int *Error)
DGifOpen(void *userPtr, InputFunc readFunc, int *Error)

### 如何使用giflib实现秒开，高性能

不能使用DGifSlurp方式，需要用while循环结合DGifGetRecordType函数，接收到帧的时候就要将其进行显示；要实现性能，不能每次都要读取文件进行显示，在一开始读取文件的时候要把每帧数据缓存起来，当然缓存的不是RGB数据，这样数据量会很大，缓存也只是颜色表和颜色索引，同时记录延时等少量信息，这样内存会少很多。

###实战

