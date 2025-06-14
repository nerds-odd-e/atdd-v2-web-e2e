package com.odde.atddv2.jfactory.mongo;

import com.github.leeonky.jfactory.Spec;
import com.odde.atddv2.entity.mongo.Express;

public class Expresses {
    public static class 快递信息 extends Spec<Express> {
        @Override
        public void main() {
            property("list[]").is(ExpressStatuses.快递状态.class);
        }
    }
}
