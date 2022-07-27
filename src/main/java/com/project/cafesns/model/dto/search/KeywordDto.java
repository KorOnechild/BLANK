package com.project.cafesns.model.dto.search;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class KeywordDto {
    private List<String> type;
    private List<String> keyword;
}
