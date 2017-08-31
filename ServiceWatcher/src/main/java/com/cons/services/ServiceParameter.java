package com.cons.services;


public class ServiceParameter {
    public ServiceParameter() {
        super();
        this.status = "NA";
    }

    private int id;
    private int retries; //Number of retries to connect to the service before consider it "dead"
    private int actualRetries; //Number of attempts (retries). If they reach "retries" and the service still doesn't
    //respond then we have a failure
    private String url;
    private String description;
    private String type;
    private String group;
    private String searchString;
    private String username;
    private String password;
    private String status;
    private String error;
    private String query;
    private String context;
    private String command;

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

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

    public void setDescription(String description) {
        this.description = description == null ? "No Description" : description;
    }

    public String getDescription() {
        return description;
    }

    public void setType(String type) {
        this.type = type == null ? "" : type;
    }

    public String getType() {
        return type;
    }

    public void setGroup(String group) {
        this.group = group == null ? "NoGroup" : group;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getRetries() {
        return retries;
    }

    public void setActualRetries(int actualRetries) {
        this.actualRetries = actualRetries;
    }

    public int getActualRetries() {
        return actualRetries;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
