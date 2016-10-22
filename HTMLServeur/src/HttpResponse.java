
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by hugueswattez on 15/10/2016.
 */
public class HttpResponse {
    private HttpRequest httpRequest;

    /**
     * Permits to respond of the http request
     * @param hr
     */
    public HttpResponse(HttpRequest hr) {
        this.httpRequest = hr;
    }

    /**
     * Create the header and the content of the response
     * @param out Buffer contains the data of the http request return
     * @throws IOException
     */
    public void respond(DataOutputStream out) throws IOException {
        int code = httpRequest.getReturnCodeInteger();

        out.writeBytes("HTTP/1.0 " + code + " " + httpRequest.getReturnCode() + "\r\n");

        switch(httpRequest.getMethod()){
            case HEAD:
                break;
            case GET:
                out.writeBytes("Content-Type: " + httpRequest.getExtension() + "\r\n");
                out.writeBytes("Content-Length: " + (httpRequest.getFileBytes().length+1) + "\r\n");
                out.writeBytes("\r\n\r\n");
                out.write(httpRequest.getFileBytes());
                break;
            case POST:
                break;
            case DELETE:
                break;
            case PUT:
                break;
            case OPTIONS:
                break;
            case TRACE:
                break;
        }
    }
}
