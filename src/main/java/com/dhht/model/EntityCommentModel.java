package com.dhht.model;

import com.dhht.annotation.EntityComment;
import lombok.Data;

@Data
public class EntityCommentModel {
    @EntityComment("值")
   private String value;
    @EntityComment("类型")
   private int type;

   public EntityCommentModel(){

   }

    public EntityCommentModel(String value, int type) {
        this.value = value;
        this.type = type;
    }
}
