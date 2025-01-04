package com.example.ttcn2etest.service.document;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.dto.DocumentDTO;
import com.example.ttcn2etest.model.etity.Document;
import com.example.ttcn2etest.repository.document.CustomDocumentRepository;
import com.example.ttcn2etest.repository.document.DocumentRepository;
import com.example.ttcn2etest.request.document.CreateDocumentRequest;
import com.example.ttcn2etest.request.document.FilterDocumentRequest;
import com.example.ttcn2etest.request.document.UpdateDocumentRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final ModelMapper modelMapper;

    public DocumentServiceImpl(DocumentRepository documentRepository, ModelMapper modelMapper) {
        this.documentRepository = documentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DocumentDTO> getAllDocument() {
        return documentRepository.findAll().stream().map(
                document -> modelMapper.map(document, DocumentDTO.class)
        ).toList();
    }

    @Override
    public DocumentDTO getByIdDocument(Long id) {
        Optional<Document> documentOptional = documentRepository.findById(id);
        if (documentOptional.isPresent()) {
            return modelMapper.map(documentOptional.get(), DocumentDTO.class);
        } else {
            throw new MyCustomException("Id tài liệu không tồn tại trong hệ thống!");
        }
    }

    @Override
    @Transactional
    public DocumentDTO createDocument(CreateDocumentRequest request) {
        try {
            Document document = Document.builder()
                    .name(request.getName())
                    .content(request.getContent())
                    .file(request.getFile())
                    .image(request.getImage())
                    .status(request.getStatus())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .updateDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            document = documentRepository.saveAndFlush(document);
            return modelMapper.map(document, DocumentDTO.class);
        } catch (Exception ex) {
            throw new MyCustomException("Có lỗi xảy ra trong quá trình thêm tài liệu mới!");
        }
    }

    @Override
    @Transactional
    public DocumentDTO updateDocument(UpdateDocumentRequest request, Long id) {
        Optional<Document> documentOptional = documentRepository.findById(id);
        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();
            document.setName(request.getName());
            document.setContent(request.getContent());
            document.setFile(request.getFile());
            document.setImage(request.getImage());
            document.setStatus(request.getStatus());
            document.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            return modelMapper.map(documentRepository.saveAndFlush(document), DocumentDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình cập nhật tài liệu!");
    }

    @Override
    @Transactional
    public DocumentDTO deleteByIdDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new MyCustomException("Tài liệu có id: " + id + " cần xóa không tồn tại trong hệ thống!");
        }
        Optional<Document> documentOptional = documentRepository.findById(id);
        if (documentOptional.isPresent()) {
            documentRepository.deleteById(id);
            return modelMapper.map(documentOptional, DocumentDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trinh xóa tài liệu!");
    }

    //31ms => 3ptu
    @Override
    public List<DocumentDTO> deleteAllIdDoc(List<Long> ids) {
        List<DocumentDTO> documentDTOS = new ArrayList<>();
        for (Document document : documentRepository.findAllById(ids)) {
            documentDTOS.add(modelMapper.map(document, DocumentDTO.class));
        }
        documentRepository.deleteAllByIdInBatch(ids);
        return documentDTOS;
    }

    //26s=>3ptu
    public List<DocumentDTO> deleteAllIdDocument(List<Long> ids) {
        List<DocumentDTO> documentDTOS = new ArrayList<>();
        for (Long id : ids) {
            Optional<Document> optionalDocument = documentRepository.findById(id);
            if (optionalDocument.isPresent()) {
                Document document = optionalDocument.get();
                documentDTOS.add(modelMapper.map(document, DocumentDTO.class));
                documentRepository.delete(document);
            } else {
                throw new MyCustomException("Có lỗi xảy ra trong quá trình xóa danh sách tài liệu!");
            }
        }
        return documentDTOS;
    }

    @Override
    public Page<Document> filterDocument(FilterDocumentRequest request, Date dateFrom, Date dateTo) {
        Specification<Document> specification = CustomDocumentRepository.filterSpecification(dateFrom, dateTo, request);
        return documentRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
    }
}
