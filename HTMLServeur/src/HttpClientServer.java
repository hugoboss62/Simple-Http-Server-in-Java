import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HttpClientServer extends Thread {
    private Socket socket;
    private BufferedReader in;
    private DataOutputStream out;
    private String path;
    private HttpRequest httpRequest;

    /**
     * Creating of new Thread for the client communication
     * @param path
     * @param socket socket used to communicate with the client
     */
    public HttpClientServer(String path, Socket socket) {
        this.path = path;
        this.socket = socket;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(this.socket.getOutputStream());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read request, execute and send a response to the client
     */
    public void run() {
        try {
            this.httpRequest = HttpRequest.read(this.path, this.in);
            this.httpRequest.findResources();
            (new HttpResponse(this.httpRequest)).respond(this.out);

            this.closeSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permits to close properly the socket
     */
    public void closeSocket(){
        try {
            if(socket.isClosed()) return;

            out.flush();
            System.out.println(this);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = "[" + sdf.format(cal.getTime()) + "] ";

        return time +
                this.socket.getInetAddress().toString() + " " +
                this.httpRequest;
    }
}
