package burp;

import java.io.PrintWriter;
import java.net.URL;
import java.util.*;

public class BurpExtender implements IBurpExtender, IHttpListener {
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private PrintWriter stdout;
    private PrintWriter stderr;
    private String ExtenderName = "burp_ext demo";
    private String[] exts = {".js",".jpg",".png",".jpeg",".svg",".mp4",".css",".mp3",".ico",".woff",".woff2"};
    public static String result = null;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(), true);
        this.callbacks = callbacks;
        helpers = callbacks.getHelpers();
        callbacks.setExtensionName(ExtenderName);
        callbacks.registerHttpListener(this);
    }

    public static String Parse(String txt) {
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
                Parse(result);
            }
        }else{
            result = txt;
        }
        return result;
    }

    public String autoMarker(String dest,Map<String,String> RRR){
        if(!RRR.isEmpty()){
            Iterator<Map.Entry<String, String>> iterator = RRR.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String k = entry.getKey().contains(".") ? entry.getKey().substring(entry.getKey().indexOf(".")+1) : entry.getKey();
                String v  = entry.getValue() ;

                if(v.equals("")){
                    dest = dest.replace("\""+k+"\""+":"+"\"\"","\""+k+"\""+":"+"\"§§\"");
                    dest = dest.replace("<"+k+"></"+k+">","<"+k+">§"+"§</"+k+">"); //xml format
                }else{
                    dest = dest.replace(k+"="+v,k+"="+"§"+v+"§");
                    dest = dest.replace("\""+k+"\""+":"+v,"\""+k+"\""+":"+"§"+v+"§");
                    dest = dest.replace("\""+k+"\""+":"+"\""+v+"\"","\""+k+"\""+":"+"\"§"+v+"§\"");
                    dest = dest.replace("<"+k+">"+v+"</"+k+">","<"+k+">§"+v+"§</"+k+">"); // xml format
                }
            }
        }
        return dest;
    }


    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
        if (toolFlag == IBurpExtenderCallbacks.TOOL_REPEATER) {
            if (messageIsRequest) { //对请求包进行处理
                IRequestInfo analyzeRequest = helpers.analyzeRequest(messageInfo);//messageInfo是请求/响应综合体
                URL url = analyzeRequest.getUrl();
                for (String ext : exts) { //特定结尾文件不处理
                    if (url.toString().endsWith(ext)) {
                        return;
                    }
                }

                UUID uuid = UUID.randomUUID();
                String prefix = uuid.toString().substring(0, 9).replace("-",".");
                List<IParameter> paraList = analyzeRequest.getParameters();//参数包括url、body、cookie、json、xml等格式参数

                Map<String,String> RRR = new HashMap<String,String>();

                for (IParameter para : paraList) {
                    if(para.getType() != IParameter.PARAM_COOKIE){
                        if(RRR.containsKey(para.getName())){
                            RRR.put(prefix+para.getName(),para.getValue());
                        }else{
                            RRR.put(para.getName(),para.getValue());
                        }
                    }
                }
                String dest = helpers.bytesToString(messageInfo.getRequest()).toString();

                String news = autoMarker(dest, RRR);
                stdout.println(news);
            }
        }
    }
}