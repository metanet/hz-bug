package io.vertx.hzbug;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Runner {

  public static void main(String[] args) {
    new Runner().go();
    pause(Long.MAX_VALUE);
  }

  private static final int NUM_NODES = 3;

  private void go() {

    try {
      Executor exec = Executors.newFixedThreadPool(NUM_NODES);
      for (int i = 0; i < NUM_NODES; i++) {
        // Warmup threads - seems to help a bit (?)
        exec.execute(() -> System.out.println("foo"));
      }
      HZBugTest[] nodes = new HZBugTest[NUM_NODES];
      for (int i = 0; i < NUM_NODES; i++) {
        nodes[i] = new HZBugTest();
        System.out.println("Started node " + i);
      }
      for (int i = 1; i < NUM_NODES; i++) {
        int index = i;
        exec.execute(() -> nodes[index].removeEntriesFromMapAndShutdown());
      }
      HzBugMapChecker checker = new HzBugMapChecker();
      exec.execute(() -> {
        while (true) {
          checker.displayMap();
          pause(5000);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void pause(long ms) {
    try {
      Thread.sleep(ms);
    } catch (Exception e) {
    }
  }
}
