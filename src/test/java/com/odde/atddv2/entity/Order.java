package com.odde.atddv2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.odde.atddv2.entity.mongo.Express;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String code, productName, recipientName, recipientMobile, recipientAddress;

    @Access(AccessType.PROPERTY)
    private String deliverNo;

    public String getDeliverNo() {
        return express == null ? deliverNo : express.getNumber();
    }

    public void setDeliverNo(String deliverNo) {
        express = null;
        this.deliverNo = deliverNo;
    }

    @Transient
    private Express express;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<OrderLine> lines = new ArrayList<>();

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant deliveredAt;

    public enum OrderStatus {
        toBeDelivered, delivering, done
    }
}
