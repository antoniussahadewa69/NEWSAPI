package com.example.newsapi.realm;

import io.realm.RealmObject;

public class SourceRealm extends RealmObject {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String id;;
    String name;
}
