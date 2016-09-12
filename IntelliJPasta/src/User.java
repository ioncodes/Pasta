import com.intellij.openapi.ui.Messages;

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by LucaMarcelli on 12.09.2016.
 */
public class User {
    private static String accessToken = "";

    public static String getAccessToken() {
        return accessToken;
    }

    public static  void setAccessToken(String accessToken) {
        User.accessToken = accessToken;
    }

    private static void createUser(File file) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String guid = java.util.UUID.randomUUID().toString();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(guid.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();
        String id = String.format("%064x", new java.math.BigInteger(1, digest));
        String accessToken = null;
        try {
            accessToken = Helpers.sendPost("http://www.ioncodes.com/Pasta/createtoken.php", "id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        User.setAccessToken(accessToken);
        try(  PrintWriter out = new PrintWriter(file.getPath())  ){
            out.println( accessToken );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void logIn() throws IOException, NoSuchAlgorithmException {
        String folder = System.getenv("APPDATA");
        File file = new File(folder + "/pasta.ini");
        if(file.exists()) {
            try {
                String token = Files.readAllLines(file.toPath()).toString();
                if(token != "") {
                    setAccessToken(token);
                } else {
                    createUser(file);
                    setAccessToken(Files.readAllLines(file.toPath()).toString());
                }
            } catch (IOException e) {
                createUser(file);
                setAccessToken(Files.readAllLines(file.toPath()).toString());
            }
        } else {
            createUser(file);
            setAccessToken(Files.readAllLines(file.toPath()).toString());
        }
    }
}
