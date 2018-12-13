package cc.translate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ReadTextFileEx5 {

    public static void main(String[] args) throws IOException {

        String fileName = "D:\\Users\\pwcraradmin\\.cc\\caches\\8517fc619f687f35e619a3734903a653-zh_CN.json";
        BufferedReader br = null;

        try {
            
            String line = null;
            StringBuilder sb = new StringBuilder();

  
             br=new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            
            System.out.println(sb);

        } finally {

            if (br != null) {
                br.close();
            }
        }
    }
}
