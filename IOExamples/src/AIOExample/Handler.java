package AIOExample;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

/**
 * @Description 服务器端的Handler负责读写数据
 * @Author Rosy
 * @Date 2019-05-27
 * @Version 1.0
 **/
public class Handler implements CompletionHandler<AsynchronousSocketChannel,AIOServer> {
    private final int BUFFER_SIZE = 1024;
    @Override
    public void completed(AsynchronousSocketChannel result, AIOServer attachment) {
        //保证多个客户端都可以阻塞
        attachment.serverSocketChannel.accept(attachment,this);
        read(result);
    }

    private void read(final AsynchronousSocketChannel socketChannel){
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        socketChannel.read(buffer, buffer,new CompletionHandler<Integer,ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                //读取后，重置标志位
                attachment.flip();
                String data = new String(attachment.array()).trim();
                System.out.println("Server get the client's msg: "+data);
                String res = "OK, " + data.substring(5);
                write(socketChannel, res);
            }
            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
    }

    private void write(AsynchronousSocketChannel socketChannel, String res){
        try {
            //1.数据写入缓冲区
            ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
            buf.put(res.getBytes());
            buf.flip();
            //2.从缓冲区写入到通道中
            socketChannel.write(buf).get();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
    }
    @Override
    public void failed(Throwable exc, AIOServer attachment) {
        exc.printStackTrace();
    }
}
