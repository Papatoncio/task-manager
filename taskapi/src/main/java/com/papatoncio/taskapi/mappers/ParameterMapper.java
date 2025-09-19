package com.papatoncio.taskapi.mappers;

import com.papatoncio.taskapi.dto.parameter.ParameterRequest;
import com.papatoncio.taskapi.dto.parameter.ParameterResponse;
import com.papatoncio.taskapi.entities.Parameter;
import org.springframework.stereotype.Component;

@Component
public class ParameterMapper {
    // DTO → Entidad
    public Parameter toEntity(ParameterRequest request) {
        return Parameter.builder()
                .key(request.key())
                .value(request.value())
                .description(request.description())
                .build();
    }

    // Entidad → DTO
    public ParameterResponse toResponse(Parameter parameter) {
        return new ParameterResponse(
                parameter.getId(),
                parameter.getKey(),
                parameter.getValue(),
                parameter.getDescription(),
                parameter.getActive(),
                parameter.getUpdatedAt()
        );
    }
}
