package io.github.protocol.sms.server.util;

import java.util.concurrent.atomic.AtomicInteger;

public class BoundAtomicInt {

    private final int max;

    private final AtomicInteger integer;

    public BoundAtomicInt(int max) {
        this.max = max;
        this.integer = new AtomicInteger();
    }

    public int nextVal() {
        return integer.getAndAccumulate(1, (left, right) -> {
            if (left >= max) {
                return 0;
            }
            return left + right;
        });
    }
}
