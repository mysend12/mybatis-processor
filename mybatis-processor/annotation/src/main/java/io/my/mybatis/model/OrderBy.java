package io.my.mybatis.model;

public enum OrderBy {
    DESC("DECS"), ASC("ASC")
    ;
    String order;

    OrderBy(String order) {
        this.order = order;
    }

    public String getOrder(){
        return this.order;
    }
}
