package com.mygdx.game;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.commands.*;
import com.mygdx.entities.Fighter;
import com.mygdx.utils.CircularBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.LinkedBlockingDeque;

public class Multiplayer {

    private FightingGame game;
    private Server server;
    private Client client;
    private int serverPort;
    private int connectedPort;

    String ipAddress;
    LinkedBlockingDeque<Command> commandQueue;

    public Multiplayer(FightingGame game) {
        this.commandQueue = new LinkedBlockingDeque<>();
        this.game = game;
    }

    public void initializeServer() {
        serverPort = findFreePort();
        server = new Server();
        client = null;
        configureKryo(server.getKryo());

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {

                if (object instanceof CreateHostClientCommand) {
                    if (client == null) {
                        System.out.println("SIEMA TU NOWY KLIENT");
                        System.out.println("Received command from: " + connection.getRemoteAddressTCP());
                        initializeClientForHost(object);
                    }
                } else if (object instanceof Command) {
                    handleCommand((Command) object);
                }
            }
        });

        ipAddress = getPublicIpAddress();
        try {
            server.bind(serverPort);
            server.start();
            System.out.println(" Server started on serverPort: " + serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeClient(String ipAddress, int port) {
        client = new Client();
        configureKryo(client.getKryo());

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {

                if (object instanceof Command) {
                    handleCommand((Command) object);
                }
            }
        });

        try {
            client.start();
            //System.out.println("Klient probuje sie polaczyc z " + ipAddress + "/" + port);
            client.connect(5000, ipAddress, port);
            this.ipAddress = ipAddress;
            //sendCreateHostClientCommand(encodeIpAndPort(ipAddress, serverPort));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCreateHostClientCommand(String inviteCode) {
        // Create a new instance of CreateHostClientCommand
        CreateHostClientCommand command = new CreateHostClientCommand();
        command.setInviteCode(inviteCode);

        // Send the command to the server
        client.sendTCP(command);
    }

    public void initializeClientForHost(Object object) {
        // Odczytaj zakodowany adres IP i port
        String[] decodedInviteLink = decodeIpAndPort(((CreateHostClientCommand) object).getInviteCode());
        String ipAddress = decodedInviteLink[0];
        int portNumber = Integer.parseInt(decodedInviteLink[1]);
        System.out.println("Klient hosta probuje sie polaczyc z " + ipAddress + "/" + portNumber);
        // Inicjalizuj klienta i połącz go z serwerem gracza, który dołącza
        initializeClient(ipAddress, portNumber);
    }

    private void configureKryo(Kryo kryo) {
        kryo.register(InputHandler.class);
        kryo.register(Object[].class);
        kryo.register(CircularBuffer.class);
        kryo.register(com.badlogic.gdx.math.Rectangle.class);
        kryo.register(ArrayList.class);
        kryo.register(Fighter.class);
        kryo.register(Command.class);
        kryo.register(MoveFighterCommand.class);
        kryo.register(AttackCommand.class);
        kryo.register(BlockStunCommand.class);
        kryo.register(CrouchCommand.class);
        kryo.register(DoNothingCommand.class);
        kryo.register(HitStunCommand.class);
        kryo.register(CreateHostClientCommand.class);
    }

    private int findFreePort() {
        int currentPort = 54555;

        while (!isPortAvailable(currentPort)) {
            currentPort++;
        }

        return currentPort;
    }

    private boolean isPortAvailable(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void stopServer() {
        if (server != null) {
            server.stop();
            server.close();
            System.out.println("Server stopped");
        }
    }

    public String encodeIpAndPort(String ipAddress, int portNumber) {
        try {
            // Convert IP address to a number
            InetAddress ip = InetAddress.getByName(ipAddress);
            byte[] ipBytes = ip.getAddress();
            int ipNumber = 0;
            for (byte b : ipBytes) {
                ipNumber = (ipNumber << 8) | (b & 0xFF);
            }

            // Combine IP and port number to a unique key
            long uniqueKey = ((long) ipNumber << 16) | (portNumber & 0xFFFF);

            // Convert the key to a byte array
            byte[] keyBytes = new byte[8];
            for (int i = 7; i >= 0; i--) {
                keyBytes[i] = (byte) (uniqueKey & 0xFF);
                uniqueKey >>= 8;
            }

            // Encode the byte array using Base64
            String encodedString = Base64.getEncoder().encodeToString(keyBytes);

            return encodedString;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String[] decodeIpAndPort(String encodedString) {
        // Decode Base64 string to byte array
        byte[] keyBytes = Base64.getDecoder().decode(encodedString);

        // Read IP address and port number from the byte array
        long uniqueKey = 0;
        for (byte b : keyBytes) {
            uniqueKey = (uniqueKey << 8) | (b & 0xFF);
        }

        // Extract IP address and port number from the key
        int portNumber = (int) (uniqueKey & 0xFFFF);
        int ipNumber = (int) ((uniqueKey >> 16) & 0xFFFFFFFF);

        // Convert IP number to IP address
        String ipAddress = ((ipNumber >> 24) & 0xFF) + "." + ((ipNumber >> 16) & 0xFF) + "." + ((ipNumber >> 8) & 0xFF) + "." + (ipNumber & 0xFF);

        return new String[]{ipAddress, String.valueOf(portNumber)};
    }


    private void handleCommand(Command command) {
        System.out.println("Received command: " + command);
        commandQueue.add(command);
    }

    public void sendCommand(Command command) {
        if (server != null) {
            server.sendToAllTCP(command);
        } else {
            client.sendTCP(command);
        }

    }

    public static String getPublicIpAddress() {
        try {
            // Use a public IP address service (e.g., checkip.amazonaws.com)
            URL url = new URL("http://checkip.amazonaws.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method to GET
            connection.setRequestMethod("GET");

            // Get the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String ipAddress = reader.readLine();

            // Close resources
            reader.close();
            connection.disconnect();

            return ipAddress;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LinkedBlockingDeque<Command> getCommandQueue() {
        return commandQueue;
    }

    public void setCommandQueue(LinkedBlockingDeque<Command> commandQueue) {
        this.commandQueue = commandQueue;
    }

}