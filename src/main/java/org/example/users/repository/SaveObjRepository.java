package org.example.users.repository;

import org.example.users.model.PolicytoDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface SaveObjRepository extends JpaRepository<PolicytoDB, Long> {

    @Query(value = "SELECT DISTINCT (usuario)  FROM dbmaster.policy  WHERE account_id=:account_id", nativeQuery = true)
   String getValuesOfDB(String account_id);

}
