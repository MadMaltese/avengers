package com.tableorder.dto.response;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class TokenResponse { private String token; private Long storeId; private Long tableId; private Integer tableNo; }
