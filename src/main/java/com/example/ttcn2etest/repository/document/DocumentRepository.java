package com.example.ttcn2etest.repository.document;

import com.example.ttcn2etest.model.etity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {
    @Query("SELECT d FROM Document d WHERE d.idService = :idService")
    List<Document> findByIdService(@Param("idService") Long idService);
}
