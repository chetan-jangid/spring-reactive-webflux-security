package com.security.reactivesecurity.util.token;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtProperties {

    private String secretKey = "H&@#^&%@#F#%&#!!VD@@#^%235BC#@R@529c52659";
    private long validity = 3600000;

}
