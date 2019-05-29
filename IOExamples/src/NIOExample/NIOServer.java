package NIOExample;

import BIOExample.BIOServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description TODO
 * @Author Rosy
 * @Date 2019-05-27
 * @Version 1.0
 **/
public class NIOServer implements Runnable {
    private final int BUFFER_SIZE = 1024;
    private final int PORT = 888;
    private ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    private Selector selector;

    public NIOServer() {
        start();
    }

    public static void main(String[] args) {
        new Thread(new NIOServer()).start();
    }

    private void start() {
        try {
            //1.开启多路复用器
            selector = Selector.open();
            //2.打开server读写通道
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            //3.设置通道非阻塞模式
            serverChannel.configureBlocking(false);
            //4.绑定端口
            serverChannel.socket().bind(new InetSocketAddress(PORT));
            //5.将服务通道注册到selector,并设置关注点
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server start >>>port：" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //单个线程负责selector的轮询
    @Override
    public void run() {
        while(true){
            try{
                //1.selector监听时阻塞
                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                //2.不停地轮询
                while(iter.hasNext()){
                    SelectionKey key = iter.next();
                    iter.remove();
                    //3.只获取有效的key
                    if (!key.isValid())
                        continue;
                    //4.处理阻塞状态
                    if (key.isAcceptable())
                        accept(key);
                    //5.处理可读状态
                    if (key.isReadable())
                        read(key);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key){
        try{
            //1.获取server通道
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            //2.执行阻塞方法
            SocketChannel socketChannel = server.accept();
            socketChannel.configureBlocking(false);
            //3.将读取通道注册到selector
            socketChannel.register(selector, SelectionKey.OP_READ);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key){
        try{
            readBuffer.clear();
            //1.获取读通道
            SocketChannel socketChannel = (SocketChannel) key.channel();
            int count = socketChannel.read(readBuffer);
            //2.处理没数据的情况
            if (-1 == count){
                key.channel().close();
                key.cancel();
                return;
            }
            //3.处理有数据的情况
            readBuffer.flip();
            byte[] bytes = new byte[readBuffer.remaining()];
            readBuffer.get(bytes);
            System.out.println("For NIO Server:" + new String(bytes)); //bytes.toString()打印出来的是乱码
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
