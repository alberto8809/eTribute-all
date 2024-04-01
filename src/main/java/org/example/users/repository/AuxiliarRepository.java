package org.example.users.repository;


import org.example.users.model.Auxiliar;
import org.example.users.model.ListAuxiliar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuxiliarRepository extends JpaRepository<Auxiliar, String> {


    @Query(value = "SELECT user_id  from user where token =:token", nativeQuery = true)
    String getIdByToken(String token);

    @Query(value = "SELECT account_id  from account where account.user_id=:user_id", nativeQuery = true)
    List<String> getAccountByUserId(String user_id);


    @Query(value = "SELECT * from policyObjFile WHERE account_id=:acount", nativeQuery = true)
    List<Auxiliar> getAuxiliar(String acount);

    @Query(value = "SELECT * FROM dbmaster.policyObjFile pof WHERE pof.cuenta=:cuenta", nativeQuery = true)
    List<Auxiliar> valuesOfTable(String cuenta);

    @Query(value = "SELECT SUM(pof.cargo) FROM  dbmaster.policyObjFile pof where pof.cuenta=:cuenta", nativeQuery = true)
    String getSumCargo(String cuenta);

    @Query(value = "SELECT SUM(pof.abono) FROM  dbmaster.policyObjFile pof where pof.cuenta=:cuenta", nativeQuery = true)
    String getSumAbono(String cuenta);

    @Query(value = "SELECT DISTINCT pof.fecha FROM  dbmaster.policyObjFile pof, dbmaster.user u  where u.token=:token", nativeQuery = true)
    List<String> getDatesByUser(String token);

    @Query(value = "SELECT * FROM dbmaster.policyObjFile pof WHERE  pof.fecha BETWEEN :initial_date AND :final_date", nativeQuery = true)
    List<Auxiliar> getValuestoBlanace(String initial_date, String final_date);

}
