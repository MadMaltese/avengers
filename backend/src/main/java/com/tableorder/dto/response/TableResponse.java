package com.tableorder.dto.response;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class TableResponse { private Long id; private Integer tableNo; private String sessionId; private String status; }
