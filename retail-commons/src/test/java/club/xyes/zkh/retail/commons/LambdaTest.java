package club.xyes.zkh.retail.commons;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Create by 郭文梁 2019/5/16 0016 17:43
 * LambdaTest
 * 测试lambda
 *
 * @author 郭文梁
 * @data 2019/5/16 0016
 */
public class LambdaTest {
    @Test
    public void test1() {
        List<String> source = Arrays.asList("1", "2", "2", "3", "4", "1");
        List<String> result = source.stream().distinct().collect(Collectors.toList());
        System.out.println(result);
    }

    @Test
    public void testRandom() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(10));
        }
    }
}
