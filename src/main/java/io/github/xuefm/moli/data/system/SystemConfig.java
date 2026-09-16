package io.github.xuefm.moli.data.system;

import lombok.Data;

@Data
public class SystemConfig {

    /**
     * 系统是否准备好了
     */
    private Boolean ready;

    public SystemConfig() {
        this.ready = false;
    }
}
