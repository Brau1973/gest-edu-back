package com.tecnoinf.gestedu.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenPassDTO {
    private String mailFrom;
    private String mailTo;
    private String mailSubject;
    private String tokenPassword;
}
