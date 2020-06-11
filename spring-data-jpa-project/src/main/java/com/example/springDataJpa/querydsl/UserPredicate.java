//package com.example.springDataJpa.querydsl;
//
//import com.example.springDataJpa.domain.User;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.core.types.dsl.NumberPath;
//import com.querydsl.core.types.dsl.PathBuilder;
//import com.querydsl.core.types.dsl.StringPath;
//
//import static org.hibernate.query.criteria.internal.ValueHandlerFactory.isNumeric;
//
///**
// * @author yukiloh
// * @version 0.1
// * @date 2020/6/11 13:38
// */
//public class UserPredicate {
//
//    private SearchCriteria criteria;
//
//    public UserPredicate() {
//    }
//
//    public UserPredicate(SearchCriteria criteria) {
//        this.criteria = criteria;
//    }
//
//    public BooleanExpression getPredicate() {
//        PathBuilder<User> entityPath = new PathBuilder<>(User.class, "user");
//
//        if (isNumeric(criteria.getValue().toString())) {
//            NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
//            int value = Integer.parseInt(criteria.getValue().toString());
//            switch (criteria.getOperation()) {
//                case ":":
//                    return path.eq(value);
//                case ">":
//                    return path.goe(value);
//                case "<":
//                    return path.loe(value);
//            }
//        }
//        else {
//            StringPath path = entityPath.getString(criteria.getKey());
//            if (criteria.getOperation().equalsIgnoreCase(":")) {
//                return path.containsIgnoreCase(criteria.getValue().toString());
//            }
//        }
//        return null;
//    }
//}
