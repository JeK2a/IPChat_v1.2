package com.ChatIP.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "message", catalog = "myshema")
public class Message implements Serializable {

    @Id
    @Column (name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date = null;

    @Column(name = "name")
    private String name = null;

    @Column (name = "text")
    private String text = null;

    @Column(name = "namePCAndIP")
    private String namePCAndIP = null;

    @Column(name = "status")
    private String status = null;

    public Message(Timestamp date, String name, String text, String namePCAndIP, String status) {
        this.date = date;
        this.name = name;
        this.text = text;
        this.namePCAndIP = namePCAndIP;
        this.status = status;
    }

    public Message(String name, String text, String status) {
        this.date = new Timestamp(new Date().getTime());
        this.name = name;
        this.text = text;
        this.namePCAndIP = null;
        this.status = status;
    }

    public Message(String name, String text) {
        this.date =new Timestamp(new Date().getTime());
        this.name = name;
        this.text = text;
        this.namePCAndIP = null;
        this.status = null;
    }

    public long getId() {
        return id;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getNamePCAndIP() {
        return namePCAndIP;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return  name + ": " + text + " - " + new SimpleDateFormat("HH:mm:ss").format(date);
    }
}
