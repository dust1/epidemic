package com.dust;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.CountDownLatch;

/**
 * Unit test for simple App.
 */
public class AppTest {

    public static void main(String[] args) throws InterruptedException {
        String path = "/Users/kous/Desktop/setting.conf";
        var count = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try (var raf = new RandomAccessFile(new File(path), "r")) {
                final var channel = raf.getChannel();
                count.countDown();
                Thread.sleep(3000);
            } catch (Exception e) {
            }
        });
        thread.start();

        count.await();
        try (var raf = new RandomAccessFile(new File(path), "r")) {
            final var channel = raf.getChannel();
            System.out.println("sss");
        } catch (Exception e) {
        }
    }

}
