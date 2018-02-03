package org.example.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer age;

    private Boolean isremoved = false;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Boolean getIsremoved() {
        return isremoved;
    }

    public Boolean isDeleted() {
        return isremoved;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void delete() {
        this.isremoved = true;
    }

    public User() {
    }

    public User(cc.api.model.v1.model.User restResponseUser) {
        id = restResponseUser.getId();
        name = restResponseUser.getName();
        age = restResponseUser.getAge();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (age != null ? !age.equals(user.age) : user.age != null) return false;
        return isremoved != null ? isremoved.equals(user.isremoved) : user.isremoved == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (isremoved != null ? isremoved.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append(", isremoved=").append(isremoved);
        sb.append('}');
        return sb.toString();
    }

    public cc.api.model.v1.model.User convertToRestResponse() {
        cc.api.model.v1.model.User restResponseUser =  new cc.api.model.v1.model.User();
        restResponseUser.setAge(this.age);
        restResponseUser.setId(this.id);
        restResponseUser.setName(this.name);
        return restResponseUser;
    }
}
