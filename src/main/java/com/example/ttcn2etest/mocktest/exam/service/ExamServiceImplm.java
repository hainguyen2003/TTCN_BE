package com.example.ttcn2etest.mocktest.exam.service;

import com.example.ttcn2etest.mocktest.answer.entity.Answer;
import com.example.ttcn2etest.mocktest.exam.dto.DetailExamDTO;
import com.example.ttcn2etest.mocktest.exam.dto.ExamDTO;
import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.example.ttcn2etest.mocktest.exam.repository.ExamRepository;
import com.example.ttcn2etest.mocktest.exam.request.ExamRequest;
import com.example.ttcn2etest.mocktest.question.dto.QuestionDTO;
import com.example.ttcn2etest.mocktest.question.entity.Question;
import com.example.ttcn2etest.mocktest.question.repository.QuestionRepository;
import com.example.ttcn2etest.mocktest.section.dto.SectionDTO;
import com.example.ttcn2etest.mocktest.section.entity.Section;
import com.example.ttcn2etest.mocktest.section.repository.SectionRepository;
import com.example.ttcn2etest.mocktest.section.request.SectionRequest;
import com.example.ttcn2etest.mocktest.section.service.SectionService;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResponse;
import com.example.ttcn2etest.mocktest.user_exam.repository.UserResponseRepository;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.repository.user.UserRepository;
import com.example.ttcn2etest.response.BaseItemResponse;
import com.example.ttcn2etest.response.BaseListItemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ExamServiceImplm implements ExamService {

    private final SectionService sectionService;
    private final ExamRepository examRepository;
    private final SectionRepository sectionRepository;
    private final ModelMapper mapper;
    private final UserResponseRepository userResponseRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public Exam createExam(ExamRequest request) {
        Exam exam = new Exam();
        exam.setCreateDate(new Date());
        exam.setName(request.getName());
        exam.setTimeExam(request.getTimeExam());
        examRepository.save(exam);
        List<Section> sections = new ArrayList<>();

        for (SectionRequest sectionRequest : request.getSectionRequests()) {
            Section section = sectionService.createSectionInExam(sectionRequest);
            section.setExam(exam);
            sectionRepository.save(section);
            sections.add(section);
        }
        exam.setSections(sections);
        return exam;

    }

    @Override
    public ExamDTO updateExam(ExamRequest request) {
        Optional<Exam> exam = examRepository.findById(request.getId());
        if (!exam.isPresent()) {
            throw new RuntimeException("Không tìm thấy id");
        }

        exam.get().setName(request.getName());
        if (request.getIsFree()) {
            exam.get().setIsFree(true);
        } else {
            exam.get().setIsFree(false);
        }
        exam.get().setType(request.getType());

        return mapper.map(examRepository.saveAndFlush(exam.get()), ExamDTO.class);
    }

    @Override
    public boolean deleteExam(String id) {
        Optional<Exam> exam = examRepository.findById(id);
        if (!exam.isPresent()) {
            throw new RuntimeException("ID không tồn tại");
        }
        List<Section> sections = sectionRepository.findSectionsByExam(exam.get());
        for (Section section : sections) {
            sectionService.deleteSection(section.getId());
        }
        for (UserResponse userResponse : exam.get().getUser_exam()) {
            userResponse.setExam(null);
            userResponseRepository.save(userResponse);

        }
        examRepository.delete(exam.get());
//        sectionRepository.deleteAll(sections);
        return true;
    }

    @Override
    public ResponseEntity<?> getAllExamFree() {
        List<ExamDTO> examDTOS = new ArrayList<>();
        List<Exam> exams = examRepository.findAll();
        examDTOS = exams.stream().filter(i -> i.getIsFree() == true).map(i -> mapper.map(i, ExamDTO.class)).collect(Collectors.toList());

        BaseListItemResponse response = new BaseListItemResponse();
        response.setResult(examDTOS, examDTOS.size());
        response.setSuccess();

        return ResponseEntity.ok((examDTOS.size() > 0) ? response : "Không có dữ liệu hiển thị ");
    }

    @Override
    public ResponseEntity<?> getAllExam() {
        List<ExamDTO> examDTOS = examRepository.findAll().stream().map(i -> mapper.map(i, ExamDTO.class)).collect(Collectors.toList());
        BaseListItemResponse response = new BaseListItemResponse();
        response.setSuccess();
        response.setResult(examDTOS, examDTOS.size());
        return ResponseEntity.ok(response);
    }

    @Override
    public DetailExamDTO getByID(String id) {
        Optional<Exam> exam = examRepository.findById(id);
        if (!exam.isPresent()) {
            throw new RuntimeException("ID không tồn tại");
        }
        return mapper.map(exam.get(), DetailExamDTO.class);
    }

    @Override
    public List<DetailExamDTO> listDetailExam() {
        List<DetailExamDTO> detailExamDTOS = new ArrayList<>();
        List<Exam> exams = examRepository.findAll();
        for (Exam exam : exams) {
            detailExamDTOS.add(mapper.map(exam, DetailExamDTO.class));
        }
        return detailExamDTOS;
    }

    @Override
    public ExamDTO addSectionToExam(SectionRequest sectionRequest) {
        Exam exam = examRepository.findExamById(sectionRequest.getExam_id());
        Section section = sectionService.createSection(sectionRequest);
        section.setExam(exam);
        sectionRepository.save(section);
        List<Section> sections = exam.getSections();
        sections.add(section);
        exam.setSections(sections);
        examRepository.save(exam);


        return mapper.map(exam, ExamDTO.class);
    }

    @Override
    public List<SectionDTO> findQuestionByType(String id, String type) {
        Optional<Exam> exam = examRepository.findById(id);
        List<SectionDTO> sections = exam.get().getSections().stream().map(i -> mapper.map(i, SectionDTO.class)).collect(Collectors.toList());

        long seed = System.nanoTime();
        Collections.shuffle(sections, new Random(seed));

        for (SectionDTO section : sections) {
            List<QuestionDTO> questions = section.getQuestions();
            Collections.shuffle(questions, new Random(seed));
        }

        return sections.stream()
                .filter(section -> section.getType().equals(type))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override

    public Exam createExamByExcel(String path) {

        Exam exam = new Exam();
        Iterator<Row> rowIterator = null;
        try {
            FileInputStream file = new FileInputStream(new File(path));
            Workbook workbook = new XSSFWorkbook(file);


            String currentExamName = null;
            boolean isExamNameRead = false;
            String currentNameSection = null;
            Section currentSection = null;
            Sheet sheet = workbook.getSheetAt(0);

            rowIterator = sheet.iterator();
            rowIterator.next();
            Section sectionCurrent = null;

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();


                Cell examNameCell = row.getCell(0);
                Cell typeExam = row.getCell(1);
                Cell timeExam = row.getCell(2);

                Cell isFreeCell = row.getCell(18);

//
                Cell sectionNameCell = row.getCell(3);
                Cell descriptionSectionCell = row.getCell(4);
                Cell fileSectionCell = row.getCell(5);
                Cell typeSectionCell = row.getCell(6);
                Cell questionContentCell = row.getCell(7);
                Cell questionTypeCell = row.getCell(8);
                Cell answer1Cell = row.getCell(9);
                Cell answer2Cell = row.getCell(10);
                Cell answer3Cell = row.getCell(11);
                Cell answer4Cell = row.getCell(12);
                Cell answer5Cell = row.getCell(13);
                Cell answer6Cell = row.getCell(14);
                Cell fileQuestionCell = row.getCell(15);

                Cell correctAnswersCell = row.getCell(16);
                Cell pointCell = row.getCell(17);


                if (!isExamNameRead) {
                    exam.setName(examNameCell.getStringCellValue());
                    exam.setType(typeExam.getStringCellValue());
                    exam.setCreateDate(new Date());
                    exam.setTimeExam(timeExam.getStringCellValue());
//                    exam.setTimeExam((long) timeExam.getNumericCellValue());
                    currentExamName = examNameCell.getStringCellValue();
//                    exam.setFree(isFreeCell.getBooleanCellValue());
                    examRepository.save(exam);
                    isExamNameRead = true;
                }


                String sectionName = (sectionNameCell != null) ? sectionNameCell.getStringCellValue() : null;
                String descriptionSection = (descriptionSectionCell != null) ? descriptionSectionCell.getStringCellValue() : null;
                String fileSection = (fileSectionCell != null) ? fileSectionCell.getStringCellValue() : null;
                String typeSection = (typeSectionCell != null) ? typeSectionCell.getStringCellValue() : null;
                String questionContent = (questionContentCell != null) ? questionContentCell.getStringCellValue() : null;
                String questionType = questionTypeCell != null ? questionTypeCell.getStringCellValue() : null;
                String answer1 = (answer1Cell != null) ? answer1Cell.getStringCellValue() : null;
                String answer2 = (answer2Cell != null) ? answer2Cell.getStringCellValue() : null;
                String answer3 = (answer3Cell != null) ? answer3Cell.getStringCellValue() : null;
                String answer4 = (answer4Cell != null) ? answer4Cell.getStringCellValue() : null;

                String answer5 = (answer5Cell != null) ? answer5Cell.getStringCellValue() : null;
                String answer6 = (answer6Cell != null) ? answer6Cell.getStringCellValue() : null;
                String fileQuestion = (fileQuestionCell != null) ? fileQuestionCell.getStringCellValue() : null;
                Float point = (pointCell != null) ? (float) pointCell.getNumericCellValue() : null;
                String correctAnswers = (correctAnswersCell != null) ? correctAnswersCell.getStringCellValue() : null;
//


                if (StringUtils.hasText(sectionName) && !sectionName.equals(currentNameSection)) {
                    currentNameSection = sectionName;
                    log.info("Tên section khác la : {}", fileSection);
//                    Section section = new Section();


                    currentSection = Section.builder()
                            .id(UUID.randomUUID().toString())
                            .title(sectionName)
                            .questions(new ArrayList<>())
                            .type(typeSection)
                            .file(fileSection)
                            .description(descriptionSection)
                            .exam(exam)
                            .build();


                    sectionRepository.save(currentSection);
                }
                List<Integer> correctAnswersList = new ArrayList<>();
                if (correctAnswers != null) {
                    String[] correctAnswersArray = correctAnswers.split(",");
                    for (String answer : correctAnswersArray) {
                        try {
                            int value = Integer.parseInt(answer.trim());
                            correctAnswersList.add(value);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    correctAnswersList = null;
                }


                Question question = Question.builder().content(questionContent)
                        .id(UUID.randomUUID().toString())
                        .section(currentSection)
                        .questionType(questionType)
                        .point(point)
                        .description(fileQuestion)
                        .choiceCorrect(correctAnswersList).build();

                List<Answer> answers = new ArrayList<>();
                answers.add(new Answer(answer1, 0, question));
                answers.add(new Answer(answer2, 1, question));
                answers.add(new Answer(answer3, 2, question));
                answers.add(new Answer(answer4, 3, question));
                if (StringUtils.hasText(answer5)) {
                    answers.add(new Answer(answer5, 4, question));
                }
                if (StringUtils.hasText(answer6)) {
                    answers.add(new Answer(answer6, 5, question));
                }
                question.setListAnswer(answers);
                if (currentSection != null) {
                    questionRepository.save(question);
                    List<Question> questionList = currentSection.getQuestions();
                    questionList.add(question);
                    currentSection.setQuestions(questionList);
                }


            }


            file.close();
            workbook.close();
            return exam;

        } catch (Exception e) {

            e.printStackTrace();

//            throw new RuntimeException("Không tạo thành công ");

            if (exam.getId() != null) {
                deleteExam(exam.getId());
            }
//            log.error("Lỗi xử lý dòng " + rowIterator.getClass() + ": " + e.getMessage());

        }


        return null;
    }

    /*
    public Exam createExamByExcel(String path) {
        try (FileInputStream file = new FileInputStream(new File(path));
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) {
                return null; // No data in the sheet.
            }

            Exam exam = null;
            Section currentSection = null;
            String currentExamName = null;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.STRING) {
                    if (exam != null) {
                        examRepository.save(exam);
                    }

                    exam = new Exam();
                    exam.setName(row.getCell(0).getStringCellValue());
                    exam.setType(row.getCell(1).getStringCellValue());
                    exam.setCreateDate(new Date());
                    exam.setTimeExam((long)(row.getCell(2).getNumericCellValue()));
                    exam.setFree(row.getCell(18).getBooleanCellValue());
                    currentExamName = exam.getName();
                }

                Section section = createSection(row);
                if (section != null) {
                    if (currentSection == null || !currentSection.getTitle().equals(section.getTitle())) {
                        section.setExam(exam);
                        sectionRepository.save(section);
                        currentSection = section;
                    }
                }

                Question question = createQuestion(row, currentSection);
                if (question != null) {
                    questionRepository.save(question);
                }
            }

            if (exam != null) {
                examRepository.save(exam);
            }

            return exam;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
*/
    private String getCellValueAsString(Cell cell) {
        return cell != null ? cell.getStringCellValue() : null;
    }

    private double getCellValueAsDouble(Cell cell) {
        return cell != null ? cell.getNumericCellValue() : 0.0;
    }

    private Section createSection(Row row) {
        if (row.getCell(3) != null && row.getCell(3).getCellType() == CellType.STRING) {
            return Section.builder()
                    .id(UUID.randomUUID().toString())
                    .title(row.getCell(3).getStringCellValue())
                    .questions(new ArrayList<>())
                    .type(getCellValueAsString(row.getCell(6)))
                    .file(getCellValueAsString(row.getCell(5)))
                    .description(getCellValueAsString(row.getCell(4)))
                    .build();
        }
        return null;
    }

    private Question createQuestion(Row row, Section section) {
        Cell questionContentCell = row.getCell(7);
        if (questionContentCell != null && questionContentCell.getCellType() == CellType.STRING) {
            Question.QuestionBuilder questionBuilder = Question.builder()
                    .content(questionContentCell.getStringCellValue())
                    .id(UUID.randomUUID().toString())
                    .section(section)
                    .questionType(getCellValueAsString(row.getCell(8)))
                    .point((float) getCellValueAsDouble(row.getCell(17)))
                    .description(getCellValueAsString(row.getCell(15)));

            List<Integer> correctAnswersList = new ArrayList<>();
            String correctAnswers = getCellValueAsString(row.getCell(16));
            if (StringUtils.hasText(correctAnswers)) {
                String[] correctAnswersArray = correctAnswers.split(",");
                for (String answer : correctAnswersArray) {
                    try {
                        int value = Integer.parseInt(answer.trim());
                        correctAnswersList.add(value);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            questionBuilder.choiceCorrect(correctAnswersList);

            List<Answer> answers = new ArrayList<>();
            for (int i = 9; i <= 14; i++) {
                String answerContent = getCellValueAsString(row.getCell(i));
                if (StringUtils.hasText(answerContent)) {
                    answers.add(new Answer(answerContent, i - 9, questionBuilder.build()));
                }
            }
            questionBuilder.listAnswer(answers);

            return questionBuilder.build();
        }
        return null;
    }

    @Override
    public ResponseEntity<?> getListExamByService(long userid) {
        List<ExamDTO> examDTOS = new ArrayList<>();
        Optional<User> user = userRepository.findById(userid);
        if (!user.isPresent()) {
            throw new RuntimeException("Không tìm thấy thông tin người dùng ");
        }
        User user1 = user.get();
        List<com.example.ttcn2etest.model.etity.Service> services = new ArrayList<>(user1.getServices());
        for (com.example.ttcn2etest.model.etity.Service service : services) {
            Long serviceID = service.getId();
            if (serviceID == 11) {
                examDTOS.addAll(findExamByServiceId("toiec"));
            } else if (serviceID == 12) {
                examDTOS.addAll(findExamByServiceId("ielts"));
            } else if (serviceID == 14) {
                examDTOS.addAll(findExamByServiceId("vstep_b1"));
            } else if (serviceID == 15) {
                examDTOS.addAll(findExamByServiceId("vstep_b2"));
            } else if (serviceID == 16) {
                examDTOS.addAll(findExamByServiceId("aptis_b1"));
            } else if (serviceID == 17) {
                examDTOS.addAll(findExamByServiceId("aptis_b2"));
            }

        }
        BaseListItemResponse response = new BaseListItemResponse();
        response.setSuccess();
        response.setResult(examDTOS, examDTOS.size());
        return ResponseEntity.ok(response);


    }


    public List<ExamDTO> findExamByServiceId(String typeService) {

        return examRepository.findAll().stream().filter(i -> i.getType().equals(typeService)).map(i -> mapper.map(i, ExamDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> readExamFromExcel(MultipartFile file) {

        Exam exam = new Exam();
        Iterator<Row> rowIterator = null;
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);


            String currentExamName = null;
            boolean isExamNameRead = false;
            String currentNameSection = null;
            Section currentSection = null;
            Sheet sheet = workbook.getSheetAt(0);

            rowIterator = sheet.iterator();
            rowIterator.next();
            Section sectionCurrent = null;

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();


                Cell examNameCell = row.getCell(0);
                Cell typeExam = row.getCell(1);
                Cell isMiniTest = row.getCell(2);

                Cell isFreeCell = row.getCell(18);

//
                Cell sectionNameCell = row.getCell(3);
                Cell descriptionSectionCell = row.getCell(4);
                Cell fileSectionCell = row.getCell(5);
                Cell typeSectionCell = row.getCell(6);
                Cell questionContentCell = row.getCell(7);
                Cell questionTypeCell = row.getCell(8);
                Cell answer1Cell = row.getCell(9);
                Cell answer2Cell = row.getCell(10);
                Cell answer3Cell = row.getCell(11);
                Cell answer4Cell = row.getCell(12);
                Cell answer5Cell = row.getCell(13);
                Cell answer6Cell = row.getCell(14);
                Cell fileQuestionCell = row.getCell(15);

                Cell correctAnswersCell = row.getCell(16);
                Cell pointCell = row.getCell(17);


                if (!isExamNameRead) {
                    exam.setName(examNameCell.getStringCellValue());
                    exam.setType(typeExam.getStringCellValue());
                    exam.setCreateDate(new Date());
                    exam.setTimeExam( isMiniTest.getStringCellValue());
//                    exam.setTimeExam((long) timeExam.getNumericCellValue());
                    currentExamName = examNameCell.getStringCellValue();
//                    exam.setFree(isFreeCell.getBooleanCellValue());
                    examRepository.save(exam);
                    isExamNameRead = true;
                }


                String sectionName = (sectionNameCell != null) ? sectionNameCell.getStringCellValue() : null;
                String descriptionSection = (descriptionSectionCell != null) ? descriptionSectionCell.getStringCellValue() : null;
                String fileSection = (fileSectionCell != null) ? fileSectionCell.getStringCellValue() : null;
                String typeSection = (typeSectionCell != null) ? typeSectionCell.getStringCellValue() : null;
                String questionContent = (questionContentCell != null) ? questionContentCell.getStringCellValue() : null;
                String questionType = questionTypeCell != null ? questionTypeCell.getStringCellValue() : null;
                String answer1 = (answer1Cell != null) ? answer1Cell.getStringCellValue() : null;
                String answer2 = (answer2Cell != null) ? answer2Cell.getStringCellValue() : null;
                String answer3 = (answer3Cell != null) ? answer3Cell.getStringCellValue() : null;
                String answer4 = (answer4Cell != null) ? answer4Cell.getStringCellValue() : null;

                String answer5 = (answer5Cell != null) ? answer5Cell.getStringCellValue() : null;
                String answer6 = (answer6Cell != null) ? answer6Cell.getStringCellValue() : null;
                String fileQuestion = (fileQuestionCell != null) ? fileQuestionCell.getStringCellValue() : null;
                Float point = (pointCell != null) ? (float) pointCell.getNumericCellValue() : null;
                String correctAnswers = (correctAnswersCell != null) ? correctAnswersCell.getStringCellValue() : null;
//


                if (StringUtils.hasText(sectionName) && !sectionName.equals(currentNameSection)) {
                    currentNameSection = sectionName;
                    log.info("Tên section khác la : {}", fileSection);
//                    Section section = new Section();


                    currentSection = Section.builder()
                            .id(UUID.randomUUID().toString())
                            .title(sectionName)
                            .questions(new ArrayList<>())
                            .type(typeSection)
                            .file(fileSection)
                            .description(descriptionSection)
                            .exam(exam)
                            .build();


                    sectionRepository.save(currentSection);
                }
                List<Integer> correctAnswersList = new ArrayList<>();
                if (correctAnswers != null) {
                    String[] correctAnswersArray = correctAnswers.split(",");
                    for (String answer : correctAnswersArray) {
                        try {
                            int value = Integer.parseInt(answer.trim());
                            correctAnswersList.add(value);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    correctAnswersList = null;
                }


                Question question = Question.builder().content(questionContent)
                        .id(UUID.randomUUID().toString())
                        .section(currentSection)
                        .questionType(questionType)
                        .point(point)
                        .description(fileQuestion)
                        .choiceCorrect(correctAnswersList).build();

                List<Answer> answers = new ArrayList<>();
                answers.add(new Answer(answer1, 0, question));
                answers.add(new Answer(answer2, 1, question));
                answers.add(new Answer(answer3, 2, question));
                answers.add(new Answer(answer4, 3, question));
                if (StringUtils.hasText(answer5)) {
                    answers.add(new Answer(answer5, 4, question));
                }
                if (StringUtils.hasText(answer6)) {
                    answers.add(new Answer(answer6, 5, question));
                }
                question.setListAnswer(answers);
                if (currentSection != null) {
                    questionRepository.save(question);
                    List<Question> questionList = currentSection.getQuestions();
                    questionList.add(question);
                    currentSection.setQuestions(questionList);
                }
            }


            inputStream.close();
            workbook.close();
            BaseItemResponse response = new BaseItemResponse();
            response.setSuccess();
            response.setData(exam);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {

            e.printStackTrace();

//            throw new RuntimeException("Không tạo thành công ");

            if (exam.getId() != null) {
                deleteExam(exam.getId());
            }
//            log.error("Lỗi xử lý dòng " + rowIterator.getClass() + ": " + e.getMessage());

        }


        return null;
    }

    @Override
    public ResponseEntity<?> findExamByName(String name) {
        List<DetailExamDTO> detailExamDTOS = examRepository.listExamByName(name).stream().map(i -> mapper.map(i, DetailExamDTO.class)).collect(Collectors.toList());
        BaseListItemResponse response = new BaseListItemResponse();
        response.setSuccess();
        response.setResult(detailExamDTOS, detailExamDTOS.size());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getExamByType(String type, boolean isFree) {
        List<ExamDTO> exams = new ArrayList<>();
        if (isFree == false) {
            exams = examRepository.findExamsByType(type).stream()
                    .map(i -> mapper.map(i, ExamDTO.class)).collect(Collectors.toList());
        } else {
            exams = examRepository.findExamsByType(type).stream()
                    .filter(i -> i.getIsFree() == true)
                    .map(i -> mapper.map(i, ExamDTO.class)).collect(Collectors.toList());
        }

        BaseListItemResponse<ExamDTO> response = new BaseListItemResponse<>();
        response.setSuccess(true);
        response.setResult(exams, exams.size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getDetailByExamId(String examId) {
        Exam exam = examRepository.findExamById(examId);
        DetailExamDTO detailExamDTO = mapper.map(exam , DetailExamDTO.class);
        BaseItemResponse response = new BaseItemResponse<>();
        response.setSuccess(true);
        response.setData(detailExamDTO);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<?> getExamById(String id) {
        return null;
    }
}
