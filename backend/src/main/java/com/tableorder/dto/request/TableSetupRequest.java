package com.tableorder.dto.request;
import lombok.*;
import javax.validation.constraints.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class TableSetupRequest { @NotNull private Integer tableNo; @NotBlank private String password; }
