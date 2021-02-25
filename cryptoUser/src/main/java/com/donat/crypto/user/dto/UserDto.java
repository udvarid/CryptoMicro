package com.donat.crypto.user.dto;

import com.donat.crypto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    public UserDto(User user) {
        this.name = user.getName();
        this.userId = user.getUserId();
    }

    public UserDto(RegisterDto registerDto) {
        this.name = registerDto.getName();
        this.userId = registerDto.getUserId();
    }

    private String name;

    private String userId;
}
