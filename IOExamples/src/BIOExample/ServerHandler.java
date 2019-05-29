package BIOExample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @Description TODO
 * @Author Rosy
 * @Date 2019-05-26
 * @Version 1.0
 **/
public class ServerHandler implements Runnable {
    private Socket socket;
    public ServerHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try{
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new PrintWriter(this.socket.getOutputStream(), true);
            String body = null;
            while(true){
                body = reader.readLine();
                if (null == body){
                    break;
                }
                System.out.println("server get the client's arg: "+ body);
                writer.println(body + ". Server: OK");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (null != writer)
                writer.close();
            try{

                if (null != reader)
                    reader.close();
                if (null != this.socket){
                    this.socket.close();
                    this.socket = null;
                }
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}
