package club.xyes.zkh.retail.service.encrypt.impl;

import org.junit.Test;

public class PasswordEncryptorImplTest {
    @Test
    public void testEncode() {
        PasswordEncryptorImpl encryptor = new PasswordEncryptorImpl();
        String res = encryptor.encode("111111");
        System.out.println(res);
        res = encryptor.encode("111111");
        System.out.println(res);
        res = encryptor.encode("111111");
        System.out.println(res);
    }
}