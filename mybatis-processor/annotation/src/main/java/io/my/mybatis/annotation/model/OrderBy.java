package io.my.mybatis.annotation.model;

public enum OrderBy {
    ASC("ASC"), DESC("DESC");
    
    OrderBy (String orderBy) {
        this.orderBy = orderBy;
    }
    private String orderBy;

    public String getOrderBy() {
        return this.orderBy;
    }

}
