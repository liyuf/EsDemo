package com.example.esdemo;

import com.example.esdemo.pojo.User;
import com.example.esdemo.service.UserService;
import com.example.esdemo.utils.JsonUtil;
import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.esdemo.constants.ESconstants.*;

/**
 * @author :liyufei
 * @Decirption : todo
 * @date :2020/8/9 17:04
 */

public class ESTest {

    public UserService userService=new UserService();

    public  RestHighLevelClient esClient;
    @Before
    public void ready()throws Exception{
        //建立ES连接
       esClient = new RestHighLevelClient(RestClient.builder(new HttpHost(HOSTNAME, PORT)));

    }

//    创建索引库
    @Test
    public void createIndicesTest() throws Exception{
        //创建请求对象,并制定索引库名称
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(INDEXNAME);
        //指定settings配置
        /*
        * 暂无分片分区
        * */
        //指定mapping映射
        createIndexRequest.mapping(MAPPINGS, XContentType.JSON);
        //发起请求，得到响应，成功则true
        CreateIndexResponse response = esClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }


    //文档的CRUD
    //添加一个文档
    @Test
    public void createDocTest() throws Exception{
        //从数据库中查询寻文档数据
        User user = userService.findById(1L);
        //创建IndexRequest对象，并制定索引库名称
        IndexRequest indexRequest = new IndexRequest(INDEXNAME);
        //指定新增的数据id
        indexRequest.id(user.getId().toString());
        //将新增的文档数据变成JSON格式
        String json = JsonUtil.EntityToJson(user);
        //将JSON数据添加到IndexRequest中
        indexRequest.source(json,XContentType.JSON);
        //发起请求，得到结果
        esClient.index(indexRequest, RequestOptions.DEFAULT);
    }
    @Test
    public void queryDocTest() throws Exception{
        //创建getRequest对象,指定索引库名称和文档ID
        GetRequest request = new GetRequest(INDEXNAME).id("1");
        //发送请求
        GetResponse response = esClient.get(request, RequestOptions.DEFAULT);
        //解析结果
        String json = response.getSourceAsString();
        //反序列化成对象
        User user = JsonUtil.JsonToEntity(json, User.class);
        System.out.println(user);
    }

    @Test
    public void updateTest() throws Exception{
        //创建UpdateRequest对象，指定索引库名称、和id
        UpdateRequest updateRequest = new UpdateRequest(INDEXNAME,"1");
        //指定要修改的字段及属性值
        updateRequest.doc(GENDER,"女");
        //提交
        esClient.update(updateRequest,RequestOptions.DEFAULT);
    }

    @Test
    public void bulkTest() throws Exception{
        //查询数据
        List<User> list = userService.findAll();
        //创建BulkRequest
        BulkRequest bulkRequest = new BulkRequest(INDEXNAME);
        for (User user : list) {
            bulkRequest.add(new IndexRequest(INDEXNAME)
                    .id(user.getId().toString())
                    .source(JsonUtil.EntityToJson(user),XContentType.JSON));
        }
        //提交
        esClient.bulk(bulkRequest,RequestOptions.DEFAULT);

    }

    /**
     * SearchSourceBuilder对象用于搜索
     * @throws Exception
     */
    //文档搜索
    @Test
    public void queryTest() throws Exception{
        //创建SearchSourceBuilder对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //添加查询条件
        SearchSourceBuilder searchSource = searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        //创建request对象
        SearchRequest searchRequest = new SearchRequest(INDEXNAME);
        //添加搜索条件
        searchRequest.source(searchSource);
        //发起请求
        SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
        //解析结果
        SearchHits hits = response.getHits();
        //搜索总条数
        Long totalHits = hits.getTotalHits().value;
        //文档
        SearchHit[] doc = hits.getHits();
        /*List<User> userList = new ArrayList<>(doc.length);
        //获取文档内容
        for (SearchHit documentFields : doc) {
            String json = documentFields.getSourceAsString();
            //反序列化为对象
            User user = JsonUtil.JsonToEntity(json, User.class);
            userList.add(user);
        }*/
        List<User> list=Stream.of(doc).map(docFields->JsonUtil.JsonToEntity(docFields.getSourceAsString(),User.class)).collect(Collectors.toList());
        System.out.println(list);
    }
    //分词查询
    @Test
    public void querytest() throws Exception{
        //创建searchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(new MatchQueryBuilder(NOTE, "柳岩Java"));
        //创建searchRequest
        SearchRequest searchRequest = new SearchRequest(INDEXNAME).source(searchSourceBuilder);
        //查询
        SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
        //文档信息
        SearchHit[] doc = response.getHits().getHits();
        List<User> list=Stream.of(doc).map(docInfo->JsonUtil.JsonToEntity(docInfo.getSourceAsString(),User.class)).collect(Collectors.toList());
        System.out.println(list);

    }

    @Test
    public void test() throws Exception{
        //创建条件构造器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().fetchSource(new String[]{NAME}, null).query(QueryBuilders.rangeQuery(AGE).from(10).to(30));
        //创建SearchRequest对象
        SearchRequest searchRequest = new SearchRequest(INDEXNAME).source(searchSourceBuilder);
        //发送
        SearchResponse response = esClient.search(searchRequest,RequestOptions.DEFAULT);
        //解析结果
        SearchHit[] doc = response.getHits().getHits();
        //反序列化
        List<User> list=Stream.of(doc).map(docInfo->JsonUtil.JsonToEntity(docInfo.getSourceAsString(),User.class)).collect(Collectors.toList());
        System.out.println(list);
    }


    @After
    public void complete()throws Exception{
        esClient.close();
    }

}
