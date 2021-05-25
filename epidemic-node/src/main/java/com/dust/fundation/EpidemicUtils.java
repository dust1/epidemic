package com.dust.fundation;

import com.dust.logs.LogFormat;
import com.dust.logs.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * 节点系统中通用的类
 * 主要承担的功能就是在整个存储节点层次的部分简单的功能，过于复杂的功能需要使用新的模块
 */
public class EpidemicUtils {

    private static MessageDigest messageDigest;

    //SHA1字符串长度
    private static final int SHA1_LENGTH = 40;

    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件句柄中读取SHA1编码后的字符串
     * @param raf 要读取的文件句柄
     * @return 如果存在则返回字符串，否则返回null，表示已经到文件末尾
     * @throws IOException
     *      读写文件失败
     */
    public static String readToSHA1(RandomAccessFile raf) throws IOException {
        int RemainingLen = (int) (raf.length() - raf.getFilePointer());
        if (RemainingLen < SHA1_LENGTH) {
            return "";
        }

        byte[] data = new byte[SHA1_LENGTH];
        raf.readFully(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    /**
     * 根据传入的文件头信心检查所在的文件句柄是否存在该文件头
     * @param head 要检查的文件头
     * @param raf 要检查的文件句柄
     * @return 如果存在则返回true，且句柄指针指向文件头之后的数据
     * @throws IOException
     *      读取文件失败
     */
    public static boolean checkHead(byte[] head, RandomAccessFile raf) throws IOException {
        int index = 0;
        while (index < head.length && raf.getFilePointer() < raf.length()) {
            if (raf.readByte() == head[index]) {
                index += 1;
            } else {
                index = 0;
            }
        }
        return index >= head.length;
    }

    /**
     * 生成一个随机的节点id
     * @param salt 用于生成的干扰字符串
     */
    public static String randomNodeId(String salt) {
        String str = System.currentTimeMillis() + salt;
        return getSHA1(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 将16进制字符串转为2进制数组
     * @param hexStr
     * @return
     */
    public static byte[] hexToByte(String hexStr) {
        ByteBuffer buffer = ByteBuffer.allocate(hexStr.length() / 2);
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i* 2 + 1, i * 2 + 2), 16);
            buffer.put((byte) ((high << 4) + low));
        }
        return buffer.array();
    }

    /**
     * 获取两个节点id之间的距离
     * @return 返回两个节点id之间的距离，前缀匹配表示
     */
    public static int getDis(String n1, String n2) {
        if (n1.length() != n2.length()) {
            return 0;
        }
        byte[] binary1 = hexToByte(n1);
        byte[] binary2 = hexToByte(n2);
        int result = 0;
        for (int i = 0; i < binary1.length; i++) {
            int bb = binary1[i] ^ binary2[i];
            boolean canBreak = false;
            for (int j = 7; j >= 0; j--) {
                int index = 1 << j;
                //检查对应位上的比特位是否为0，如果是0则按位或的结果与原来的值不同
                if ((bb | index) != bb) {
                    result += 1;
                } else {
                    canBreak = true;
                    break;
                }
            }
            if (canBreak) {
                break;
            }
        }
        return result;
    }

    /**
     * 计算SHA1的值
     */
    public static String getSHA1(byte[] data) {
        if (Objects.isNull(messageDigest)) {
            return null;
        }
        byte[] cipherBytes = messageDigest.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte c : cipherBytes) {
            sb.append(String.format("%02x", c));
        }
        return sb.toString();
    }

    public static long now() {
        return System.currentTimeMillis();
    }

}
