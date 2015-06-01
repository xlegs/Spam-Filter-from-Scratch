import java.io.*;
import java.util.HashMap;

public class ParseTest {
    public static void main(String args[]) {
        BufferedReader br =
            new BufferedReader(new InputStreamReader(System.in));
        Parser p = new Parser(br);
        while (true) {
            Message m = p.parseMessage();
            String subj = m.headers.get("Subject");
            System.out.println(subj);
        }
    }
}
