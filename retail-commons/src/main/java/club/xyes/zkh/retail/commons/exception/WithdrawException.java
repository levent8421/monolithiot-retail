package club.xyes.zkh.retail.commons.exception;

/**
 * Create by 郭文梁 2019/5/30 0030 17:59
 * WithdrawException
 * 提现异常
 *
 * @author 郭文梁
 * @data 2019/5/30 0030
 */
public class WithdrawException extends Exception {
    public WithdrawException() {
    }

    public WithdrawException(String message) {
        super(message);
    }

    public WithdrawException(String message, Throwable cause) {
        super(message, cause);
    }

    public WithdrawException(Throwable cause) {
        super(cause);
    }

    public WithdrawException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
