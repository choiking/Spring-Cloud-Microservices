package xcode.springcloud.riderclient;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.*;
import java.util.Scanner;

public class TestErrorHandler extends DefaultResponseErrorHandler {

    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        //conversion logic for decoding conversion
//        ByteArrayInputStream arrayInputStream = (ByteArrayInputStream) response.getBody();
//        Scanner scanner = new Scanner(arrayInputStream);
//        scanner.useDelimiter("\\Z");
//        String data = "";
//        if (scanner.hasNext())
//            data = scanner.next();
//        System.out.println(data);


        String body = convertStreamToString(response.getBody());

        errorHandler.handleError(response);

        System.out.println(body);

    }

    // inputStream 装换为 string
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
