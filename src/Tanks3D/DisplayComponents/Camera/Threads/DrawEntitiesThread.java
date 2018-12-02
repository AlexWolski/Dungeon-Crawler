package Tanks3D.DisplayComponents.Camera.Threads;

import java.util.concurrent.Callable;

public class DrawEntitiesThread implements Callable<Void> {
    private final ThreadArgs threadArgs;

    DrawEntitiesThread(ThreadArgs threadArgs) {
        this.threadArgs = threadArgs;
    }

    @Override
    public Void call() {
        return null;
    }
}
