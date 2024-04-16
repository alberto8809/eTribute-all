package org.example.users.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveObjRepository extends JpaRepository<Object, Long> {
}
