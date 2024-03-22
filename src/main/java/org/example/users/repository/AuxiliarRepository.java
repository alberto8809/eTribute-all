package org.example.users.repository;


import org.example.users.model.Auxiliar;
import org.example.users.model.ListAuxiliar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuxiliarRepository  extends JpaRepository<Auxiliar,String> {

    @Query(value = "SELECT pof.cuenta, pof.concepto FROM dbmaster.policyObjFile pof, dbmaster.user u, dbmaster.account a  WHERE a.account_id = pof.account_id AND u.token=:token" ,nativeQuery = true)
    List<ListAuxiliar> getAuxiliar(String token);

    @Query(value = "SELECT * FROM dbmaster.policyObjFile pof WHERE pof.cuenta=:cuenta" ,nativeQuery = true)
    List<Auxiliar> valuesOfTable(String cuenta);

    @Query(value = "SELECT SUM(pof.cargo) FROM  dbmaster.policyObjFile pof where pof.cuenta=:cuenta" ,nativeQuery = true)
    String getSumCargo(String cuenta);

    @Query(value = "SELECT SUM(pof.abono) FROM  dbmaster.policyObjFile pof where pof.cuenta=:cuenta" ,nativeQuery = true)
    String getSumAbono(String cuenta);

    @Query(value = "SELECT DISTINCT pof.fecha FROM  dbmaster.policyObjFile pof, dbmaster.user u  where u.token=:token" ,nativeQuery = true)
    List<String> getDatesByUser(String token);

    @Query(value = "SELECT * FROM dbmaster.policyObjFile pof WHERE  pof.fecha BETWEEN :initial_date AND :final_date" ,nativeQuery = true)
    List<Auxiliar> getValuestoBlanace(String initial_date, String final_date);

}
