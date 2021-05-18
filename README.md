# volatil - Java Chat App

A multithreaded console chat application designed to demonstrate client-server and multithreading programming practices. Each message is ephemeral, lasting only as long as the users that saw it are connected and all clients are anonymised.

## Server

The server runs two main processes:

1. **Connection Handler** - As the server accepts incoming requests, new threads are spawned for each, with a central list of all connected clients facilitating broadcasting messages in addition to adding and removing clients as they connect and disconnect.
2. **Exit Handler** - While the connection handler is managing connections, there is another thread in the background listening to `System.in` which when it detects "EXIT" will send a kill message to all clients, before killing the server itself.

## Clients

Every client also has two main processes:

1. **Transmitter** - When user's or bots wish to send a message to the server, the transmitter sends it to its `ClientHandler` counterpart on the server which either broadcasts the message to all connected clients or kills the handler if the client has sent an "EXIT" message.
2. **Receiver** - When any message is sent to the user, the receiver prints it to the user. If the user is sent an "EXIT" message from the server, it will automatically kill the user.

There is also (on non-human) clients, an **Exit Handler** process just like on the server which is listening for the "EXIT" command to be typed into the command prompt.

## Private Messages

As part of integrating the Dungeon of Doom code, I had to decide on a format for private messages sent between two users, without others seeing. What I settled on was **`[<Origin>] [<Destination>] <Message>`**. I was inspired to make this choice based on how my logging system made the output of any given part of the application look, so this was a natural extension. This necessitated additional logic on the part of the client to engage in a handshake with another (bot) client by sending a "JOIN" message with the id of the client they want to private message.

When the user has finished private messaging the Dungeon of Doom bot, they can send an "EXIT" that doesn't cause them to leave the server, but just causes them to exit the private conversation.

## Dungeon of Doom Integration

The dungeon of doom integration reuses much of the same code as the previous, non-networked iteration, but there are two important differences:

1. **RemotePlayer** - In order to keep interfaces in line with the previous iteration, the `RemotePlayer` class must have its next move set externally so that when the `getNextAction` method is called, no console input is required.
2. **RemoteGameLogic** - This also makes some superficial changes to how the game works. Instead of using a central game loop that is waiting on console input, a function can be called that takes the player's instruction as a parameter. This makes game execution run piecemeal and removes the need for blocking on the console.
