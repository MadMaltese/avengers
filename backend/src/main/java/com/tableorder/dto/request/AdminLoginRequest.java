package com.tableorder.dto.request;
import lombok.*;
import javax.validation.constraints.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class AdminLoginRequest { @NotBlank private String storeCode; @NotBlank private String username; @NotBlank private String password; }
