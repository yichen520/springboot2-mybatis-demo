package com.dhht.model;

import lombok.Data;

import java.util.List;

@Data
public class ExamineDetailList {
      private List<ExamineDetail> examineDetails;
      private String examineTypeId;
}
