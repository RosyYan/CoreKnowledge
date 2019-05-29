package BIOExample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

/**
 * @Description TODO
 * @Author Rosy
 * @Date 2019-05-26
 * @Version 1.0
 **/
public class BIOClient {
    private static final int PORT = 888;
    private static String address = "127.0.0.1";

    public static void main(String[] args) {
        for(int i = 0; i<10;i++){
            request(i);
        }
    }

    public static void request(int i){
        BufferedReader reader = null;
        PrintWriter writer = null;
        try(Socket socket = new Socket(address,PORT)){
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(),true);
            Random random = new Random(System.currentTimeMillis());
            writer.println(random.nextInt(4));
            System.out.println("The " + i + " client get the server msg:"+ reader.readLine());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                reader.close();
                writer.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
