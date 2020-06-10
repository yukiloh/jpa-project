package com.example.hibernate.domain;

public class CompanyAddress2 {
    private Integer id;
    private String address;
    private Company company;

    @Override
    public String toString() {
        return "CompanyAddress{" +
                "id=" + id +
                ", address='" + address + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
