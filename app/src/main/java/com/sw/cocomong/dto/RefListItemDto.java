package com.sw.cocomong.dto;

import java.util.ArrayList;
import java.util.List;

public class RefListItemDto {
    private int refId;
    private String name;
    private static List<RefListItemDto> refListItemDtos = new ArrayList<>();
    public RefListItemDto(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }


    public static List<RefListItemDto> getRefListItemDtos() {
        return refListItemDtos;
    }
}
