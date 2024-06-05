package org.example.users.repository;


import org.example.users.model.PolicytoDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveObjRepository extends JpaRepository<PolicytoDB, Long> {
}
