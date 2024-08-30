1.Server Initialization:

    The 'server' starts and halts at line "serverSocket.accept()". This call blocks the thread until a client connection request
    is received. Once a 'client' connects, the method returns a "Socket" representing the connection with the client.

2.Client Initialization:

    The 'client' starts and creates a "ClientHandler" to process incoming messages from the server. "thread.start()" initiates a
    new thread that runs the "run()" method of the "ClientHandler" class. This thread enters a "while" loop, waiting for messages
    from the server. 
    +Meanwhile, the main thread of the client reads user-input messages and sends them to the server,
    exiting the loop only when the client is closed.

3.Server Continuation:

    After the client is started, the 'server' continues its operation. It creates a "ServerHandler" to manage the connection
    with the specific client. "ServerHandler" runs in a separate thread. At line "thread.start()", this thread executes the
    "run()" method of the "ServerHandler", which also calls "clientRegistration()". The server sends several messages to the
    connected 'client' and waits in a "while" loop within "parseClientMessage" for messages from clients, exiting the loop only
    when the server is shut down.

4.Client Interaction:

    After "ClientHandler" is started "thread.start();", the main thread of the client remains active, continuing to run. It
    pauses in a "while" loop that waits for user input. User-entered messages are sent to the server through the "send()" method
    of the "ClientHandler".