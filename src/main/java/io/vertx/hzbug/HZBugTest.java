/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.hzbug;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;
import sun.misc.Signal;

import java.util.UUID;

public class HZBugTest extends HzBugBase{

  public static void main(String[] args) {
    new HZBugTest().run();
  }

  private static final int NUM_ENTRIES = 500;

  String serverID;
  MultiMap<String, String> map;
  HazelcastInstance hazelcast;

  public void run() {
    System.setProperty("hazelcast.shutdownhook.enabled", "false");
    hazelcast = Hazelcast.newHazelcastInstance(loadConfigFromClasspath());
    this.serverID = UUID.randomUUID().toString();
    System.out.println("Starting instance, serverID is " + serverID);
    this.map = hazelcast.getMultiMap("mymap");
    populateMap();

    Signal.handle(new Signal("INT"), signal -> {
      System.out.println("Caught SIGINT, gonna remove entries!");
      removeFromMap();
    });
  }

  private void populateMap() {
    for (int i = 0; i < NUM_ENTRIES; i++) {
      map.put("ping-address", serverID + i);
    }
    System.out.println("Populated map");
  }

  private void removeFromMap() {
    System.out.println("Shutting down - removing entries from map");
    for (int i = 0; i < NUM_ENTRIES; i++) {
      System.out.println(map.remove("ping-address", serverID + i));
    }
    System.out.println("Removal complete");

//    try {
//      Thread.sleep(5000);
//    } catch (Exception e) {
//    }

    hazelcast.shutdown();
  }




}
