package pl.edu.pja.tpo7.s26822tpo7;


import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

public class Code implements Serializable {
    private String id;
    private String body;
    private LocalDateTime exp;
    public Code(String id, String body)
    {
        this.body=body;
        this.id=id;
    }

    public Code() {
    }

    public LocalDateTime getExp() {
        return exp;
    }

    public void setExp(LocalDateTime exp) {
        this.exp = exp;
    }

    public Code(String body) {
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
