package com.example.esdemo.pojo;

import lombok.Data;

/**
 * @author :liyufei
 * @Decirption : todo
 * @date :2020/8/9 16:44
 */
@Data
public class User {

    private Long id;

    private String name;// 姓名

    private Integer age;// 年龄

    private String gender;// 性别

    private String note;// 备注
}
