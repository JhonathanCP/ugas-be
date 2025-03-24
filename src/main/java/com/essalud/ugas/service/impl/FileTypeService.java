package com.essalud.ugas.service.impl;

import com.essalud.ugas.model.FileType;
import com.essalud.ugas.repo.IGenericRepo;
import com.essalud.ugas.repo.IFileTypeRepo;
import com.essalud.ugas.service.IFileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileTypeService extends CRUDImpl<FileType, Integer> implements IFileTypeService{

    @Autowired
    private IFileTypeRepo repo;

    @Override
    protected IGenericRepo<FileType, Integer> getRepo() {
        return repo;
    }

}
