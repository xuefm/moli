package io.github.xuefm.moli.data.web;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 账号信息
 * </p>
 *
 * @author Author
 * @since 2021-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AccountInfo implements Serializable{

    private static final long serialVersionUID = 1L;


    private String id;

    private String name;

    private String token;

    private String loginName;

    private String loginPassword;

    private String phone;

    private Date registrationTime;

    private Date blockedTime;

    private Integer enabled;




}
