package com.example.messenger;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Server(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();//server acik mi diye kontrol ediyor
            //sockete yazma-okuma islemleri:
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (IOException e){
            System.out.println("Error!");
            e.printStackTrace();
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void sendMessageToClient(String messageToClient){//mesaji client a gonderme islemi
        try{
            bufferedWriter.write(messageToClient);//sockete mesaji yazar
            bufferedWriter.newLine();//yeni satır
            bufferedWriter.flush();//yeni bir mesaj icin onbellegi bosaltir
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error");
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void receiveMessageFromClient(VBox vBox){
        //client in mesaji almasi islemi
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        String messsageFromClient = bufferedReader.readLine();//socketten mesaji okur ve messsageFromClient aktarir
                        Controller.addLabel(messsageFromClient, vBox);//vbox icine messsageFromClient mesajini ekler
                    }catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Error receiving message from client");
                        closeEverything(socket,bufferedReader,bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if (bufferedReader !=null){
                bufferedReader.close();
            }
            if (bufferedWriter !=null){
                bufferedWriter.close();
            }
            if (socket !=null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}