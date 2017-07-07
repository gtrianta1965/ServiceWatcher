package com.cons.services;

public class ServiceParameter {
    public ServiceParameter() {
        super();
    }
    
    private int    id;
    private String url;
    private String ip;
    private int    port;
    private int    socDieInterval;
    private String description;
    private String type;
    private String group;
    private String searchString;
    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
    
    public void setIP(String ip) {
        this.ip = ip;
    }

    public String getIP() {
        return ip;
    }
    
    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }

    public int getPort() {
        return port;
    }
    
    public void setSocDieInterval(String interval) {
        this.socDieInterval = Integer.parseInt(interval);
    }

    public int getSocDieInterval() {
        return socDieInterval;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
