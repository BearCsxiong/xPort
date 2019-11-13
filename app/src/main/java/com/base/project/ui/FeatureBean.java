package com.base.project.ui;

/**
 * @Desc : 功能bean
 * @Author : csxiong - 2019-11-13
 */
public class FeatureBean {

    private String name;
    
    private String route;

    public FeatureBean(String name, String route) {
        this.name = name;
        this.route = route;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
