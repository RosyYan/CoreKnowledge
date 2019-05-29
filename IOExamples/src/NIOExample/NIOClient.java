package NIOExample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description
 * The Client is responsible for connecting server,
 * declare and connect channel.
 * @Author Rosy
 * @Date 2019-05-27
 * @Version 1.0
 **/
public class NIOClient {
    private final static int PORT = 888;
    private final static int BUFFER_SIZE = 1024;
    private final static String address = "127.0.0.1";

    public static void main(String[] args) {
        request();
    }

    public static void request(){
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        try(SocketChannel socketChannel = SocketChannel.open()){
            //1.连接server
            socketChannel.connect(new InetSocketAddress(address,PORT));
//            while(true){
                byte[] bytes = new byte[BUFFER_SIZE];
                //2.键盘写入数据
                System.in.read(bytes);
                //3.数据放入缓冲区
                buffer.put(bytes);
                buffer.flip();
                //4.写入通道
                socketChannel.write(buffer);
                buffer.clear();
//            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
