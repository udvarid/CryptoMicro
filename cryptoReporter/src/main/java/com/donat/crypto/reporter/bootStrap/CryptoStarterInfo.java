package com.donat.crypto.reporter.bootStrap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CryptoStarterInfo {
    private String name;

    private float price;

    private int count;

    private float volume;
}
