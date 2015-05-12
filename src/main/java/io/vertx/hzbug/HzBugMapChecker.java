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

public class HzBugMapChecker extends HzBugBase {

  MultiMap<String, String> map;

  public HzBugMapChecker() {
    HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance(loadConfigFromClasspath());
    this.map = hazelcast.getMultiMap("mymap");
  }

  public void displayMap() {
    for (String key : map.keySet()) {
      System.out.printf("%-35s", key);
      map.get(key);
      System.out.println(map.get(key));
    }
    System.out.println("Map Size:" + map.size());
  }

}
