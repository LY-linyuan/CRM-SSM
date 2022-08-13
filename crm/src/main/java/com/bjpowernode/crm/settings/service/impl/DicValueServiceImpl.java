package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import com.bjpowernode.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 临渊
 * @Date 2022-08-13 20:10
 */

@Service
public class DicValueServiceImpl implements DicValueService {


    @Autowired
    DicValueMapper dicValueMapper;


    @Override
    public List<DicValue> queryDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
