package com.jgp.ljoa.hr.service.impl;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.hr.model.Relation;
import com.jgp.ljoa.hr.repository.RelationRepository;
import com.jgp.ljoa.hr.service.RelationService;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class RelationServiceImpl implements RelationService {
    @Autowired
    private RelationRepository relationRepository;

    @Transactional
    @Override
    public Relation saveRelation(Relation ljRelation) {
        return relationRepository.createOrUpdate(ljRelation);
    }


    @Override
    public Relation queryOrgByEmployeeId(String id) {
        QueryFilterList list = new QueryFilterList();
        list.addFilter("subUuid","eq",id);
        List<Relation> read = relationRepository.read(list);
        return read.size() > 0 ? read.get(0) : null;
    }

    @Override
    public List<Relation> queryGroupRelation(String mainUuid, String type,@UIParam("pager")Pager pager) {
        QueryFilterList list = new QueryFilterList();
        list.addFilter("mainUuid","eq",mainUuid);
        list.addFilter("relationType","eq",type);
        return relationRepository.read(list,pager);
    }
    @Transactional
    @Override
    public void removeRelation(String id) {
        relationRepository.delete(id);
    }


    @Override
    public Relation queryRelationByAdminUserId(String adminUserId) {
        QueryFilterList list = new QueryFilterList();
        list.addFilter("subUuid","eq",adminUserId);
        list.addFilter("relationType","eq","3");
        List<Relation> read = relationRepository.read(list);
        return read.size() > 0 ? read.get(0) : null;
    }


    @Override
    public List<Relation> queryGroupRelation(Relation relation) {
        QueryFilterList list = new QueryFilterList();
        if(Objects.nonNull(relation.getMainUuid()) && StringUtils.isNotEmpty(relation.getMainUuid())){
            list.addFilter("mainUuid","eq",relation.getMainUuid());
        }
        if(Objects.nonNull(relation.getSubUuid()) && StringUtils.isNotEmpty(relation.getSubUuid())){
            list.addFilter("subUuid","eq",relation.getSubUuid());
        }
        if(Objects.nonNull(relation.getMainId()) && StringUtils.isNotEmpty(relation.getMainId())){
            list.addFilter("mainId","eq",relation.getMainId());
        }
        if(Objects.nonNull(relation.getSubId()) && StringUtils.isNotEmpty(relation.getSubId())){
            list.addFilter("subId","eq",relation.getSubId());
        }
        if(Objects.nonNull(relation.getRelationType()) && StringUtils.isNotEmpty(relation.getRelationType())){
            list.addFilter("relationType","eq",relation.getRelationType());
        }
        return relationRepository.read(list);
    }
}
