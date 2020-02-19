package com.carspark.sql;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Relation {

    private static Logger LOG= LoggerFactory.getLogger(Relation.class);

    private String name;
    private String id;
    private String ref;
    private Set<String> tables=new HashSet<>();
    private List<Link> links=new ArrayList<>();


    public String getName() {
        return name;
    }

    public static Relation build(String name) {
        Relation ret=new Relation();
        ret.name=name;
        return ret;
    }

    public Relation to(TableEntity tableEntity) {
        this.id=tableEntity.getName()+"."+tableEntity.getId();
        return this;
    }

    public Relation id(String id) {
        this.id=id;
        return this;
    }

    public Relation ref(String ref) {
        this.ref=ref;
        return this;
    }

    public Relation link(String from, String to) {
        setLink(from, to);
        return this;
    }

    public void setLink(String from, String to) {
        Link link=new Link();
        link.from=from;
        link.to=to;
        this.setTable(from);
        this.setTable(to);
        this.links.add(link);
    }

    public Condition getJoinCondition() {

        List c=new ArrayList<>();
        for(Link link: this.links) {
            c.add(link.getJoinCondition());
        }

        return DSL.and(c);
    }

    private void setTable(String in) {
        String[] split=in.split("\\.");
        if(split!=null && split.length>0) {
            this.tables.add(split[0]);
        }
    }

    public Set<String> getTables() {
        return this.tables;
    }

    public Table[] getFromCondition() {
        Table[] ret=new Table[this.tables.size()];
        int i=0;
        for(String table : this.tables) {
            ret[i]=DSL.table(table);
            i++;
        }
        return ret;
    }


    public List<Map<String, Object>> fetch(DSLContext dslContext, List<Object> ids) {

        Field<Object> idField = DSL.field(this.id);
        Field<Object> refField = DSL.field(this.ref);

        Condition whereCondition = DSL.and(refField.in(ids), getJoinCondition());
        Table[] fromCondition = getFromCondition();

        SelectConditionStep<Record2<Object, Object>> query = dslContext
                .select(idField.as("ID"), refField.as("REF"))
                .from(fromCondition)
                .where(whereCondition);

        LOG.info("Q[rel]: {}", query.getSQL());

        return query.fetchMaps();
    }


    public static class Link {
        private String from;
        private String to;

        public Link() {}


        public Condition getJoinCondition() {
            return DSL.field(this.from).eq(DSL.field(this.to));
        }

    }
}
