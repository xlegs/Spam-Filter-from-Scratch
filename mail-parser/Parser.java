import java.io.*;
import java.util.HashMap;

public class Parser {
    BufferedReader stream;
    public Parser(BufferedReader br) {
        stream = br;
    }

    // parse one message from the stream
    // return null if EOF
    public Message parseMessage() {
        Message m = new Message();
        boolean line_read = false;;
        // header stage: read line until blank line
        String key="";
        String value;
        while (true) {
            String line;
            try {
                line = stream.readLine();
            } catch (Exception e) { return null; }
            if (line==null) {
                return null;
            }
            if (line.equals("")) {
                break;
            }
            // if the line starts with ' ' or '\t',
            // consider it a continuation line
            if (line.charAt(0)==' ' || line.charAt(0)=='\t')  {
                // update the value of this key
                value=m.headers.get(key);
                value=value+'\n'+line;
                m.headers.put(key, value);
                continue;
            }
            String[] pair = line.split(":", 2);
            if (pair.length != 2) {
                // error, so just skip this
                continue;
            }
            key=pair[0];
            value=pair[1];
            m.headers.put(key, value);
        }
        // blank line separates headers from message.
        // now read rest of message until '\0'
        String content="";
        while (true) {
            int c;
            try {
                c = stream.read();
            } catch (Exception e) { return null; }
        if (c==-1 || c==0) {
                break; // EOF or null byte
            }
            content=content+(char)c;
        }
        m.content=content;
        return m;
    }
}
