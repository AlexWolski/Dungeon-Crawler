package Tanks3D.DisplayComponents.Camera.Threads;

import java.util.concurrent.Callable;

public class CalcWallBufferThread implements Callable<Void> {
    private final ThreadArgs threadArgs;

    CalcWallBufferThread(ThreadArgs threadArgs) {
        this.threadArgs = threadArgs;
    }

    @Override
    public Void call() {
        return null;
    }
}
