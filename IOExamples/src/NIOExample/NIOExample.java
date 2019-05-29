package NIOExample;

import BIOExample.ServerHandler;
import com.sun.security.ntlm.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description TODO
 * @Author Rosy
 * @Date 2019-05-27
 * @Version 1.0
 **/
public class NIOExample extends Thread{
    @Override
    public void run() {
        try(Selector selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open()){
            serverChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(),888));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            while(true){
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectionKeys.iterator();
                while(iter.hasNext()){
                    SelectionKey key = iter.next();
                    handle((ServerSocketChannel)key.channel());
                    iter.remove();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handle(ServerSocketChannel server) throws IOException{
        try(SocketChannel channel= server.accept()){

            channel.write(Charset.defaultCharset().encode("Hello, client!"));
        }
    }

    public static void main(String[] args) throws IOException{
        NIOExample nioExample = new NIOExample();
        nioExample.start();
        try(Socket client =new Socket(InetAddress.getLocalHost(),888)){
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            reader.lines().forEach(s-> System.out.println(s));
        }
    }
}
