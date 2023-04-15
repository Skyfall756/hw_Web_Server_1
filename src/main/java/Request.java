import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Request {

    private final String methodRequest;
    private final String[] requestLine;
    private final String path;
    private final List<String> headers;
    private String body;
    private List<NameValuePair> queryParams;
    private List<String> postParams;

    public Request(String[] requestLine, List<String> headers) {
        this.requestLine = requestLine;
        this.methodRequest = requestLine[0];
        this.path = requestLine[1];
        this.headers = headers;
        this.queryParams = URLEncodedUtils.parse(URI.create(path), UTF_8);
    }

    public Request(String[] requestLine, List<String> headers, String body) {
        this.requestLine = requestLine;
        this.methodRequest = requestLine[0];
        this.path = requestLine[1];
        this.headers = headers;
        this.body = body;
        this.queryParams = URLEncodedUtils.parse(path, UTF_8);
    }


    public String getMethodRequest() {
        return methodRequest;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }

    public String[] getRequestLine() {
        return requestLine;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<NameValuePair> getQueryParams() {
        return queryParams;
    }

    public List<String> getQueryParam(String name) {
        return queryParams.stream()
                .filter(nameValuePair -> nameValuePair.getName().equals(name))
                .map(NameValuePair::getValue)
                .collect(Collectors.toList());
    }

    //x-www-form-urlencoded
    public List<String> getPostParams() {
        return Arrays.asList(body.split("&"));
    }

    public List<String> getPostParam(String name) {
        List<String> postParams = getPostParams();
        return postParams.stream()
                .filter(s -> s.startsWith(name + "="))
                .map(s -> s.substring(s.indexOf("=") + 1))
                .collect(Collectors.toList());
    }
}
