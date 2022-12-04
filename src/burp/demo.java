package burp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class demo {

    public static class analyze_Json {
        public String jsons;
        public JSONObject jsonObject;
        public JSONArray jsonArray;

        public analyze_Json(String jsons) {
            this.jsons = jsons;
            this.getJsonObject();
        }

        public void getJsonObject() {
            try {
                jsonObject = JSON.parseObject(jsons);
                JsonPrase(jsonObject);
            }catch (Exception e){
                jsonArray = JSON.parseArray(jsons);
                JsonPrase(jsonArray);
            }
        }

        public void JsonPrase(Object object) {
            if (object instanceof JSONArray) {
                jsonArray = (JSONArray) object;
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonPrase(jsonArray.get(i));
                }
            } else if (object instanceof JSONObject) {
                jsonObject = (JSONObject) object;
                Iterator<Map.Entry<String, Object>> iterator = jsonObject.entrySet().iterator();
                while (iterator.hasNext()) {
//                    UUID uuid = UUID.randomUUID();
//                    String payload = uuid.toString().substring(0, 13).replace("-", ".");
                    Map.Entry<String, Object> en = iterator.next();
                    String k = en.getKey();
                    Object v = en.getValue();
                    //如果得到的是数组
                    if (v instanceof JSONArray) {
                        JSONArray objArray = (JSONArray) v;
                        JsonPrase(objArray);
                    } else if (v instanceof JSONObject) {
                        JsonPrase((JSONObject) v);
                    } else {//如果key中是其他
                        jsonObject.put(k, "wahaha--wahaha");
                    }
                }
            }
        }

        public String getResord() {
            if (jsonArray != null) {
                return jsonArray.toString();
            } else {
                return jsonObject.toString();
            }
        }
    }

    public static void main(String[] args) {
//        String json_schema1 = "{\"uri\":\"https://ec-static.net/openpay/signin.html\",\"sn\":0,\"so\":1,\"sp\":{},\"se\":{},\"srn\":-1,\"sr\":\"\",\"sro\":0,\"et\":3,\"du\":0,\"sa\":0,\"ext\":{\"sm_device_id\":\"BePXQtTPVWwHoVvzvaI91VCRYovypFFBgDAHQBj+XAGt3zAHXJ/2/PnZbEJsn9bsbpzAdDT06UU8TUIKLe5UR+Q==\"},\"id\":\"001\",\"cnid\":1,\"ab\":\"op\",\"di\":\"9602237d794b61e923a2e7360d149607\",\"ui\":0,\"ajid\":\"\",\"adid\":\"\",\"uuid\":\"\",\"si\":\"5626f0d9-a688-4488-be71-8bc6eda12be2\",\"av\":\"\",\"irn\":0,\"ts\":1669705350599,\"adt\":-1}";
//        String json_schema2 = "[{\"uri\":\"https://ec-static.net/openpay/signin.html\",\"sn\":0,\"so\":1,\"sp\":{},\"se\":{},\"srn\":-1,\"sr\":\"\",\"sro\":0,\"et\":3,\"du\":0,\"sa\":0,\"ext\":{\"sm_device_id\":\"BePXQtTPVWwHoVvzvaI91VCRYovypFFBgDAHQBj+XAGt3zAHXJ/2/PnZbEJsn9bsbpzAdDT06UU8TUIKLe5UR+Q==\"},\"id\":\"001\",\"cnid\":1,\"ab\":\"op\",\"di\":\"9602237d794b61e923a2e7360d149607\",\"ui\":0,\"ajid\":\"\",\"adid\":\"\",\"uuid\":\"\",\"si\":\"5626f0d9-a688-4488-be71-8bc6eda12be2\",\"av\":\"\",\"irn\":0,\"ts\":1669705350599,\"adt\":-1}]";
//
//        analyze_Json analyze_json = new analyze_Json(json_schema2);
//        System.out.println(analyze_json.getResord());

        String a = "7d48c612.ts";
        System.out.println(a.contains("."));

    }

}

