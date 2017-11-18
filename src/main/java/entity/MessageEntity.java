package entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "message", schema = "myshema")
public class MessageEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date;

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column(name = "namePCAndIP")
    private String namePcAndIp;

    public String getNamePcAndIp() {
        return namePcAndIp;
    }

    public void setNamePcAndIp(String namePcAndIp) {
        this.namePcAndIp = namePcAndIp;
    }

    @Column(name = "status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntity that = (MessageEntity) o;

        if (id != that.id) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (namePcAndIp != null ? !namePcAndIp.equals(that.namePcAndIp) : that.namePcAndIp != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (namePcAndIp != null ? namePcAndIp.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
