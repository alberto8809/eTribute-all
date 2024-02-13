package org.example.users.repository;

import org.example.users.model.Regimen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegimenRepository extends JpaRepository<Regimen,String> {

}
