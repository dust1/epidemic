package com.dust;

import com.dust.fundation.EpidemicUtils;
import com.dust.router.kademlia.NodeTriadRouterNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTriadRouterNodeTest {

    private NodeTriadRouterNode node;

    private String savePath = "/Users/kous/myProjects/javaproject/epidemic/temp/node.cache";


    private List<NodeTriadRouterNode> nodes;

    @Before
    public void before() {
        node = new NodeTriadRouterNode("ss", "127.0.0.1", 8080);

        nodes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            byte[] timestamp = String.valueOf(System.currentTimeMillis()).getBytes();
            String nodeId = EpidemicUtils.getSHA1(timestamp);
            nodes.add(new NodeTriadRouterNode(nodeId, "127.0.0." + i, i + 8080));
        }
    }

    @Test
    public void readTest() {
        try (RandomAccessFile raf = new RandomAccessFile(savePath, "rw");
            FileChannel fc = raf.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(2);
            fc.read(buffer);
            assertEquals(raf.getFilePointer(), 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void encoderTest() {
        Buffer buffer = node.toBuffer();
        System.out.println(buffer);
    }

    @Test
    public void saveTest() {
        ByteBuffer buffer = node.toBuffer();
        buffer.flip();
        try (RandomAccessFile wf = new RandomAccessFile(savePath, "rw")) {
            wf.seek(wf.length());
            FileChannel channel = wf.getChannel();
            channel.write(buffer);
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void batchSaveAndLoadTest() {
        long seek = 0;
        try (RandomAccessFile raf = new RandomAccessFile(savePath, "rw");
             FileChannel channel = raf.getChannel()) {
            raf.seek(seek);
            for (NodeTriadRouterNode n : nodes) {
                ByteBuffer buffer = n.toBuffer();
                buffer.flip();
                seek += channel.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<NodeTriadRouterNode> list = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(savePath, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                list.add(NodeTriadRouterNode.fromFile(raf));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(list.size(), nodes.size());

        for (int i = 0; i < list.size(); i++) {
            assertEquals(nodes.get(i).getKey(), list.get(i).getKey());
            assertEquals(nodes.get(i).getHost(), list.get(i).getHost());
            assertEquals(nodes.get(i).getPort(), list.get(i).getPort());
        }
    }

    @Test
    public void loadTest() {
        try (RandomAccessFile wf = new RandomAccessFile(savePath, "r")) {
            NodeTriadRouterNode n = NodeTriadRouterNode.fromFile(wf);
            System.out.println(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
