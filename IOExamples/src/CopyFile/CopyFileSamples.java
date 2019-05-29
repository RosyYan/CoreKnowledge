package CopyFile;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * @Description TODO
 * @Author Rosy
 * @Date 2019-05-28
 * @Version 1.0
 **/
public class CopyFileSamples {
    public static void copyFileByStream(File source, File dest) throws
            IOException {
        try (InputStream is = new FileInputStream(source);
             OutputStream os = new FileOutputStream(dest);) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    public static void copyFileByChannel(File source, File dest) throws
            IOException {
        try (FileChannel sourceChannel = new FileInputStream(source)
                .getChannel();
             FileChannel targetChannel = new FileOutputStream(dest).getChannel
                     ()) {
            for (long count = sourceChannel.size(); count > 0; ) {
                long transferred = sourceChannel.transferTo(
                        sourceChannel.position(), count, targetChannel);
                sourceChannel.position(sourceChannel.position() + transferred);
                count -= transferred;
            }
        }
    }

    public static void spend() {
        long start = System.currentTimeMillis();
        File source = new File("C:\\Users\\Rosy\\Desktop\\test.txt");
        File dest = new File("C:\\Users\\Rosy\\Desktop\\dest.txt");
        try {
            copyFileByStream(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Stream spends time: " + (end - start));
    }

    public static void spend1(){
        long start = System.currentTimeMillis();
        File source = new File("C:\\Users\\Rosy\\Desktop\\test.txt");
        File dest = new File("C:\\Users\\Rosy\\Desktop\\dest1.txt");
        try{
            copyFileByChannel(source, dest);
        }catch (IOException e){
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Channel spends time: "+ (end - start));
    }

    public static void main(String[] args) {
        spend();
        spend1();
    }

}
