import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hugueswattez on 15/10/2016.
 */
public class HttpMessage {
    private Map<String, String> hMap = new HashMap<>();

    /**
     * Parse the content of the HTML request and save it into a HashMap
     * @param in The html Buffer
     * @return a new instance of HttpMessage containing the HTML request informations
     * @throws IOException
     */
    public static HttpMessage read(BufferedReader in) throws IOException {
        String line;
        String[] tab;
        HttpMessage httpMessage = new HttpMessage();

        while ((line = in.readLine()) != null && !"".equals(line)) {
            tab=line.split(":", 2);
            httpMessage.addMessage(tab[0], tab[1]);
        }

        return httpMessage;
    }

    private void addMessage(String key, String value){
        this.hMap.put(key, value);
    }
}
