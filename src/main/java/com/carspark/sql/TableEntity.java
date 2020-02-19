package com.carspark.sql;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TableEntity {
    private static Logger LOG= LoggerFactory.getLogger(TableEntity.class);

    private final String name;
    private final String id;
    private Table table;
    private Set<String> fields;
    private Map<String, Relation> relations=new HashMap<>();

    public TableEntity(String name, String id) {
        this.name = name;
        this.id = id;
        this.table=DSL.table(this.name);
    }

    public static TableEntity table(String name, String key) {
        return new TableEntity(name, key);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Set<String> getFields() {
        return fields;
    }

    public void setFields(Collection<String> fields) {
        this.fields = new HashSet<>(fields);
    }

    public Table getTable() {
        return table;
    }

    public Relation relation(String name) {
        Relation ret= Relation.build(name);
        ret.ref(this.name+"."+this.id);
        this.relations.put(ret.getName(), ret);
        return ret;
    }

    private void setFieldsIfNeeded(DSLContext dslContext) {
        if(this.fields==null) {
            List<Map<String, Object>> schemata = dslContext.select().from(this.table).offset(0).limit(1).fetchMaps();
            if(schemata!=null && !schemata.isEmpty()) {
                this.fields=schemata.iterator().next().keySet();
            }
            if(!this.fields.contains(this.id)) {
                LOG.error("key {} not in {}", this.id, Arrays.toString(this.fields.toArray()));
            }
            else {
                LOG.info("Table {} initialized {}", this.name, Arrays.toString(this.fields.toArray()));
            }
        }
    }

    private Set<String> prj(Map<String, List<String>> in) {

        Set<String> inc=new HashSet<>();
        {
            List<String> list=in.get("inc");
            if(list!=null) {
                inc.addAll(list);
            }
        }

        Set<String> exc=new HashSet<>();
        {
            List<String> list=in.get("exc");
            if(list!=null) {
                exc.addAll(list);
            }
        }


        Set<String> all=new HashSet<>(this.fields);
        all.addAll(this.relations.keySet());


        Set<String> ret=new HashSet<>(all);
        ret.retainAll(inc);
        ret.removeAll(exc);

        // Always preserves key
        ret.add(this.id);




        return ret;
    }

    private List<Field> prj(Set<String> set) {
        Set<String> prj=new HashSet<>(this.fields);
        prj.retainAll(set);
        List<Field> ret=new ArrayList<>();
        for(String field : prj) {
            ret.add(DSL.field(this.name+"."+field));
        }
        return ret;
    }

    private List<SortField> sort(Map<String, Boolean> in) {
        List<SortField> ret=new ArrayList<>();
        for(Map.Entry<String, Boolean> entry : in.entrySet()) {
            if(entry.getValue()) {
                ret.add(DSL.field(this.name+"."+entry.getKey()).asc());
            }
            else {
                ret.add(DSL.field(this.name+"."+entry.getKey()).desc());
            }
        }
        return ret;
    }

    private String where(String in) {
        if(in==null) {
            return "1 = 1";
        }
        if(in.isEmpty()) {
            return "1 = 1";
        }
        return in;
    }

    private Collection<Relation> prjRelations(Set<String> set) {
        Set<String> prj=new HashSet<>(this.relations.keySet());
        prj.retainAll(set);
        List<Relation> ret=new ArrayList<>();
        for(String field : prj) {
            if(set.contains(field)) {
                ret.add(this.relations.get(field));
            }
        }
        return ret;
    }

    private Table[] fromCondition(Collection<Relation> prjRelations) {
        Set<String> tables=new HashSet();
        tables.add(this.getName());

        for(Relation relation : prjRelations) {
            tables.addAll(relation.getTables());
        }

        Table[] ret=new Table[tables.size()];
        int i=0;
        for(String table : tables) {
            ret[i]=DSL.table(table);
            i++;
        }

        return ret;
    }

    private Condition joinCondition() {
        List<Condition> joins=new ArrayList<>();
        joins.add(DSL.condition("1 = 1"));

        for(Relation rel : this.relations.values()) {
            joins.add(rel.getJoinCondition());
        }
        return DSL.and(joins.toArray(new Condition[joins.size()]));
    }

    private SortField[] sortCondition(Map<String, Boolean> sort) {
        List<SortField> sortFields=this.sort(sort);
        SortField[] sortFieldsArray=sortFields.toArray(new SortField[sortFields.size()]);
        return sortFieldsArray;
    }


    private List<Object> fetchIds(DSLContext dslContext, Table[] fromCondition, SortField[] sort, String where, Integer skip, Integer limit) {

        Condition joinCondition=this.joinCondition();
        Condition whereCondition=DSL.and(DSL.condition(where), joinCondition);

        Field<Object> ids = DSL.field(this.name + "." + this.id);

        Table nested= dslContext
                .select(ids.as("ID"))
                .from(fromCondition)
                .where(whereCondition)
                .orderBy(sort).asTable("NESTED");

        SelectLimitPercentAfterOffsetStep query = dslContext
                .select(nested.field("ID"))
                .from(nested)
                .groupBy(nested.field("ID"))
                .offset(skip)
                .limit(limit);


        LOG.info("Q[ids]: {}", query.getSQL());

        return query.fetch(nested.field("ID"));
    }






    public List<Map<String, Object>> fetch(DSLContext dslContext, Map<String, List<String>> prj, Map<String, Boolean> sort, String where, Integer skip, Integer limit) {
        this.setFieldsIfNeeded(dslContext);

        Set<String> prjSet=this.prj(prj);
        List<Field>  prjFields=this.prj(prjSet);

        Collection<Relation> prjRelations = prjRelations(prjSet);
        Table[] fromCondition = fromCondition(prjRelations);

        SortField[] sortCondition = sortCondition(sort);

        List<Object> ids=fetchIds(dslContext, fromCondition, sortCondition, where, skip, limit);


        Condition inIdsCondition=DSL.field(this.name + "." + this.id).in(ids);

        SelectLimitPercentAfterOffsetStep<Record> query = dslContext
                .select(prjFields)
                .from(this.table)
                .where(inIdsCondition)
                .orderBy(sortCondition)
                .offset(skip)
                .limit(limit);

        LOG.debug("Q: {}", query.getSQL());

        List<Map<String, Object>> tableResult = query.fetchMaps();

        Collector collector=new Collector(this);
        collector.setResults(tableResult);

        for(Relation relation : prjRelations) {
            List<Map<String, Object>> results = relation.fetch(dslContext, ids);
            collector.setRelationResults(relation.getName(), results);
        }

        return tableResult;
    }


}
