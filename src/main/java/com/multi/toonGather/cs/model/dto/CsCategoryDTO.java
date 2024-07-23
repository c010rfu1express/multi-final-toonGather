package com.multi.toonGather.cs.model.dto;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class CsCategoryDTO {
    private String csCategoryCode;
    private String csCategoryDesc;
}
