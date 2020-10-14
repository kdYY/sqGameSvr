package org.sq.gameDemo.svr.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName BitConverter
 * @Description: TODO
 * @Author kevins
 * @Date 2019/11/28
 * @Version V1.0
 **/
public class BitConverter {
    public static short ToInt16(byte[] bytes, int offset) {
        short result = (short) ((int) bytes[offset] & 0xff);
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        return (short) (result & 0xffff);
    }

    public static int ToUInt16(byte[] bytes, int offset) {
        int result = (int) bytes[offset + 1] & 0xff;
        result |= ((int) bytes[offset] & 0xff) << 8;
        return result & 0xffff;
    }

    public static int ToInt32(byte[] bytes, int offset) {
        int result = (int) bytes[offset] & 0xff;
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        result |= ((int) bytes[offset + 2] & 0xff) << 16;
        result |= ((int) bytes[offset + 3] & 0xff) << 24;
        return result;
    }

    public static long ToUInt32(byte[] bytes, int offset) {
        long result = (int) bytes[offset] & 0xff;
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        result |= ((int) bytes[offset + 2] & 0xff) << 16;
        result |= ((int) bytes[offset + 3] & 0xff) << 24;
        return result & 0xFFFFFFFFL;
    }

    public static long ToInt64(byte[] buffer, int offset) {
        long values = 0;
        for (int i = 0; i < 8; i++) {
            values <<= 8;
            values |= (buffer[offset + i] & 0xFF);
        }
        return values;
    }

    public static long ToUInt64(byte[] bytes, int offset) {
        long result = 0;
        for (int i = 0; i <= 56; i += 8) {
            result |= ((int) bytes[offset++] & 0xff) << i;
        }
        return result;
    }

    public static float ToFloat(byte[] bs, int index) {
        return Float.intBitsToFloat(ToInt32(bs, index));
    }

    public static double ToDouble(byte[] arr, int offset) {
        return Double.longBitsToDouble(ToUInt64(arr, offset));
    }

    public static boolean ToBoolean(byte[] bytes, int offset) {
        return (bytes[offset] == 0x00) ? false : true;
    }

    public static byte[] GetBytes(short value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (value & 0xff);
        bytes[1] = (byte) ((value & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] GetBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value) & 0xFF); //最低位
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >>> 24)); //最高位，无符号右移
        return bytes;
    }

    public static byte[] GetBytes(long values) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte) ((values >> offset) & 0xff);
        }
        return buffer;
    }

    public static byte[] GetBytes(float value) {
        return GetBytes(Float.floatToIntBits(value));
    }

    public static byte[] GetBytes(double val) {
        long value = Double.doubleToLongBits(val);
        return GetBytes(value);
    }

    public static byte[] GetBytes(boolean value) {
        return new byte[]{(byte) (value ? 1 : 0)};
    }

    public static byte IntToByte(int x) {
        return (byte) x;
    }

    public static int ByteToInt(byte b) {
        return b & 0xFF;
    }

    public static char ToChar(byte[] bs, int offset) {
        return (char) (((bs[offset] & 0xFF) << 8) | (bs[offset + 1] & 0xFF));
    }

    public static byte[] GetBytes(char value) {
        byte[] b = new byte[2];
        b[0] = (byte) ((value & 0xFF00) >> 8);
        b[1] = (byte) (value & 0xFF);
        return b;
    }

    public static byte[] Concat(byte[]... bs) {
        int len = 0, idx = 0;
        for (byte[] b : bs) len += b.length;
        byte[] buffer = new byte[len];
        for (byte[] b : bs) {
            System.arraycopy(b, 0, buffer, idx, b.length);
            idx += b.length;
        }
        return buffer;
    }

    public static void main(String[] args) {
        int a = 9999;
        byte [] b = GetBytes(a);
        System.out.println(b);


        byte[] data = {01, 12, 34, 01, 03, 01};

        //高八位是0x34 低八位是 0x12
        //转成in
        short result = (short) ( data[1] & 0xff);
        result |= (data[2] & 0xff) << 8;
        int re = result & 0xffff;
        System.out.println(re);



    //高八位是0x12 低八位是 0x34
        short result1 = (short) ( (data[1] & 0xff ) << 8);
        result1 |= (data[2] & 0xff);
        int re1 = result1 & 0xffff;
        System.out.println(re1);


        Set<Integer> set = new HashSet<>();
        boolean add = set.add(1);
        boolean add1 = set.add(1);
        System.out.println(add1);

        byte[] a111 = {(byte) 255,(byte) 255};

        int i = Byte.toUnsignedInt(a111[0]);
        String j = Byte.toString(a111[0]);
    }
}
