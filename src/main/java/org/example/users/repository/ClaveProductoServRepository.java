package org.example.users.repository;


import org.example.users.model.ClaveProductoServ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaveProductoServRepository extends JpaRepository<ClaveProductoServ,String> {
    @Query(value = "SELECT *  FROM dbmaster.claveProdServ cp WHERE cp.c_claveprodserv=:c_claveprodserv " ,nativeQuery = true)
    ClaveProductoServ getClaveProducto(String c_claveprodserv);

    @Query(value = "SELECT *  FROM dbmaster.claveProdServ cp WHERE cp.c_claveprodserv=:c_claveprodserv " ,nativeQuery = true)
    List<String> getClaveProductoS(String c_claveprodserv);



}
