package org.example.users.repository;


import org.example.users.model.PolicytoDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaveObjRepository extends JpaRepository<PolicytoDB, Long> {

    @Query(value = "SELECT file_name FROM policy p where p.usuario=:usuario and p.tipo=:type", nativeQuery = true)
    List<String> getObjFromDB(String usuario, String type);
}
