package com.example.ttcn2etest.service.document;

import com.example.ttcn2etest.model.dto.DocumentDTO;
import com.example.ttcn2etest.model.etity.Document;
import com.example.ttcn2etest.request.document.CreateDocumentRequest;
import com.example.ttcn2etest.request.document.FilterDocumentRequest;
import com.example.ttcn2etest.request.document.UpdateDocumentRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface DocumentService {
    List<DocumentDTO> getAllDocument();

    DocumentDTO getByIdDocument(Long id);

    DocumentDTO createDocument(CreateDocumentRequest request);

    DocumentDTO updateDocument(UpdateDocumentRequest request, Long id);

    DocumentDTO deleteByIdDocument(Long id);

    List<DocumentDTO> deleteAllIdDoc(List<Long> ids);

    List<DocumentDTO> deleteAllIdDocument(List<Long> ids);

    Page<Document> filterDocument(FilterDocumentRequest request, Date dateFrom, Date dateTo);
}
