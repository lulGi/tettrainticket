package com.lul.train.domain.station.aggregate;

import com.lul.common.core.domain.AggregateRoot;
import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.train.domain.station.valueobject.StationCode;
import lombok.Getter;

@Getter
public class Station extends AggregateRoot<String> {
    private StationCode stationCode;
    private String name;
    private String city;
    private String province;

    private Station(StationCode stationCode, String name, String city, String province) {
        super();
        this.stationCode = stationCode;
        this.name = name;
        this.city = city;
        this.province = province;
    }

    public static Station create(StationCode stationCode, String name, String city, String province) {
        validateName(name);
        return new Station(stationCode,name,city,province);
    }

    public void updateInfo(String name, String city, String province) {
        if(name != null) {
            validateName(name);
            this.name = name;
        }

        if(city != null) {
            this.city = city;
        }

        if(province != null) {
            this.province = province;
        }
    }


    private static void validateName(String name) {
        if(name == null || name.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Station name cant be empty");
        }
        if( name.length() > 100) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Station name cant be too long");
        }
    }
}
