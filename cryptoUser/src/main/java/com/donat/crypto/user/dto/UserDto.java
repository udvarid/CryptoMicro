package com.donat.crypto.user.dto;

import java.util.Map;

import com.donat.crypto.user.domain.enums.CCY;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String name;

    private String userId;

    private Map<CCY, Double> wallets;
}
