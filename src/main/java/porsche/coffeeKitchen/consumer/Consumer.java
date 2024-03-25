package porsche.coffeeKitchen.consumer;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table
public class Consumer {
    @Id
    @SequenceGenerator(
            name = "consumer_sequence",
            sequenceName =  "consumer_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "consumer_sequence"
    )
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "count")
    private Integer count;
    @Column(name = "credit")
    private BigDecimal credit;

    public Consumer() {
    }

    public Consumer(String name, Integer count, Integer credit) {
        this.name = name;
        this.count = count;
        this.credit = BigDecimal.valueOf(credit);
    }

    public Consumer(long id, String name, Integer count, Integer credit) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.credit = BigDecimal.valueOf(credit);
    }

    public Consumer(String name) {
        this.name = name;
        this.count = 0;
        this.credit = BigDecimal.valueOf(0);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {return count;}

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "Consumer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", credit=" + credit.toString() +
                '}';
    }

}
