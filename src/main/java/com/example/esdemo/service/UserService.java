package com.example.esdemo.service;

import com.example.esdemo.mapper.UserMapper;
import com.example.esdemo.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.example.esdemo.constants.ESconstants.RESOURCE;

public class UserService {
    private UserMapper userMapper;

    public UserService(){
        try {
            //读取配置文件
            String resource=RESOURCE;
            InputStream resourceAsStream = Resources.getResourceAsStream(resource);
            //获取连接工厂
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
            //获取连接
            SqlSession sqlSession = sqlSessionFactory.openSession();
            //获取对象
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //赋值对象
            userMapper=mapper;
        } catch (IOException e) {
            throw new RuntimeException("请添加mybatis配置文件",e);
        }
    }

    public User findById(Long id){
        //调用mapper
        return userMapper.findById(id);
    }

    public List<User> findAll(){
        return userMapper.findAll();
    }
}
