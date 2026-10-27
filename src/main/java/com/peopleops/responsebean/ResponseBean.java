package com.peopleops.responsebean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class ResponseBean {

    private String errorCode;
    private String errorDesc;
    private Object data;

    public static ResponseBean success(String desc, Object data) {
        return new ResponseBean("200", desc, data);
    }

    public static ResponseBean error(String code, String desc) {
        return new ResponseBean(code, desc, null);
    }
}
