package club.xyes.zkh.retail.commons.utils;


import java.math.BigDecimal;

/**
 * Create by 郭文梁 2019/6/21 0021 09:11
 * MoneyUtils
 * 金额相关工具类
 *
 * @author 郭文梁
 * @data 2019/6/21 0021
 */
public class MoneyUtils {
    private static final BigDecimal FEN_YUAN_RATE = BigDecimal.valueOf(100);

    /**
     * 分 -> 元
     *
     * @param fen 分
     * @return 元
     */
    public static String fen2Yuan(int fen) {
        return new BigDecimal(fen)
                .setScale(2, BigDecimal.ROUND_DOWN)
                .divide(FEN_YUAN_RATE, BigDecimal.ROUND_DOWN)
                .toString();
    }
}
