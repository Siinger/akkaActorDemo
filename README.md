# Java akka Actor remote Demo

## Overview

a example for server and client remote Communicate with each other

### server

- server config ActorSystem listen on port 2552
- Every 10 seconds Build remoteActor to send command to client to do something

### Client

- client config ActorSystem and listen on port 2552
- Every 5 seconds Build remoteActor to send Heart command to Server

 