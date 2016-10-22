import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hugueswattez on 15/10/2016.
 */
public class HttpServer {
    private ServerSocket serverSocket;
    private String path;

    /**
     * Create a new http server
     * @param path Absolute path to files
     * @param port Port used to connect ServerSocket
     */
    public HttpServer(String path, int port) {
        try {
            this.path = path;
            this.serverSocket = new ServerSocket(port);

            this.lunch();

            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waiting new clients
     * @throws IOException
     */
    public void lunch() throws IOException {
        Socket socket;

        while(true){
            socket = this.serverSocket.accept();
            (new HttpClientServer(this.path, socket)).start();
        }
    }
}
