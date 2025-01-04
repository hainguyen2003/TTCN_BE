package com.example.ttcn2etest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseListItemResponse<T> extends BaseResponse {
    private DataList<T> data;

    public void setResult(List<T> items, long total) {
        data = new DataList<>();
        data.setItems(items);
        data.setTotal(total);
    }

    @Data
    public static class DataList<T> {
        private long total = 0;
        private List<T> items;
    }
}
