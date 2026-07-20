package com.com.pri_vrat.authentication.dto;

import com.com.pri_vrat.authentication.entity.AuthUserPk;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AuthUserDto {
    @NotNull(message = "{MESSAGE.USERNAME.NOT.NULL}")
    @NotBlank(message = "{MESSAGE.USERNAME.NOT.BLANK}")
    @Size(min = 4, max = 40, message = "{MESSAGE.USERNAME.VALID.SIZE}")
    @Pattern( regexp = "^[A-Za-z0-9]+$", message = "{MESSAGE.USERNAME.USERNAME.FORMAT}")
    private String userName;

    @NotBlank(message = "{MESSAGE.EMAIL.NOT.EMPTY}")
    @Email(message = "{MESSAGE.EMAIL.VALID.FORMAT}")
    private String email;

    @NotBlank(message = "{MESSAGE.FIRSTNAME.EMPTY}")
    @Pattern(regexp = "^[A-Za-z]+$", message = "{MESSAGE.FIRSTNAME.VALID.FORMAT}")
    @Size(min = 4, max = 40, message = "{MESSAGE.FIRSTNAME.VALID.SIZE}")
    private String firstName;

    @NotBlank(message = "{MESSAGE.APP.NAME.EMPTY}")
    @Pattern(regexp = "^[A-Za-z]+$", message = "{MESSAGE.APP.NAME.VALID.FORMAT}")
    @Size(min = 4, max = 40, message = "{MESSAGE.APP.NAME.VALID.SIZE}")
    private String applicationName;

    @NotBlank(message = "{MESSAGE.LASTNAME.EMPTY}")
    @Pattern(regexp = "^[A-Za-z]+$", message = "{MESSAGE.LASTNAME.VALID.FORMAT}")
    @Size(min = 4, max = 40, message = "{MESSAGE.LASTNAME.VALID.SIZE}")
    private String lastName;

    @NotNull(message = "{MESSAGE.DOB.MUST.NOT.EMPTY}")
    @Past(message = "{MESSAGE.VALID.DATE.OF.BIRTH}")
    private LocalDate dob;

    private String userType;

    private String status;

    @Size(min = 8, max = 150, message = "{MESSAGE.PASSWORD.MIN.LENGTH}")
    private String password;

}
