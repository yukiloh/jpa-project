package com.example.demo.domain;

public class Company {
    private Integer id;
    private String companyName;
    private CompanyAddress companyAddress;


    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", companyAddress=" + companyAddress +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public CompanyAddress getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(CompanyAddress companyAddress) {
        this.companyAddress = companyAddress;
    }
}
