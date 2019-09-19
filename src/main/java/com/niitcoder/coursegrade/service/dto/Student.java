package com.niitcoder.coursegrade.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {
    private Long id;
    private String login;

    public Student() {
    }

    public Student(Long id, String login) {
        this.id = id;
        this.login = login;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public String getLogin(){
        return login;
    }
    public void setLogin(String login){
        this.login = login;
    }
    @Override
    public String toString() {
        return "CourseGroup{" +
            "id=" + getId() +
            ", groupCode='" + getLogin() + "'" +
            "}";
    }
}
