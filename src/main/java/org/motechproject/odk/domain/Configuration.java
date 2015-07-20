package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Persistent;
import java.util.List;

@Entity
public class Configuration {

    @Field
    private String url;
    @Field
    private String username;
    @Field
    private String password;
    @Field
    private String name;
    @Field
    private ConfigurationType type;



    public Configuration(String url, String username, String password, String name, ConfigurationType type) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.name = name;
        this.type = type;
    }

    public Configuration() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConfigurationType getType() {
        return type;
    }

    public void setType(ConfigurationType type) {
        this.type = type;
    }


}
