package com.oxymoron.util.multi;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiskIOExecutors implements Executor {
    private final Executor diskIo;

    DiskIOExecutors() {
        this.diskIo = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(Runnable command) {
        this.diskIo.execute(command);
    }
}
