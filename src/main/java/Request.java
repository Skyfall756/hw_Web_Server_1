public class Request {

    private String methodRequest;
    private String path;
    private String body;

    public Request (String request) {
        var parts = request.split("\r\n\r\n");
        var headers = parts[0].split("\r\n");
        var partsRequestLine = headers[0].split(" ");
        if (partsRequestLine.length == 3) {
            this.methodRequest = partsRequestLine[0];
            this.path = partsRequestLine[1];
        } else {
            this.methodRequest = null;
            this.path = null;
        }
        this.body = parts.length > 1 ? parts[1] : null;
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

}
