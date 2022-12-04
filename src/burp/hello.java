package burp;

import java.util.UUID;

public class hello {

    public static String result = null;

    public static String parse(String txt) {
        int start = 0;
        int end = 0;
        int tmp = 1;

        UUID uuid = UUID.randomUUID();
        String payload = uuid.toString().substring(0,13).replace("-",".");

        if(txt.contains("§")){
            for (int i = 0; i < txt.length(); i++) {
                if (txt.charAt(i) == new Character('§')) {
                    if(tmp ==1){
                        start = i;
                    }else {
                        end = i;
                        break;
                    }
                    tmp++;
                }
            }

            StringBuilder sb = new StringBuilder(txt).replace(start,end+1,payload);
            start = 0 ;end =0;
            result = sb.toString();
            if(result.contains("§")){
                parse(result);
            }
        }else{
            result = txt;
        }
        return result;
    }

    public static void main(String[] args) {
        String e = "{\"appId\":\"§io.installment§\",\"organization\":\"§mkEM7Sm6QY93xu9gjRA0§\",\"ep\":\"§YLzY§\"}";
        String b = "{\"appId\":\"io.installment\",\"organization\":\"mkEM7Sm6QY93xu9gjRA0\",\"ep\":\"YLzY\"}";
        System.out.println(parse(b));
    }
}