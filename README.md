# Reproducer for potential Hazelcast bug

If multiple Hazelcast nodes attempt to remove values from a key of a multimap concurrently, then shutdown their node,
the multimap can remain in an inconsistent state with entries remaining after all have been removed.

## To reproduce

The easiest way to do this is just to run it in your IDE.

Steps:

## Step 1

Run 4 instances of HzBugTest in your IDE. (Right click on its main() method to run it).

This class adds 500 entries to a well known key of a multimap.

When all four instances are running we should have 2000 entries for that key in the multimap.

## Step 2

Run 1 instance of HzBugMapChecker in your IDE (Again right click on its main() method).

This class simply prints how many entries are in the well known key to stdout.

It should display 2000. All good so far.

## Step 3

We're going to tell all the instances of HzBugTest to remove all their entries from the well known key and then
shutdown their hazelcast instance.

We want this to happen at the same time so I've implemented a signal handler in the class that will do this when
it receives a SIGINT signal. This can be sent to a *nix process using:

kill -2 <pid>

You don't have to do it this way, and the same problem occurs as long as the remove and shutdown happens concurrently,
however you trigger it, but its convenient to do this using signals.

Ok, to kill the processes do something like:

ps aux | grep HzBugTest

This will list the pids of the processes, then to send SIGINT to them do:

kill -2 pid1 pid2 pid3 pid4

Where pidN is the Nth pid listed above.

## Step 4

Wait a bit and look at the output of the HzBugMapChecker, you should see some remaining entries that never get removed,
this is despite the return value from the map.remove operation being true, representing success.

## Observations

The problem only occurs if the nodes remove entries then immediately shutdown their hazelcast.

If an artificial delay is inserted between the remove and the shutdown then the problem does not manifest.

This strongly implies that the cluster wide state of the map is not made consistent until some time after the calls
to map.remove have returned, which should not be the case.

