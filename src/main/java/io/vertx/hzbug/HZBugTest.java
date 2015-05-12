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

import java.util.UUID;

public class HZBugTest extends HzBugBase {

  private static final int NUM_ENTRIES = 50;

  String serverID;
  MultiMap<String, String> map;
  HazelcastInstance hazelcast;

  public HZBugTest() {
    System.setProperty("hazelcast.shutdownhook.enabled", "false");
    hazelcast = Hazelcast.newHazelcastInstance(loadConfigFromClasspath());
    this.serverID = UUID.randomUUID().toString();
    System.out.println("Starting instance, serverID is " + serverID);
    this.map = hazelcast.getMultiMap("mymap");
    populateMap();
  }

  private void populateMap() {
    for (int i = 0; i < NUM_ENTRIES; i++) {
      map.put("ping-address" + i, serverID);
    }
    System.out.println("Populated map");
  }

  public void removeEntriesFromMapAndShutdown() {
    System.out.println("removing entries from map");
    for (int i = 0; i < NUM_ENTRIES; i++) {
      boolean removed = map.remove("ping-address" + i, serverID);
      System.out.println("Removing entry " + serverID  + " from subsmap, result = " + removed);
    }
    System.out.println("Removal complete, shutting down hazelcast");

    hazelcast.getLifecycleService().shutdown();
    System.out.println("Hazelcast is shutdown");
  }




}
