package org.example.users.repository;

import org.example.users.model.CuentaContable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CuentaContableRepository extends JpaRepository<CuentaContable, String> {


    @Query(value = "SELECT *  FROM dbmaster.cuentas WHERE codigo_agrupador='101' OR codigo_agrupador ='102' OR codigo_agrupador ='103' OR codigo_agrupador ='105' OR codigo_agrupador ='107' OR codigo_agrupador ='110' OR codigo_agrupador ='113' OR codigo_agrupador ='114' OR codigo_agrupador ='118' OR codigo_agrupador ='119'", nativeQuery = true)
    List<CuentaContable> getValuestoBlanaceActivo();

    @Query(value = "SELECT *  FROM dbmaster.cuentas WHERE codigo_agrupador='101' OR codigo_agrupador ='102' OR codigo_agrupador ='103' OR codigo_agrupador ='105' OR codigo_agrupador ='107' OR codigo_agrupador ='110' OR codigo_agrupador ='113' OR codigo_agrupador ='114' OR codigo_agrupador ='118' OR codigo_agrupador ='119'", nativeQuery = true)
    String getSumBlanaceActivo();

    @Query(value = "SELECT *  FROM dbmaster.cuentas WHERE codigo_agrupador='201' OR codigo_agrupador ='205' OR codigo_agrupador ='208' OR codigo_agrupador ='209' OR codigo_agrupador ='210' OR codigo_agrupador ='213' OR codigo_agrupador ='216' OR codigo_agrupador ='218'", nativeQuery = true)
    List<CuentaContable> getValuestoBlanacePasivo();

    @Query(value = "SELECT *  FROM dbmaster.cuentaContable WHERE codigo_agrupador='171'", nativeQuery = true)
    List<CuentaContable> getValuestoBlanaceActivoFijo();

    @Query(value = "SELECT *  FROM dbmaster.cuentaContable WHERE codigo_agrupador='184'", nativeQuery = true)
    List<CuentaContable> getValuesBalanceActivoDiferido();

    @Query(value = "SELECT *  FROM dbmaster.cuentas WHERE codigo_agrupador='301' OR codigo_agrupador ='304' OR codigo_agrupador ='305'", nativeQuery = true)
    List<CuentaContable> getValuestoBlanaceCapital();



    @Query(value = "SELECT * FROM dbmaster.cuentaContable cc WHERE cc.codigo_agrupador =:codigo_agrupador ", nativeQuery = true)
    CuentaContable getCuantaContable(String codigo_agrupador);

    @Query(value = "SELECT cc.nombre_cuenta FROM dbmaster.cuentaContable cc WHERE cc.codigo_agrupador =:codigo_agrupador ", nativeQuery = true)
    String getCuentaContableVenta(String codigo_agrupador);


    @Query(value = "SELECT cc.nombre_cuenta FROM dbmaster.cuentaContable cc WHERE cc.codigo_agrupador =:codigo_agrupador ", nativeQuery = true)
    String getCuantaContableMethod(String codigo_agrupador);

    @Query(value = "SELECT c.nombre_cuenta FROM dbmaster.cuentaContable c WHERE c.codigo_agrupador =:tax_id ", nativeQuery = true)
    String getCuantaContableTax(String tax_id);

}
