package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {

    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> clueActivityRelationList);

    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation clueActivityRelation);
}
