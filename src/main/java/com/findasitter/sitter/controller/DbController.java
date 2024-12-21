package com.findasitter.sitter.controller;

import com.findasitter.sitter.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbController {

    @Autowired
    private DbService dbService;

    @GetMapping("/column-count/{tableName}")
    public int getColumnCount(@PathVariable String tableName) {
        return dbService.getColumnCount(tableName);
    }
}

