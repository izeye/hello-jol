package com.izeye.helloworld;

import jdk.internal.vm.annotation.Contended;

public class Isolated {

    @Contended
    private int v1;

    @Contended
    private long v2;

}
