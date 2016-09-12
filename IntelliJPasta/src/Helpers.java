import com.intellij.util.io.DataOutputStream;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by LucaMarcelli on 12.09.2016.
 */
public class Helpers {
    public static String sendPost(String url, String post) throws Exception {
        URL obj = new URL(url.replace('[', ' ').replace(']', ' ').replaceAll("\\s+",""));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(post);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebpage(String url) throws MalformedURLException {
        URL url1 = new URL(url);
        try {
            openWebpage(url1.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
