package Tanks3D.DisplayComponents.Camera.Threads;

import java.util.concurrent.Callable;

public class DrawWallsThread implements Callable<Void> {
    private final ThreadArgs threadArgs;

    DrawWallsThread(ThreadArgs threadArgs) {
        this.threadArgs = threadArgs;
    }

    @Override
    public Void call() {
        return null;
    }
}
