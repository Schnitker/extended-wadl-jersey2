package com.github.schnitker.extwadl.sample;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement ( name = "person", namespace = "http://www.sample.com/sampleapp/2018/01" )
@XmlType ( name = "person", namespace = "http://www.sample.com/sampleapp/2018/01" )
@XmlAccessorType ( XmlAccessType.FIELD )
public class RsPerson {

    @XmlAttribute ( name = "name", required = true )
    private String name;
    
    @XmlElement ( name = "email" )
    private String email;

    public RsPerson() {
    }

    public RsPerson( String name, String email ) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", email=" + email + "]";
    }
}
