package com.dhht.service.index;

import com.dhht.model.IndexCount;
import com.dhht.model.Seal;
import com.dhht.model.User;

import java.util.List;

public interface EmployeeIndexService {
    List<IndexCount> piechart(User user);

}
