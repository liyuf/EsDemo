package com.example.esdemo.constants;

/**
 * @author :liyufei
 * @Decirption : todo
 * @date :2020/8/9 17:29
 */
public class ESconstants {
    public final static String HOSTNAME="localhost";
    public final static int PORT=9200;
    public final static String INDEXNAME="user";

    //配置文件
    public final static String RESOURCE="mybatis-config.xml";

    //es映射
    public final static String MAPPINGS=" {\n" +
            "    \"properties\": {\n" +
            "      \"id\":{\n" +
            "        \"type\": \"long\"\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"age\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"gender\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"note\":{\n" +
            "        \"type\":\"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }";

    public final static String GENDER="gender";
    public final static String NAME="name";
    public final static String AGE="age";
    public final static String NOTE="note";

}
