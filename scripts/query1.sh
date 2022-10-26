#!/bin/bash

tar -xf "client/target/tpe2-g3-client-1.0-SNAPSHOT-bin.tar.gz" -C "client/target/"
java -Dhazelcast.logging.type=none  -cp 'client/target/tpe2-g3-client-1.0-SNAPSHOT/lib/jars/*'  "ar.edu.itba.pod.client.query1.Client" "$@"
