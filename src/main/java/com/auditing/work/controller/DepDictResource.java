package com.auditing.work.controller;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auditing.work.controller.util.HeaderUtil;
import com.auditing.work.jpa.dao.DepDictRepository;
import com.auditing.work.jpa.po.DepDict;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing DepDict.
 */
@RestController
@RequestMapping("/api")
@Api(tags="部门字典")
public class DepDictResource {

    private final Logger log = LoggerFactory.getLogger(DepDictResource.class);

    private static final String ENTITY_NAME = "depDict";

    @Autowired
    private  DepDictRepository depDictRepository;

    /**
     * POST  /dep-dicts : Create a new depDict.
     *
     * @param depDict the depDict to create
     * @return the ResponseEntity with status 201 (Created) and with body the new depDict, or with status 400 (Bad Request) if the depDict has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value="/dep-dicts",method=RequestMethod.POST)
    @ApiOperation(value="新增",notes = "新增")
    public ResponseEntity<DepDict> createDepDict(@RequestBody DepDict depDict) throws URISyntaxException {
        log.debug("REST request to save DepDict : {}", depDict);
        if (depDict.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new depDict cannot already have an ID")).body(null);
        }
        
        DepDict result = depDictRepository.save(depDict);
        return ResponseEntity.created(new URI("/api/dep-dicts/" + result.getId()))
            .body(result);
    }

    /**
     * PUT  /dep-dicts : Updates an existing depDict.
     *
     * @param depDict the depDict to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated depDict,
     * or with status 400 (Bad Request) if the depDict is not valid,
     * or with status 500 (Internal Server Error) if the depDict couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value="/dep-dicts",method=RequestMethod.PUT)
    @ApiOperation(value="修改",notes = "修改")
    public ResponseEntity<DepDict> updateDepDict(@RequestBody DepDict depDict) throws URISyntaxException {
        log.debug("REST request to update DepDict : {}", depDict);
        if (depDict.getId() == null) {
            return createDepDict(depDict);
        }
        String name = depDict.getName();
        String[] names = name.split(",");
        Set<String> set = new HashSet<>();
        for(String s : names) {
        	set.add(s);
        }
        if(set.size() < names.length) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "nameexists", "The dep_dict has existed")).body(null);
        }
        DepDict result = depDictRepository.save(depDict);
        return ResponseEntity.ok()  .body(result);
    }

    /**
     * GET  /dep-dicts : get all the depDicts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of depDicts in body
     * @throws URISyntaxException 
     */
    @RequestMapping(value="/dep-dicts",method=RequestMethod.GET)
    @ApiOperation(value="列表",notes = "列表")
    public ResponseEntity<List<DepDict>> getAllDepDicts() throws URISyntaxException {
        log.debug("REST request to get a page of DepDicts");
        List<DepDict> list = depDictRepository.findAll();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dep-dicts");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }



    /**
     * DELETE  /dep-dicts/:id : delete the "id" depDict.
     *
     * @param id the id of the depDict to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value="/dep-dicts/{id}",method=RequestMethod.DELETE)
    @ApiOperation(value="删除",notes = "删除")
    public ResponseEntity<Void> deleteDepDict(@PathVariable Long id) {
        log.debug("REST request to delete DepDict : {}", id);
        depDictRepository.delete(id);
        return ResponseEntity.ok().build();
    }

}
