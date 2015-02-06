/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studypointexercise1;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Lars Mortensen
 */
public class StudyPointExercise1 {

    static int port = 8080;
    static String contentFolder = "public/";
    static String ip = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(ip, port), 0);
        server.createContext("/welcome", new RequestHandler());
        server.createContext("/headers", new HeaderHandler());
        server.createContext("/pages", new SimpleFileHandler());
        server.setExecutor(null); // Use the default executor
        server.start();
        System.out.println("Server started, listening on port: " + port);
    }

    static class RequestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<title>My fancy Web Site</title>\n");
            sb.append("<meta charset='UTF-8'>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");

            sb.append("<h1>Welcome to my very first almost home made Web Server :-)</h1>");
            sb.append("<br>").append("URI ").append(he.getRequestURI());

            sb.append("</body>\n");
            sb.append("</html>\n");

            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "text/html");

            he.sendResponseHeaders(200, sb.length());
            try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                pw.print(sb.toString()); //What happens if we use a println instead of print --> Explain
            }
        }
    }

      static class SimpleFileHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            File file = new File(contentFolder + "index.html");
            byte[] bytesToSend = new byte[(int) file.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
            } catch (IOException ie) {
                System.out.println(ie);
            }
            he.sendResponseHeaders(200, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }
           
        }
    }
    
    static class HeaderHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            StringBuilder sb = new StringBuilder();

            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<title>My fancy Web Site</title>\n");
            sb.append("<meta charset='UTF-8'>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");

            sb.append("<table border=1 style=width:100%>");
            sb.append("<tr>");

            sb.append("<th align:center>Header</th><th align:center>Value</th>");

            sb.append("</tr>");

            sb.append("<tr>");

            for (String s : he.getRequestHeaders().keySet()) {

                sb.append("<td>").append(s).append("</td>");

                List<String> list = he.getRequestHeaders().get(s);

                sb.append("<td>").append(list).append("</td>");

                sb.append("</tr>");
            }

            sb.append("</table>");


            sb.append("</body>\n");
            sb.append("</html>\n");

            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "text/html");

            he.sendResponseHeaders(200, sb.length());
            try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                pw.print(sb.toString()); //What happens if we use a println instead of print --> Explain
            }
        }

    }
}
