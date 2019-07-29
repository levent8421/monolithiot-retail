package club.xyes.zkh.retail.commons.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Create by 郭文梁 2019/6/25 0025 10:32
 * CRC16
 * CRC16工具类
 *
 * @author 郭文梁
 * @data 2019/6/25 0025
 */
@Slf4j
public class CRC {
    /**
     * 一个字节包含位的数量 8
     */
    private static final int BITS_OF_BYTE = 8;
    /**
     * 多项式
     */
    private static final int POLYNOMIAL = 0xA001;
    /**
     * 初始值
     */
    private static final int INITIAL_VALUE = 0xFFFF;

    /**
     * CRC16 编码
     *
     * @param bytes 编码内容
     * @return 编码结果
     */
    public static int crc16(int[] bytes) {
        int res = INITIAL_VALUE;
        for (int data : bytes) {
            res = res ^ data;
            for (int i = 0; i < BITS_OF_BYTE; i++) {
                res = (res & 0x0001) == 1 ? (res >> 1) ^ POLYNOMIAL : res >> 1;
            }
        }
        return revert(res);
    }

    /**
     * 翻转16位的高八位和低八位字节
     *
     * @param src 翻转数字
     * @return 翻转结果
     */
    private static int revert(int src) {
        int lowByte = (src & 0xFF00) >> 8;
        int highByte = (src & 0x00FF) << 8;
        return lowByte | highByte;
    }
}
