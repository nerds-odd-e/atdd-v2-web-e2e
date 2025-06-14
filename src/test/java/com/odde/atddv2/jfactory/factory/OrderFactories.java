package com.odde.atddv2.jfactory.factory;

import com.github.leeonky.jfactory.Spec;
import com.github.leeonky.jfactory.Trait;
import com.odde.atddv2.entity.Order;
import com.odde.atddv2.jfactory.mongo.Expresses;

import java.time.Clock;
import java.time.temporal.ChronoUnit;

import static com.odde.atddv2.entity.Order.OrderStatus.delivering;
import static com.odde.atddv2.entity.Order.OrderStatus.toBeDelivered;

public class OrderFactories {

    public static class 订单 extends Spec<Order> {

        @Override
        public void main() {
            property("lines").reverseAssociation("order");
            property("deliverNo").ignore();
        }

        @Trait
        public void 已发货的() {
            property("status").value(delivering);
            property("express").is(Expresses.快递信息.class);
        }

        @Trait
        public void 十五天前() {
            Clock clock = FactoryConfig.getContext().getBean(Clock.class);
            property("deliveredAt").value(clock.instant().minus(16, ChronoUnit.DAYS));
        }

        @Trait
        public void 未到十五天() {
            Clock clock = FactoryConfig.getContext().getBean(Clock.class);
            property("deliveredAt").value(clock.instant().minus(14, ChronoUnit.DAYS));
        }

        @Trait
        public void 未发货的() {
            property("status").value(toBeDelivered);
            property("deliveredAt").value(null);
        }
    }

}
