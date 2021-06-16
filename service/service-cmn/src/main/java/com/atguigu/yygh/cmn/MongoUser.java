package com.atguigu.yygh.cmn;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("user")
public class MongoUser {

    private String id;

    private String name;

    private String sex;
}
