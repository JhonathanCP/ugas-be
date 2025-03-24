package com.essalud.ugas.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.essalud.ugas.model.User;
import com.essalud.ugas.model.FileType;
import com.essalud.ugas.model.Module;
import com.essalud.ugas.model.Report;


public interface IUserRepo extends IGenericRepo<User, Integer>{

    //@Query("FROM User u WHERE u.username = :username")
    //DerivedQuery
    User findOneByUsername(String username);

    @Query("SELECT DISTINCT m FROM Module m JOIN Report r ON m.idModule = r.module.idModule JOIN UserReport ur ON ur.report.idReport = r.idReport JOIN User u ON u.idUser = ur.user.idUser WHERE u = :user AND r.active = true")
    List<Module> getModulesWithActiveReports(User user);

    @Query("SELECT DISTINCT r FROM Report r JOIN r.module m JOIN UserReport ur ON ur.report.idReport = r.idReport JOIN User u ON u.idUser = ur.user.idUser WHERE u = :user AND m = :module AND r.active = true")
    List<Report> getActiveReportsFromModule(User user, Module module);

    @Query("SELECT DISTINCT f FROM FileType f JOIN MediaFile m ON f.idFileType = m.fileType.idFileType JOIN UserMediaFile um ON um.mediaFile.idMediaFile = m.idMediaFile JOIN User u ON u.idUser = um.user.idUser WHERE u = :user AND m.active = true")
    List<FileType> getFileTypesWithActiveFiles(User user);
}