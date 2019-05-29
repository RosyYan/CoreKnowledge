package AIOExample;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 服务端负责创建服务器端口，绑定端口，等待请求
 * @Author Rosy
 * @Date 2019-05-27
 * @Version 1.0
 **/
public class AIOServer {
    private ExecutorService executorService;
    private AsynchronousChannelGroup channelGroup; //通道组
    public AsynchronousServerSocketChannel serverSocketChannel;

    public void start(int port){
        try{
            //1.创建缓存池
            executorService = Executors.newCachedThreadPool();
            //2.创建通道组
            channelGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService,1);
            //3.创建服务器通道
            serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
            //4.绑定
            serverSocketChannel.bind(new InetSocketAddress(port));
            //5.等待客户端请求
            serverSocketChannel.accept(this, new Handler());
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AIOServer server = new AIOServer();
        server.start(888);
    }
}
