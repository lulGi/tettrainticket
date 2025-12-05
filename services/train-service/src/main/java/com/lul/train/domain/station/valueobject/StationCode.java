package com.lul.train.domain.station.valueobject;


import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class StationCode {

    private static final String CODE_PATTERN = "^[A-Z]{2,5}$";

    private String value;

    public StationCode(String value) {
        validate(value);
        this.value= value;
    }

    private void validate(String code) {
        if( code == null || code.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Station code cant be empty");
        }

        if(!code.matches(CODE_PATTERN)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Station code must be 2-5 UPPERCASE letters");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof  StationCode)) return false;
        StationCode that = (StationCode) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode(){
        return Objects.hash(value);
    }

}
