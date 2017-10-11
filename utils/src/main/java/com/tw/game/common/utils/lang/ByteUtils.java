package com.tw.game.common.utils.lang;

public final class ByteUtils {

    public static final int intFromByte(byte[] array) {
        return intFromByte(array, 0);
    }

    public static final int intFromByte(byte[] array, int offset) {
        return array[offset] << 24 |
                (array[offset + 1] & 0xFF) << 16 |
                (array[offset + 2] & 0xFF) << 8 |
                (array[offset + 3] & 0xFF);
    }

    public final static byte[] intToByte(int number) {
        return intToByte(number, new byte[4], 0);
    }

    public final static byte[] intToByte(int number, byte[] array, int offset) {
        array[offset + 3] = (byte) number;
        array[offset + 2] = (byte) (number >> 8);
        array[offset + 1] = (byte) (number >> 16);
        array[offset] = (byte) (number >> 24);
        return array;
    }

    public static long longFromByte(byte[] b) {
        return longFromByte(b, 0);
    }

    public static long longFromByte(byte[] array, int offset) {
        long s0 = array[offset] & 0xFF;
        long s1 = array[offset + 1] & 0xFF;
        long s2 = array[offset + 2] & 0xFF;
        long s3 = array[offset + 3] & 0xFF;
        long s4 = array[offset + 4] & 0xFF;
        long s5 = array[offset + 5] & 0xFF;
        long s6 = array[offset + 6] & 0xFF;
        long s7 = array[offset + 7] & 0xFF;
        return s0 << 56 | s1 << 48 | s2 << 40 | s3 << 32 | s4 << 24 | s5 << 16 | s6 << 8 | s7;
    }

    public static byte[] longToByte(long number) {
        return longToByteWithOffset(number, new byte[8], 0);
    }

    public final static byte[] longToByteWithOffset(long number, byte[] array, int offset) {
        array[offset + 7] = (byte) number;
        array[offset + 6] = (byte) (number >> 8);
        array[offset + 5] = (byte) (number >> 16);
        array[offset + 4] = (byte) (number >> 24);
        array[offset + 3] = (byte) (number >> 32);
        array[offset + 2] = (byte) (number >> 40);
        array[offset + 1] = (byte) (number >> 48);
        array[offset] = (byte) (number >> 56);
        return array;
    }

    public static final short shortFromByte(byte[] array) {
        return shortFromByteWithOffset(array, 0);
    }

    public static final short shortFromByteWithOffset(byte[] array, int offset) {
        return (short) ((array[offset] & 0xFF) << 8 | (array[offset + 1] & 0xFF));
    }

    public final static byte[] shortToByte(short number) {
        return shortToByteWithOffset(number, new byte[2], 0);
    }

    public final static byte[] shortToByteWithOffset(short number, byte[] array, int offset) {
        array[offset + 1] = (byte) number;
        array[offset] = (byte) (number >> 8);
        return array;
    }

}
