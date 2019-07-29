package club.xyes.zkh.retail.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;

@Slf4j
public class DateTimeUtilsTest {
    @Test
    public void testBetween() {
        Date start = DateTimeUtils.parseTime("24:00:00");
        Date end = DateTimeUtils.parseTime("23:59:59");
        Date now = DateTimeUtils.now();
        now = DateTimeUtils.cleanDate(now);
        start = DateTimeUtils.cleanDate(start);
        end = DateTimeUtils.cleanDate(end);

        log.info("Between {}", DateTimeUtils.between(now, start, end));
    }

    @Test
    public void testCurrentTime() {
        final long now = System.currentTimeMillis();
        log.info("time[{}]", now);
        log.info("time[{}]", Long.MAX_VALUE - now);
    }
}