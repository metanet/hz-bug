# Reproducer for potential Hazelcast bug

Runner Runner.java main class in your IDE

This starts 3 nodes, each of which adds 50 entries to a multimap.

It also starts another node which is simply used to display what is in the multimap.

Once the nodes are started all but one of the nodes concurrently remove their entries from the multimap and shutdown
their hazelcast instance.

If all works correctly you should notice in the console output

"Map Size 50"

After the nodes have shutdown as there is just a single node (and the checker left alive).

However, sometimes you will notice

"Map Size 35"

NOTE: If it's not 50, it *always" seems to be 35 when there are 3 nodes, and 43 when there are 4 nodes in the cluster!

**You may need to run Runner several times to see the problem**

**Please note that we are using cluster config which uses TCP transport and localhost. I cannot see the issue when
using multicast transport**