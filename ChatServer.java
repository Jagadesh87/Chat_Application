import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class ChatServer extends Frame implements Runnable, ActionListener {
    TextField textField;
    TextArea textArea;
    Button send;
    Label label;

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    ServerSocket serverSocket;
    Socket socket;

    Thread chat;

    ChatServer() {
        label = new Label("Enter the Messege here:");
        textField = new TextField();
        textArea = new TextArea();
        send = new Button("Send");

        send.addActionListener(this);

        try {

            serverSocket = new ServerSocket(7687);
            socket = serverSocket.accept();

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }
        catch(Exception e){

        }
        add(label);
        add(textField);
        add(textArea);
        add(send);

        chat = new Thread(this);
        chat.setDaemon(true);
        chat.start();

        setSize(520,500);
        setTitle("Server");
        setLayout(new FlowLayout());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = textField.getText();
        textArea.append("Server: "+msg+"\n");
        textField.setText("");

        try {
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();
        }
        catch (Exception ex) {

        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }
    public void run(){
        while (true){
            try{
                String msg = dataInputStream.readUTF();
                textArea.append("ChatClient: "+ msg +"\n");
            }
            catch (Exception e){

            }
        }
    }
}

