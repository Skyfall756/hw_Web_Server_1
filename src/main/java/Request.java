import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
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

    public List<NameValuePair> getQueryParam(String name) {
        return queryParams.stream()
                .filter(nameValuePair -> nameValuePair.getName().equals(name))
                .collect(Collectors.toList());
    }
}
