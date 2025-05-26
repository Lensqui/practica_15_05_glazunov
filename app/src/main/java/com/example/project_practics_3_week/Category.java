package com.example.project_practics_3_week;

import java.io.Serializable;

public class Category implements Serializable {
    private String id;
    private String name;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
