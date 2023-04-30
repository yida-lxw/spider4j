package com.yida.spider4j.crawler.utils.jdbc;

/**
 * @author yida
 * @package com.witarchive.common.jdbc
 * @date 2023-04-26 09:13
 * @description Type your description over here.
 */
@Table("t_user")
public class User {
    private Integer id;
    private String userName;
    private Integer age;

    public User() {}

    public User(Integer id, String userName, Integer age) {
        this.id = id;
        this.userName = userName;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
