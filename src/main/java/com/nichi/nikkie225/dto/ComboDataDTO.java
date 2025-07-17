package com.nichi.nikkie225.dto;

public class ComboDataDTO {
    private String code;

    public ComboDataDTO(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ComboDataDTO{" +
                "code='" + code + '\'' +
                '}';
    }
}
