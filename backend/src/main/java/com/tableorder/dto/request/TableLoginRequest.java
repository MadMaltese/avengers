package com.tableorder.dto.request;
import lombok.*;
import javax.validation.constraints.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class TableLoginRequest { @NotBlank private String storeCode; @NotNull private Integer tableNo; @NotBlank private String password; }
