package com.gdx.pokemon.udp;

import com.gdx.pokemon.battle.BattleOnline;
import com.gdx.pokemon.battle.OpponentTrainer;
import com.gdx.pokemon.screen.GameScreen;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class UDP_client {
    private static volatile UDP_client instance = null;
    private DatagramSocket socket;
    private Queue<String> messageQueue = new ArrayDeque<>();
    private int generateRandomPort() {
        Random random = new Random();
        return random.nextInt(65535 - 49152) + 49152;
    }

    private boolean isPortInUse(int port) {
        try (DatagramSocket ignored = new DatagramSocket(port)) {
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private void listeningFromServer(){
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String response = new String(packet.getData(), 0, packet.getLength());
                messageQueue.add(response);
            }
        } catch (Exception ignored) {
        }
    }

    private void processResponse(){
        if(messageQueue.isEmpty()){
            return;
        }
        String response = messageQueue.poll();

        String[] parts = response.split(" ");
        if(Objects.equals(parts[0], "newplayer")){
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            String playerID = parts[4];

            GameScreen.getInstance().addNewPlayer(x, y, playerID, playerID);
        }
        else if (Objects.equals(parts[0], "where")){
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int keyCodeUp = Integer.parseInt(parts[3]);
            int keyCodeDown = Integer.parseInt(parts[4]);
            String playerID = parts[5];
            GameScreen.getInstance().updatePlayer(x, y, keyCodeUp, keyCodeDown, playerID);
        }
        else if(Objects.equals(parts[0], "ask")){
            System.out.println(response);
            String playerID = parts[1];
            GameScreen.getInstance().battle(playerID);
        }
        else if(Objects.equals(parts[0], "responsebattle")){
            String playerID = parts[1];
            String responseBattle = parts[2];
            GameScreen.getInstance().responseBattle(playerID, responseBattle);
        }
        else if(Objects.equals(parts[0], "pokemon")){
            String opponentID = parts[1];
            String name = parts[2];
            int level = Integer.parseInt(parts[3]);
            int exp = Integer.parseInt(parts[4]);
            int hp = Integer.parseInt(parts[5]);
            int attack = Integer.parseInt(parts[6]);
            int defense = Integer.parseInt(parts[7]);
            int special_attack = Integer.parseInt(parts[8]);
            int special_defense = Integer.parseInt(parts[9]);
            int speed = Integer.parseInt(parts[10]);

            OpponentTrainer.getInstance().addPokemon(name, level, exp, hp, attack, defense, special_attack, special_defense, speed);

            if (OpponentTrainer.getInstance().getPlayerTrainer().getTeamSize() == 3){
                GameScreen.getInstance().startBattle(opponentID);
            }
        }
        else if (Objects.equals(parts[0], "input")){
            String opponentAddr = parts[1];
            int input = Integer.parseInt(parts[2]);
            BattleOnline battleOnline = BattleOnline.getInstance();
            battleOnline.setInput(input);
        }
        else if (Objects.equals(parts[0], "damage")){
            String opponentAddr = parts[1];
            int damage = Integer.parseInt(parts[2]);
            System.out.println("Damage: " + damage);
            BattleOnline battleOnline = BattleOnline.getInstance();
            battleOnline.setDamage(damage);
        }
    }


    public void sendMessage(String message) {
        try {
            InetAddress address = InetAddress.getByName("localhost");
            int port = 1200;
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), address, port);
            socket.send(packet);
        } catch (Exception ignored) {
        }
    }

    private UDP_client() {
        // Exists only to defeat instantiation
        int port;
        do {
            port = generateRandomPort();
        } while (isPortInUse(port));

        try {
            socket = new DatagramSocket(port);
            CompletableFuture.runAsync(this::listeningFromServer);
            CompletableFuture.runAsync(() -> {
                while (true) {
                    if (!messageQueue.isEmpty()) {
                        processResponse();
                    } else {
                        try {
                            Thread.sleep(1); // Sleep for 1 second
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt(); // Restore interrupted status
                        }
                    }
                }
            });
        } catch (Exception ignored) {
        }
    }

    public static UDP_client getInstance() {
        if (instance == null) {
            synchronized (UDP_client.class) {
                if (instance == null) {
                    instance = new UDP_client();
                }
            }
        }
        return instance;
    }

    public int getPort() {
        return socket.getLocalPort();
    }

}
