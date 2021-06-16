package com.atguigu.controller;


import com.atguigu.yygh.cmn.MongoUser;
import com.atguigu.yygh.cmn.ServiceCmnApplication;
import com.atguigu.yygh.cmn.TestPO;
import com.atguigu.yygh.cmn.mapper.TestMapper;
import com.mongodb.client.result.UpdateResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest(classes = ServiceCmnApplication.class ,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestController {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void test(){
        ArrayList<TestPO> testPOS = new ArrayList<>();
        int i =5000;

        for (int j = 0; j < i; j++) {
            TestPO testPO = new TestPO();
            testPO.setName("名字"+j);
            testPO.setSex("男");
            testPOS.add(testPO);
        }


        long l1 = System.currentTimeMillis();
        sessionMethod(testPOS);
        long l2 = System.currentTimeMillis();
        long l3 = System.currentTimeMillis();
        batchMethod(testPOS);
        long l4 = System.currentTimeMillis();
        long l5 = System.currentTimeMillis();
        xmlMethod(testPOS);
        long l6 = System.currentTimeMillis();
        System.out.println(l2-l1);
        System.out.println(l4-l3);
        System.out.println(l6-l5);
    }

    @Transactional
    public void sessionMethod(List<TestPO> list){
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        TestMapper mapper = sqlSession.getMapper(TestMapper.class);
        for (TestPO testPO : list) {
            int insert = mapper.insert(testPO);
        }
        sqlSession.commit();

    }

    @Transactional
    public void batchMethod(List<TestPO> list){
        for (TestPO testPO : list) {
            int insert = testMapper.insert(testPO);
        }
    }

    @Transactional
    public void xmlMethod(List<TestPO> list){
        testMapper.batchInsert(list);
    }


    @Test
    public void mongo1(){
        MongoUser mongoUser = new MongoUser();
        mongoUser.setName("test");
        mongoUser.setSex("nan");
//        MongoUser insert = mongoTemplate.insert(mongoUser);
//        System.out.println(insert);

        Query query = new Query();
//        query.addCriteria(Criteria.where("name").is("test").and("sex").is("nan"));

//        query.addCriteria(Criteria.where("name").regex("^.*es.*$"));
        long count = mongoTemplate.count(new Query(),"user");
        System.out.println(count);

        int pageNum = 1;
        int pageSize = 2;
        List<MongoUser> mongoUsers = mongoTemplate.find(query.skip((pageNum-1)*pageSize).limit(pageSize), MongoUser.class);
        for (MongoUser user : mongoUsers) {
            System.out.println(user);
        }
    }

    @Test
    public void mongo2(){
        Update update = new Update();
        update.set("sex","man");
        UpdateResult id = mongoTemplate.upsert(new Query(Criteria.where("_id").is("60c960378cad0721699d4850")), update, MongoUser.class);
        long modifiedCount = id.getModifiedCount();
        System.out.println(modifiedCount);

    }

    @Test
    public void mongo3(){

    }

}
