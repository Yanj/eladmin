package com.test1;

/**
 * @author yanjun
 * @date 2020-11-22 16:01
 */
public class CopyTest {

    public static void main(String[] args) throws Exception {
        B b1 = new B("zhangsan");
        B b2 = new B("lisi");
        A a1 = new A("1", "2", b1, b2);
        A a2 = new A();
        a2.copy(a1);
        System.out.println(a2);
    }

}
