package AIOExample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * @Description AIO客户端负责连接服务器，声明通道，连接通道
 * @Author Rosy
 * @Date 2019-05-27
 * @Version 1.0
 **/
public class AIOClient implements Runnable{
    private static int PORT = 888;
    private static String address = "127.0.0.1";
    private AsynchronousSocketChannel socketChannel;

    public AIOClient(){
        try{
            socketChannel = AsynchronousSocketChannel.open();
        }catch (IOException e){
            e.printStackTrace();
        }
        socketChannel.connect(new InetSocketAddress(address,PORT));
    }

    public void write(String req){
        try{
            socketChannel.write(ByteBuffer.wrap(req.getBytes())).get();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            socketChannel.read(buf).get();
            buf.flip();
            byte[] resBuf = new byte[buf.remaining()];
            buf.get(resBuf);
            System.out.println("Server msg: " + new String(resBuf,"utf-8").trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while (true){
        }
    }

    public static void main(String[] args) {
        for (int i=0;i<10;i++){
            AIOClient client = new AIOClient();
            new Thread(client,"myClient").start();
            client.write("I am client " + i);
        }
    }
}
