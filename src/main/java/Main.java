import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        final var server = new Server();
        server.addHandler("GET", "/messages", (request, out) -> {
            String message = "Hello from /messages";
            try {
                out.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + "text/plain" + "\r\n" +
                                "Content-Length: " + message.length() + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.write(message.getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        server.addHandler("GET", "/index.html", (request, out) -> {
            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);
            final var length = Files.size(filePath);
            try {
                out.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + mimeType + "\r\n" +
                                "Content-Length: " + length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                Files.copy(filePath, out);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        server.listen(8888);
    }

}
