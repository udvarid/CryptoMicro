package com.donat.crypto.message;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshMessage implements Serializable {

    private static final long serialVersionUID = 5878593887885097153L;
    private String message;

}
