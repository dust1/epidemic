package com.dust;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import com.dust.fundation.EpidemicUtils;
import com.dust.router.kademlia.timer.PingTask;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

/**
 * Unit test for simple App.
 */
public class UtilTest {

    private String node1;
    private String node2;

    @Before
    public void nodeInit() {
        byte[] data = String.valueOf(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8);
        node1 = EpidemicUtils.getSHA1(data);
        data = String.valueOf(System.currentTimeMillis() + 123).getBytes(StandardCharsets.UTF_8);
        node2 = EpidemicUtils.getSHA1(data);
    }

    @Test
    public void hexoToByteTest() {
        System.out.println(node1);
        byte[] data = EpidemicUtils.hexToByte(node2);
        int[] list = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            list[i] = (int) data[i];
        }
        System.out.println(Arrays.toString(list));
    }

    @Test
    public void disTest() {
        int len = Math.min(node1.length(), node2.length());
        byte[] b1 = EpidemicUtils.hexToByte(node1);
        byte[] b2 = EpidemicUtils.hexToByte(node2);
        System.out.println("node1=" + node1 + ", node2=" + node2);
        int prev = 0;
        for (int i = 0; i < len / 2; i++) {
            int bb = b1[i] ^ b2[i];
            boolean canBreak = false;
            for (int j = 7; j >= 0; j--) {
                int check = 1 << j;
                if ((bb | check) != bb) {
                    prev += 1;
                } else {
                    canBreak = true;
                    break;
                }
            }
            if (canBreak) {
                break;
            }
        }
        System.out.println(prev);
    }

    @Test
    public void timerTest() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("timer");
            }
        }, 100000,10000);
    }

    @Test
    public void streamTest() {
        int[] nums = new int[1000];
        for (int i = 0; i < 1000; i++) {
            nums[i] = i + 1;
        }
        var list = new ArrayList<Integer>();
        var list2 = new ArrayList<Integer>();
        Arrays.stream(nums)
                .parallel()
                .map(i -> i + 10)
                .peek(list::add)
                .forEach(list2::add);
        Integer[] l1 = list.toArray(new Integer[list.size()]);
        Integer[] l2 = list2.toArray(new Integer[list.size()]);
        assertArrayEquals(l1, l2);
    }

}
