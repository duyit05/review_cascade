package com.review.monkey.security.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagingResponse<T> {
    List<T> contents = new ArrayList<>();
    PageableResponse paging;

    public PagingResponse<T> setContents(List<T> contents) {
        this.contents = contents;
        return this;
    }

    public PagingResponse<T> setPaing (PageableResponse paging){
        this.paging = paging;
        return this;
    }
}
