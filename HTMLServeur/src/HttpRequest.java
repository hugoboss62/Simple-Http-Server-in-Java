
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hugueswattez on 15/10/2016.
 */
public class HttpRequest {
    private static final Map<String, String> extensions = new HashMap<String, String>() {{
        put("html", "text/html");
        put("css", "text/css");
        put("woff", "text/woff");
        put("woff2", "text/woff2");
        put("ttf", "text/ttf");
        put("js", "application/js");
        put("jpg", "image/jpg");
        put("jpeg", "image/jpeg");
        put("png", "image/png");
        put("ico", "image/ico");
    }};
    private static final String HTTP = "HTTP/1.1";
    private static final String page403 = "403.html";
    private static final String page404 = "404.html";
    private static final String page405 = "405.html";

    private String path;
    private Method method;
    private String resources;
    private HttpMessage httpMessage;
    private byte[] fileBytes = null;
    private ReturnCode returnCode = ReturnCode.OK;
    private String extension;

    /**
     * Contains all the informations of the Http request
     * @param path Absolute path to the files
     * @param method Header method to know the action of request
     * @param resources Resources to return to the user
     * @param httpMessage Object containing content-length...
     * @param HTTPversion The http version which must have to be equals to static String HTTP
     */
    public HttpRequest(String path, Method method, String resources, HttpMessage httpMessage, String HTTPversion) {
        this.path = path;
        if ((this.method = method) == null || !HTTP.equals(HTTPversion))
            this.unauthorizedMethodPage();
        this.resources = resources.substring(1);
        this.httpMessage = httpMessage;
    }

    /**
     * Static method which reads the request and creates a new instance of HttpRequest
     * @param path Absolute path to the files
     * @param in Buffer contains all the informations of request
     * @return new Instance of HttpRequest containing all the informations of request
     * @throws IOException
     */
    public static HttpRequest read(String path, BufferedReader in) throws IOException {
        String ressource, line = in.readLine();
        String[] tab = line.split(" ");
        Method method;
        HttpMessage httpMessage;

        switch (tab[0]) {
            case "HEAD":
                method = Method.HEAD;
                break;
            case "GET":
                method = Method.GET;
                break;
            case "POST":
                method = Method.POST;
                break;
            case "DELETE":
                method = Method.DELETE;
                break;
            case "PUT":
                method = Method.PUT;
                break;
            case "OPTIONS":
                method = Method.OPTIONS;
                break;
            case "TRACE":
                method = Method.TRACE;
                break;
            default:
                method=null;
        }

        ressource = tab[1];
        httpMessage = HttpMessage.read(in);

        return new HttpRequest(path, method, ressource, httpMessage, tab[2]);
    }

    /**
     * Find the resources and test it (if exists, is a file or directory...)
     */
    public void findResources() {
        File file = new File(this.path+this.resources);

        if (!file.exists())
            this.unknowPage();
        else if (file.isFile())
            this.createResourcesFile();
        else
            this.createResourcesRepository(file);
    }

    /**
     * List all the elements of the directory as html document
     * @param dir
     */
    private void createResourcesRepository(File dir) {
        File[] list = dir.listFiles();
        String fileOut = "";

        for(File f : list)
            fileOut += "<a href='" + f.getName() + "'>" + f.getName() + "</a><br>";

        this.fileBytes = fileOut.getBytes();
        extension = HttpRequest.extensions.get("html");
    }

    /**
     * Read the content of file to send
     */
    private void createResourcesFile(){
        String file = this.resources;
        extension = file.substring(file.lastIndexOf(".") + 1);

        if ((extension = HttpRequest.extensions.get(extension)) == null) {
            this.unknowPage();
            return;
        }

        try {
            InputStream is = new FileInputStream(this.path+file);
            this.fileBytes = new byte[is.available()];
            is.read(this.fileBytes);
        } catch (IOException e) {
            this.unknowPage();
        }
    }

    /**
     * Load the 404 page
     */
    private void unknowPage(){
        this.resources = page404;
        this.returnCode = ReturnCode.NOT_FOUND;
        this.findResources();
    }

    /**
     * Load the 405 page
     */
    private void unauthorizedMethodPage(){
        this.resources = page405;
        this.returnCode = ReturnCode.UNAUTHORIZED_METHOD;
    }

    /**
     * Load the 403 page
     */
    private void forbiddenPage(){
        this.resources = page403;
        this.returnCode = ReturnCode.UNAUTHORIZED_METHOD;
    }

    public Method getMethod() {
        return method;
    }

    public String getResources() {
        return resources;
    }

    public HttpMessage getHttpMessage() {
        return httpMessage;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public String getExtension() {
        return extension;
    }

    @Override
    public String toString() {
        return method + " "
                + '/' + resources + ' ' +
                this.getReturnCodeInteger();
    }

    public int getReturnCodeInteger() {
        switch(returnCode){
            case OK:
                return 200;
            case FORBIDDEN:
                return 403;
            case NOT_FOUND:
                return 404;
            case UNAUTHORIZED_METHOD:
                return 405;
        }
        return 0;
    }

    public ReturnCode getReturnCode() {
        return returnCode;
    }
}
