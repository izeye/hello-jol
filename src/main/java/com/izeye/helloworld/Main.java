package com.izeye.helloworld;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class Main {

    private static volatile Object consumer;

    // Run with "-Djdk.attach.allowAttachSelf"
    public static void main(String[] args) {
        System.out.println(VM.current().details());

        // Try -XX:-UseCompressedOops
        System.out.println(ClassLayout.parseClass(SimpleInt.class).toPrintable());

        SimpleInt instance = new SimpleInt();
        System.out.println(ClassLayout.parseInstance(instance).toPrintable());

        System.out.println("The identity hash code is " + System.identityHashCode(instance));
        System.out.println(ClassLayout.parseInstance(instance).toPrintable());

        // Try -XX:ObjectAlignmentInBytes=16
        System.out.println(ClassLayout.parseClass(SimpleLong.class).toPrintable());

        System.out.println(ClassLayout.parseClass(FieldsArrangement.class).toPrintable());

        testLock();

        testAging();

        // Run with "--add-exports java.base/jdk.internal.vm.annotation=ALL-UNNAMED -XX:-RestrictContended"
        System.out.println(ClassLayout.parseClass(Isolated.class).toPrintable());

        boolean[] booleans = new boolean[3];
        System.out.println(ClassLayout.parseInstance(booleans).toPrintable());
    }

    private static void testLock() {
        Lock lock = new Lock();
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        synchronized (lock) {
            System.out.println(ClassLayout.parseInstance(lock).toPrintable());
        }
    }

    private static void testAging() {
        Object instance = new Object();
        long lastAddress = VM.current().addressOf(instance);
        ClassLayout layout = ClassLayout.parseInstance(instance);
        for (int i = 0; i < 10_000; i++) {
            long currentAddress = VM.current().addressOf(instance);
            if (currentAddress != lastAddress) {
                System.out.println("Address has been changed.");
                System.out.println(layout.toPrintable());
            }

            for (int j = 0; j < 10_000; j++) {
                consumer = new Object();
            }

            lastAddress = currentAddress;
        }
    }

}
