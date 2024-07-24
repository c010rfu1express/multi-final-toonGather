package com.multi.toonGather.cs.model.dto;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class CsStatusDTO {
    private String csStatusCode;
    private String csStatusDesc;
}
