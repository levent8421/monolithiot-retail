package club.xyes.zkh.retail.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class CRC16Test {
    @Test
    public void test() {
        final int[] data = {
                0x01,
                0x10,
                0x00,
                0x0C,
                0x00,
                0x02,
                0x04,
                0x00,
                0x00,
                0x00,
                0x00
        };
        final int res = CRC.crc16(data);
        final String hex = Integer.toHexString(res);
        log.debug("Res hex {}", hex);
    }
}