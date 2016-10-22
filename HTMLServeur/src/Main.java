/**
 * Created by hugueswattez on 15/10/2016.
 */
public class Main {
    /**
     * Instance of HttpServer object
     * @param args Contains the path and the port number of new server
     */
    public static void main(String[] args){
        if(args.length != 2){
            System.err.println("You have to wright the great command : java Main <path> <portNumber>");
            System.exit(1);
        }

        new HttpServer(args[0], Integer.parseInt(args[1]));
    }
}
