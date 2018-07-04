package tests;

public class Test {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();
        A bb = new B();
        System.out.println(a.a);
        System.out.println(b.a);
        System.out.println(bb.getA());
    }
}
