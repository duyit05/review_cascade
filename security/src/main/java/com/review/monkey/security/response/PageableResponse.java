package com.review.monkey.security.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class PageableResponse {
    int pageNumber;
    int pageSize;
    int totalPage;
    long totalRecord;

    public PageableResponse setPageNumber (int pageNumber){
        this.pageNumber = pageNumber + 1;
        return this;
    }

    public PageableResponse setPageSize (int pageSize){
        this.pageSize = pageSize;
        return this;
    }

    public PageableResponse setTotalPage (int totalPage){
        this.totalPage = totalPage;
        return this;
    }

    public PageableResponse setToltalRecord (long totalRecord){
        this.totalRecord = totalRecord;
        return this;
    }
}
