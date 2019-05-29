package BIOExample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description BIO Example
 * @Author Rosy
 * @Date 2019-05-26
 * @Version 1.0
 **/
public class BIOServer {
    private static final int PORT = 888;

    public static void main(String[] args) {
        ThreadPoolExecutor executor = null;
        Socket socket = null;
        ServerSocket server = null;
        try{
            server = new ServerSocket(PORT);
            System.out.println("BIO Server open...");
            executor = new ThreadPoolExecutor(10, 100, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50));
            while (true){
                socket = server.accept(); // 阻塞,等待client请求
                ServerHandler handler = new ServerHandler(socket);
                executor.execute(handler);

            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if (null != socket){
                    socket.close();
                    socket = null;
                }
                if (null != server){
                    server.close();
                    server = null;
                    System.out.println("BIO server closed!!!");
                }
                executor.shutdown();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
