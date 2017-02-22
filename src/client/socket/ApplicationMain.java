package client.socket;

import messages.Message;
import messages.messageboard.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ApplicationMain {

    private static final String SERVER = "localhost";
    private static final int PORT = 3333;

    public static void main(String[] args){
        ObjectInputStream input = null;
        ObjectOutputStream output = null;
        try(Socket client = new Socket(SERVER, PORT)){
            System.out.println("Connected to server: " + client.getRemoteSocketAddress());
            input = new ObjectInputStream((client.getInputStream()));
            output = new ObjectOutputStream(client.getOutputStream());

            new Thread(new MessageHandler(input)).start();

            ApplicationMain app = new ApplicationMain();
            System.out.println("Ready to send commands!");
            app.processUserInput(output);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try {
                if(input != null) input.close();
            }
            catch(IOException ioe){
                System.err.println("Error during inputstream closing!");
            }
            try {
                if(output != null) output.close();
            }
            catch(IOException ioe) {
                System.err.println("Error during outputstream closing!");
            }
        }
    }

    private void send(Message msg, ObjectOutputStream output) throws IOException {
        output.writeObject(msg);
        output.flush();
    }

    private void processUserInput(ObjectOutputStream output) throws IOException {
        Scanner in = new Scanner(System.in);
        while(true){
            String cmd = in.nextLine();
            if(cmd.startsWith("QUIT")){
                send(new CloseMessage(), output);
                break;
            }
            else if(cmd.startsWith("ADD")){
                System.out.print("\tAuthor: ");
                String author = in.nextLine();
                System.out.print("\tMessage: ");
                String message = in.nextLine();
                AddMessage add = new AddMessage();
                BoardMessage bm = new BoardMessage();
                bm.setAuthor(author);
                bm.setMessage(message);
                add.setMessage(bm);
                send(add, output);
            }
            else if(cmd.startsWith("LIST")){
                ListMessage list = new ListMessage();
                send(list, output);
            }
            else{
                System.out.println("Unsupported operation!");
            }
        }
    }

    private static class MessageHandler implements Runnable {

        private final ObjectInputStream input;

        MessageHandler(ObjectInputStream input){
            this.input = input;
        }

        @Override
        public void run(){
            while(true){
                try {
                    Message msg = (Message)input.readObject();
                    processMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        private void processMessage(Message msg){
            if(msg instanceof AckMessage){
                System.out.println("Operation succeded!");
            }
            else if(msg instanceof ErrorMessage){
                System.out.println("Error: " + ((ErrorMessage)msg).getErrorMsg());
            }
            else if(msg instanceof ListMessage){
                System.out.println("Messageboard content:");
                ((ListMessage)msg).getBoardMessages().forEach(m -> {
                    System.out.println("\tAuthor: " + m.getAuthor() + " - Message: " + m.getMessage());
                });
            }
        }

    }

}
