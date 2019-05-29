<font face='Consolas'>
## IO模型
1. BIO
    Block-IO，通过Socket和ServerSocket完成客户和服务端之间的通信。最大的问题是阻塞、同步、建立连接耗时（依赖网络）
1. NIO
    Non Block-IO，通过单线程轮询事件的机制，高效地定位就绪的Channel。
    NIO的主要组成部分：
    * Selector，是实现多路复用的基础，基于底层操作系统机制，实现了单线程对多个Channel的高效管理。不过NIO会阻塞在Selector的select方法上。
    * Buffer，高效的数据容器，所有数据的读写都是用Buffer处理的。可以参考相关[教程](http://tutorials.jenkov.com/java-nio/buffers.html)。
    * Channel，类似Linux的文件描述符，用来批量操作IO。

    NIO使用通道来操作数据，将`Channel`注册到多路复用器`Selector`，当某个`Channel`发生读或写事件时，会被`Selector`轮询出来，然后通过`SelectionKey`获取就绪`Channel`的集合，进行后续的IO操作。为了避免无限轮询造成阻塞，JDK使用epoll代替传统Selector实现，所以并没有最大连接数限制，可以接入成千上万个客户端。
    >epoll，对select操作进行统计，每次select到空结果就计数一次，当连续发生N次轮询就触发epoll，以此重建Selector。
1. AIO
Asynchronous-IO，采用异步通道实现异步通信，其read和write方法异步，主动通知程序。
## 文件拷贝
1. 基于Stream方式
    为源文件构建一个`FileInputStream`读取，为目标文件构建一个`FileOutputStream`完成写入工作。
    缺点：需要进行多次上下文切换，降低IO效率。比如App要读取数据，先要在内核态将数据从磁盘读到内核缓存，再切换到用户态将数据从内核缓存读取到用户缓存。

1. 基于NIO的transferTo方式
    在Linux或Unix上，使用到零拷贝技术，数据传输不需要用户态参与，省去上下文切换的开销和不必要的内存拷贝。

**提高类似拷贝等IO操作的性能的基本思路：**
1. 使用缓存等机制，合理减少IO次数。
1. 使用transferTo等机制(零拷贝)，减少上下文切换和额外IO操作。
1. 尽量减少不必要的转换过程，比如编解码，对象序列化和反序列化，可以的话直接传输二进制信息。