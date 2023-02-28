import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {


    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers =
            new ConcurrentHashMap<>();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(64);


    public void listen(int port) {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    final var socket = serverSocket.accept();
                    threadPool.execute(() -> connection(socket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connection(Socket socket) {
        try (final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             final var out = new BufferedOutputStream(socket.getOutputStream())) {
            String requestLine = in.readLine(); // = getRequest(in); ??

            Request request = new Request(requestLine);
            Handler handler = findHandler(request);
            if (!(handler == null)) {
                handler.handle(request, out);
                socket.close();
            } else {
                sendNotFound(out);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getRequest(BufferedReader in) {
        StringBuilder sb = new StringBuilder();
        int c;
        try {
            while ((c = in.read()) != -1) {
                sb.append((char) c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void addHandler(String method, String path, Handler handler) {
        if (handlers.containsKey(method)) {
            handlers.get(method).put(path, handler);
        } else {
            handlers.put(method, new ConcurrentHashMap<>());
            handlers.get(method).put(path, handler);
        }

    }

    public Handler findHandler(Request request) {
        Handler handler = null;
        if (handlers.containsKey(request.getMethodRequest()) &&
                handlers.get(request.getMethodRequest()).containsKey(request.getPath())) {
            handler = handlers.get(request.getMethodRequest()).get(request.getPath());
        }
        return handler;
    }

    public void sendNotFound(BufferedOutputStream out) {
        try {
            out.write((
                    "HTTP/1.1 404 Not Found\r\n" +
                            "Content-Length: 0\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}