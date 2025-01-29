import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

class ChatClient extends Frame implements Runnable, ActionListener {
    TextField textField;
    TextArea textArea;
    Button send;
    Label label;

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Socket socket;

    Thread chat;

    ChatClient() {
        label = new Label("Enter The Message here:");
        textField = new TextField();
        textArea = new TextArea();
        send = new Button("Send");

        send.addActionListener(this);

        try {
            socket = new Socket("localhost",7687);

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
        setTitle("Client");
        setLayout(new FlowLayout());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = textField.getText();
        textArea.append("Client: "+msg+"\n");
        textField.setText("");

        try {
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();
        }
        catch (Exception E) {

        }
    }

    public static void main(String[] args) {
        new ChatClient();
    }
    public void run(){
        while(true){
            try{
                String msg = dataInputStream.readUTF();
                textArea.append("Server: "+ msg +"\n");
            }
            catch (Exception e){

            }
        }
    }
}

