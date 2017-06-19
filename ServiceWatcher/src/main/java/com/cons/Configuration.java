package com.cons;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    public Configuration() {
        super();
    }
    public List<ServiceParameter> listWithParameters = new ArrayList<ServiceParameter>();
    private int concurentThreads = 5;

    public void init() {
        //to do
    }

    public void init(String fileName) {
        //to do
    }
    public boolean isValid(){
        return true;
    }
    public void save(){
        
    }

    public void setListWithParameters(List<ServiceParameter> listWithParameters) {
        this.listWithParameters = listWithParameters;
    }

    public List<ServiceParameter> getListWithParameters() {
        return listWithParameters;
    }

    public void setConcurentThreads(int concurentThreads) {
        this.concurentThreads = concurentThreads;
    }

    public int getConcurentThreads() {
        return concurentThreads;
    }
}
