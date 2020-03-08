package net.rcsoft.service.proxy.client;

public class Data {

    private String name;

    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Data) {
            Data data = (Data) o;

            return (data.getAge().hashCode() == this.getAge().hashCode()) && (data.getName().hashCode() == this.getName().hashCode());
        }

        return false;
    }
}
