package ir.maktabsharif.QuizLMS.config;

import ir.maktabsharif.QuizLMS.model.DescriptiveQuestion;
import ir.maktabsharif.QuizLMS.model.MultipleChoiceQuestion;
import ir.maktabsharif.QuizLMS.model.Option;
import ir.maktabsharif.QuizLMS.model.dto.QuestionDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
public class ModelConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.createTypeMap(MultipleChoiceQuestion.class, QuestionDTO.class)
                .addMappings(mapping -> {
                    mapping.map(MultipleChoiceQuestion::getTitle, QuestionDTO::setTitle);

                    mapping.using(ctx -> {
                        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) ctx.getSource();
                        if (mcq.getOptions() != null) {
                            return mcq.getOptions().stream()
                                    .map(Option::getText)
                                    .collect(Collectors.toList());
                        }
                        return Collections.emptyList();
                    }).map(src -> src, QuestionDTO::setOptions);

                    mapping.using(ctx -> {
                        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) ctx.getSource();
                        return mcq.getOptions().stream()
                                .filter(Option::isCorrect)
                                .findFirst()
                                .map(Option::getText)
                                .orElse(null);
                    }).map(src -> src, QuestionDTO::setCorrectAnswer);

                    mapping.skip(QuestionDTO::setQuestionType);
                }).setPostConverter(context -> {
                    QuestionDTO dto = context.getDestination();
                    dto.setQuestionType("MULTIPLE_CHOICE");
                    return dto;
                });

        mapper.createTypeMap(DescriptiveQuestion.class, QuestionDTO.class)
                .addMappings(mapping -> {
                    mapping.map(DescriptiveQuestion::getTitle, QuestionDTO::setTitle);
                    mapping.map(DescriptiveQuestion::getSampleAnswer, QuestionDTO::setSampleAnswer);
                    mapping.skip(QuestionDTO::setQuestionType);
                }).setPostConverter(context -> {
                    QuestionDTO dto = context.getDestination();
                    dto.setQuestionType("DESCRIPTIVE");
                    return dto;
                });

        return mapper;
    }
}
