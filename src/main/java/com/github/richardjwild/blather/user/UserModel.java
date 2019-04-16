package com.github.richardjwild.blather.user;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "UserModel")
public class UserModel implements Serializable {

    @Id @GeneratedValue
    @Column(name = "ID")
    private int id;

    @Column(name = "Name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name){
         this.name = name;
    }
}
