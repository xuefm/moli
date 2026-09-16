package io.github.xuefm.moli.data.web;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账号id
     */
    private String accountId;

    /**
     * 版本
     */
    private String version;
}
