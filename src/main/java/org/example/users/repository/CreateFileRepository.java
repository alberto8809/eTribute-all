package org.example.users.repository;



import org.example.users.model.Regimen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
//@ComponentScan
public interface CreateFileRepository extends JpaRepository<Regimen,String> {

    @Query(value = "SELECT * FROM dbmaster.regimen cc WHERE cc.regimen_id =:regimen " ,nativeQuery = true)
    Regimen getRegimen(String regimen);



}
